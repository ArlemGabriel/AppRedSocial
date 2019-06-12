package com.example.appredsocial.Objetos;

import android.util.Log;

public class Amigo {
    private String nombre;
    private String apellido;
    private String urlImagen;

    public Amigo(){
    }

    public Amigo(String nombre, String apellido, String urlImagen){
        this.nombre = nombre;
        this.apellido = apellido;
        this.urlImagen = urlImagen;
    }

    public String getNombreCompleto(){
        String nomAp = getNombre()+" "+getApellido();
        return nomAp;
    }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }

    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getUrlImagen(){ return urlImagen; }

    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }
}
