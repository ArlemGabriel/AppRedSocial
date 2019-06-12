package com.example.appredsocial.Objetos;

public class Comentario {
    private String nombre;
    private String apellido;
    private String comentario;
    private String urlImagen;
    private String email;

    public Comentario(){
    }

    public Comentario(String nombre, String apellido, String urlImagen){
        this.nombre = nombre;
        this.apellido = apellido;
        this.urlImagen = urlImagen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombreCompleto(){
        String nomAp = getNombre()+" "+getApellido();
        return nomAp;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }

    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getUrlImagen(){ return urlImagen; }

    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }
}
