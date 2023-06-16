package com.example.shutapp.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shutapp.R;
import com.example.shutapp.modelo.Usuario;

//Activity que se encarga del registro y el inicio de sesion del usuario
public class LoginActivity extends AppCompatActivity {
    private EditText txtUsuario;
    private EditText txtContrasena;
    private Button btnLogin;
    private Button btnRegister;
    private CtrlUsuario cu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtContrasena = (EditText) findViewById(R.id.txtContrasena);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        cu = new CtrlUsuario(getApplicationContext());

        //Botón "ENTRAR" que valida la informacion introducida por el usuario y comprueba que este
        //esté en la BD
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario u = new Usuario();
                u.setUsuario(txtUsuario.getText().toString());
                u.setContrasena(txtContrasena.getText().toString());

                if((u.getUsuario().equals("") || u.getContrasena().equals(""))){
                    makeToast("Campos vacíos");
                }else if(cu.validate(u.getUsuario(), u.getContrasena()) == 0){
                    cambiaMain(u);
                }else{
                    makeToast("Usuario o contraseña incorrectos");
                }
            }
        });

        //Botón "REGISTRARSE" encargado de registrar a un usuario en la BD
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario u = new Usuario();
                u.setUsuario(txtUsuario.getText().toString());   //Se recogen los valores de los
                u.setContrasena(txtContrasena.getText().toString()); //cuadros de texto.

                if(cu.isNull(u) == 0){ //Se comprueba que ninguno de los campos sea nulo.
                    makeToast("Campos vacíos");
                }else if(cu.insertUser(u)){ //Se intenta insertar el usuario y cambia de actividad.
                    makeToast("Registrado Correctamente");
                    cambiaMain(u);
                }else{ //Si no se ha podido insertar es porque ya existe dicho usuario.
                    makeToast("Usuario ya existente");
                }
            }
        });
    }


    public void makeToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    //Metodo encargado de que cuando el usuario se loguee o se registre correctamente pase a la
    //MainActivity
    public void cambiaMain(Usuario u){
        Intent iMain = new Intent(LoginActivity.this, MainActivity.class);
        iMain.putExtra("user", u.getUsuario());
        startActivity(iMain);
        finish();
    }
}
