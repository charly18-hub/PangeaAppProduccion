package com.example.pangeaappproduccion.Listas;

public class listPublicaciones {


    private String usuario;
    private String mensaje;
    private String multimedia;
    private String status;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(String  multimedia) {
        this.multimedia = multimedia;
    }
}
