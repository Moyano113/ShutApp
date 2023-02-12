package com.example.shutapp.modelo;

import java.io.Serializable;

public class Envio implements Serializable {
    String ip;
    String nombre;
    String msg;

    public Envio(String ip, String nombre, String msg){
        this.ip = ip;
        this.nombre = nombre;
        this.msg = msg;
    }

    public String getIp(){return ip;}

    public void setIp(String ip){this.ip = ip;}

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getMsg() {return msg;}

    public void setMsg(String msg) {this.msg = msg;}
}
