package edu.upc.dsa.models;

public class Denuncia {

    String fecha;
    String titulo;
    String mensaje;
    String sender;

    public Denuncia(){}
    public Denuncia(String fecha, String titulo, String mensaje, String sender) {
        this.fecha = fecha;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.sender = sender;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
