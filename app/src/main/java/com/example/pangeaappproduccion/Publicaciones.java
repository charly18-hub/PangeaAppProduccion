package com.example.pangeaappproduccion;

public class Publicaciones {

    private String clave;
    private String id;
    private String mensaje;
    private String multimedia;
    private String status;
    private String usuario;
    private String usuarioPublico;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
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
    public void setMultimedia(String multimedia) {
        this.multimedia = multimedia;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsuarioPublico() {
        return usuarioPublico;
    }

    public void setUsuarioPublico(String usuarioPublico) {
        this.usuarioPublico = usuarioPublico;
    }
}
