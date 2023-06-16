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
        //Se recoge el nombre de usuario de la LoginActivity
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
        //Ejecutamos este método para que la aplicación esté a la escucha de mensajes recibidos
        recibir();

        //Se hace un listener para el boton de enviar
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviar();
            }
        });
    }

    //Método encargado del envío del mensaje
    private void enviar() {
        Thread hiloEnviar = new Thread(new Runnable() {
            @Override
            public void run() {
                //Se comprueba si el campo 'IP' está vacío
                if(txtIp.getText().toString().equals("")){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("El campo 'IP' está vacío.");
                        }
                    });

                }else{
                    try{
                        //Se crea el socket para la conexión y el DataOutputStream para enviar el
                        //mensaje
                        Socket socket = new Socket(txtIp.getText().toString(), PUERTO);
                        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                        //Guardo aqui el texto escrito en la caja de texto donde se escribe el
                        //mensaje
                        String mensaje = "  "+txtMsgEnviar.getText().toString()+"  ";

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Añado el mensaje al recyclerView
                                adapter.insertarItem("env" + mensaje);
                                //Paso el recyclerView a la ultima posicion para que baje el chat
                                rvChat.scrollToPosition(adapter.getListMsg().size()-1);
                                //Limpio la caja de texto para esperar el nuevo mensaje
                                txtMsgEnviar.setText("");
                            }
                        });

                        //Se envía el mensaje y se cierran el socket y el DataOutputStream
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
        //Inicio el hilo y vuelvo a bajar a la ultima posicion en el chat
        hiloEnviar.start();
        rvChat.scrollToPosition(adapter.listMsg.size()+1);
    }

    //Método encargado de recibir el mensaje
    private void recibir() {
        //Indicamos que estamos conectados
        conectado = true;

        Thread hiloRecibir = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //Hacemos un socket indicando el puerto
                    serverS = new ServerSocket(PUERTO);

                    //Mientras estemos conectados se ejecutará el siguiente bloque de código
                    while(conectado){
                        //Se crea un socket a partir de la peticion del servidor enviada por el emisor
                        //y se inicializa un lector de datos a partir del socket(ya que este traerá
                        //el mensaje)
                        Socket socket = serverS.accept();
                        DataInputStream dis = new DataInputStream(socket.getInputStream());

                        //Se lee y se guarda el mensaje en una variable
                        mensajes = dis.readUTF();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Se inserta el mensaje en el recyclerView
                                adapter.insertarItem("rec"+mensajes);//"rec" para recibidos
                                adapter.notifyDataSetChanged();
                                rvChat.scrollToPosition(adapter.getListMsg().size() -1);

                            }
                        });
                        //Se cierra el socket porque ya lo hemos usado y se cierra tambien el
                        //ObjectInputStream
                        socket.close();
                        dis.close();
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        //Se inicia el hilo recibir
        hiloRecibir.start();
        adapter.notifyDataSetChanged();
    }

    //Método encargado de hacer toast
    public void makeToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    //Se cierra la aplicacion
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Al cerrar la aplicación automaticamente se desconecta
        conectado = false;

        //Comprueba si el servidor se ha cerrado; En caso de que no, lo cierra
        try {
            if (serverS != null && !serverS.isClosed()){
                serverS.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Se vuelve a abrir cuando estaba en segundo plano o suspendida
    @Override
    protected void onResume() {
        super.onResume();
        //Se comprueba si se sigue conectado; Si no es así, lo conecta
        if (!conectado) {
            conectado = true;
        }
    }
}





















