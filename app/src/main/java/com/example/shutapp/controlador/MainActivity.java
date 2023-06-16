package com.example.shutapp.controlador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shutapp.R;
import com.example.shutapp.adapter.RecyclerAdapter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity{
    private String user;
    private RecyclerAdapter adapter;
    private RecyclerView rvChat;
    private TextView lblUsuario;
    public EditText txtIp;
    private ImageView btnEnviar;
    private EditText txtMsgEnviar;
    private Boolean conectado;
    private ServerSocket serverS;

    private String mensajes = "";

    private static int PUERTO = 1244;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = getIntent().getStringExtra("user");

        lblUsuario = findViewById(R.id.lblUsuario);
        txtIp = findViewById(R.id.txtIp);
        btnEnviar = findViewById(R.id.btnEnviar);
        txtMsgEnviar = findViewById(R.id.txtMsgEnviar);
        lblUsuario.setText("Usuario: " + user);

        rvChat = findViewById(R.id.rvChat);

        adapter = new RecyclerAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvChat.setLayoutManager(linearLayoutManager);
        rvChat.setAdapter(adapter);

        serverS = null;

        recibir();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviar();
            }
        });
    }

    private void enviar() {
        Thread hiloEnviar = new Thread(new Runnable() {
            @Override
            public void run() {
                if(txtIp.getText().toString().equals("")){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("El campo 'IP' está vacío.");
                        }
                    });

                }else{
                    try{
                        Socket socket = new Socket(txtIp.getText().toString(), PUERTO);
                        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                        String mensaje = txtMsgEnviar.getText().toString();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.insertarItem("env" + mensaje);
                                rvChat.scrollToPosition(adapter.getListMsg().size()-1);
                                txtMsgEnviar.setText("");
                            }
                        });


                        dos.writeUTF(mensaje);
                        dos.close();
                        socket.close();

                    } catch (UnknownHostException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                makeToast("Ip incorrecta");
                            }
                        });

                    } catch (IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                makeToast("Error de conexion");
                            }
                        });

                    }
                }
            }
        });
        hiloEnviar.start();
        rvChat.scrollToPosition(adapter.listMsg.size()+1);
    }

    private void recibir() {

        conectado = true;

        Thread hiloRecibir = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    serverS = new ServerSocket(PUERTO);

                    while(conectado){

                        Socket socket = serverS.accept();
                        DataInputStream dis = new DataInputStream(socket.getInputStream());

                        mensajes = dis.readUTF();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                adapter.insertarItem("rec"+mensajes);
                                adapter.notifyDataSetChanged();
                                rvChat.scrollToPosition(adapter.getListMsg().size() -1);

                            }
                        });

                        socket.close();
                        dis.close();
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        hiloRecibir.start();
        adapter.notifyDataSetChanged();
    }

    public void makeToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        conectado = false;

        try {
            if (serverS != null && !serverS.isClosed()){
                serverS.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!conectado) {
            conectado = true;
        }
    }
}





















