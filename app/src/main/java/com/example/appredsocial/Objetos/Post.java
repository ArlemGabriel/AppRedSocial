package com.example.appredsocial.Objetos;

import java.util.Calendar;

public class Post {
    String correoUsuario;
    String urlImagen;
    String descripcion;
    String idPost;
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

    public String tiempoDePublicacion(){

        if(anno<getYear()){
            int Result=getYear()-anno;
            if(Result==1){
                return Result + " Año";
            }
            else {
                return Result + " Años";
            }
        }
        else {
            if (dia < getDay()) {
                int Result = getDay() - dia;
                if (Result == 1) {
                    return Result + " Dia";
                } else {
                    return Result + " Dias";
                }
            } else {
                if (dia == getDay()) {
                    if (hora < getHour()) {
                        int Result = getHour() - hora;
                        if (Result == 1) {
                            return Result + " Hora";
                        } else {
                            return Result + " Horas";
                        }
                    } else {
                        if (minutos < getMinute()) {
                            int Result = getMinute() - minutos;
                            if (Result == 1) {
                                return Result + " Minuto";
                            } else {
                                return Result + " Minutos";
                            }
                        } else {
                            if (segundos < getSecond()) {
                                int Result = getSecond() - segundos;
                                if (Result == 1) {
                                    return Result + " Segundo";
                                } else {
                                    return Result + " Segundos";
                                }
                            } else {
                                return "Ahora";
                            }
                        }
                    }
                }
            }
        }
        return "";
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

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
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

    //---------------------- Obtener Fecha ---------------------------
    private int getHour(){
        Calendar c = Calendar.getInstance();
        int Hora = c.get(Calendar.HOUR_OF_DAY);
        return Hora;
    }
    private int getMinute(){
        Calendar c = Calendar.getInstance();
        int Minuto = c.get(Calendar.MINUTE);
        return Minuto;
    }
    private int getDay(){
        Calendar c = Calendar.getInstance();
        int Dia = c.get(Calendar.DAY_OF_MONTH);
        return Dia;
    }
    private int getMonth(){
        Calendar c = Calendar.getInstance();
        int Mes = c.get(Calendar.MONTH)+1;
        return Mes;
    }
    private int getYear(){
        Calendar c = Calendar.getInstance();
        int Año = c.get(Calendar.YEAR);
        return Año;
    }
    private int getSecond(){
        Calendar c = Calendar.getInstance();
        int Segundo = c.get(Calendar.SECOND);
        return Segundo;
    }
}
