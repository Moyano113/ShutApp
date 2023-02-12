package com.example.shutapp.vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shutapp.R;
import com.example.shutapp.adapter.RecyclerAdapter;
import com.example.shutapp.modelo.Envio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Runnable{
    private String user;
    private Thread hServidor;
    private Thread hCliente;
    private List<Envio> listMsg;
    private RecyclerAdapter adapter;

    private RecyclerView rvChat;
    private TextView lblUsuario;
    private EditText txtIp;
    private Button btnEnviar;
    private EditText txtMsgEnviar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = getIntent().getStringExtra("user");
        adapter = new RecyclerAdapter(listMsg);
        rvChat = findViewById(R.id.rvChat);
        rvChat.setAdapter(adapter);
        lblUsuario = findViewById(R.id.lblUsuario);
        txtIp = findViewById(R.id.txtIp);
        btnEnviar = findViewById(R.id.btnEnviar);
        txtMsgEnviar = findViewById(R.id.txtMsgEnviar);

        lblUsuario.setText("Usuario: " + user);
        rvChat.setLayoutManager(new LinearLayoutManager(this));

        hServidor = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ServerSocket ss = new ServerSocket(1244);
                    String ip, user, msg;
                    Envio recibido;

                    while(true){
                        Socket s = ss.accept();
                        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                        recibido = (Envio)ois.readObject();

                        ip = recibido.getIp();
                        user = recibido.getNombre();
                        msg = recibido.getMsg();

                        recibido.setIp(ip);
                        recibido.setNombre(user);
                        recibido.setMsg(msg);

                        adapter.addMsg(recibido);
                        s.close();
                        ois.close();
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } catch (ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        hCliente = new Thread(new Runnable() {
            @Override
            public void run() {
                btnEnviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(txtIp.getText().toString().equals("")){
                            makeToast("El campo 'IP' está vacío.");
                        }else if(!txtMsgEnviar.getText().toString().equals("")){
                            try{
                                Socket s = new Socket("192.168.1.66", 1244);
                                Envio envio = new Envio(txtIp.getText().toString(), user, txtMsgEnviar.getText().toString());
                                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

                                oos.writeObject(envio);
                                adapter.addMsg(envio);

                                s.close();
                                oos.close();
                            } catch (UnknownHostException e) {
                                makeToast("Ip incorrecta");
                            } catch (IOException e) {
                                makeToast("Error de conexion");
                            }
                        }
                    }
                });
            }
        });

        hServidor.start();
        hCliente.start();

    }

    public void makeToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void run() {

    }
}





















