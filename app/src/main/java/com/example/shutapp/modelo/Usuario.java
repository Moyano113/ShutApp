package com.example.shutapp.modelo;

//Clase usada para guardar la informacion del usuario que este usando el chat
public class Usuario {
    private int id;
    private String usuario;
    private String contrasena;

    public Usuario(){}

    public Usuario(String usuario, String contrasena){
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
