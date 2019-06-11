package com.example.appredsocial.Objetos;

public class Post {
    String correoUsuario;
    String urlImagen;
    String descripcion;
    int idPost;
    int cantLikes;
    int cantDislikes;
    int anno;
    int mes;
    int dia;
    int hora;
    int minutos;
    int segundos;

    public Post(){

    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    public Post(String correoUsuario, String urlImagen, String descripcion, int idPost, int anno, int mes, int dia, int hora, int segundos) {
        this.correoUsuario = correoUsuario;
        this.urlImagen = urlImagen;
        this.descripcion = descripcion;
        this.idPost = idPost;
        this.cantLikes = 0;
        this.cantDislikes = 0;
        this.anno = anno;
        this.mes = mes;
        this.dia = dia;
        this.hora = hora;
        this.segundos = segundos;
    }

    public Post(String correoUsuario, String descripcion, int idPost, int anno, int mes, int dia, int hora, int segundos) {
        this.correoUsuario = correoUsuario;
        this.urlImagen = "No imagen";
        this.descripcion = descripcion;
        this.idPost = idPost;
        this.cantLikes = 0;
        this.cantDislikes = 0;
        this.anno = anno;
        this.mes = mes;
        this.dia = dia;
        this.hora = hora;
        this.segundos = segundos;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }

    public int getCantLikes() {
        return cantLikes;
    }

    public void setCantLikes(int cantLikes) {
        this.cantLikes = cantLikes;
    }

    public int getCantDislikes() {
        return cantDislikes;
    }

    public void setCantDislikes(int cantDislikes) {
        this.cantDislikes = cantDislikes;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getSegundos() {
        return segundos;
    }

    public void setSegundos(int segundos) {
        this.segundos = segundos;
    }
}
