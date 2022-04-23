package com.example.pangeaappproduccion.Model.Registro;

public class ImagenPerfil {
    private String multimedia;
    private String usuario;

    public ImagenPerfil() {
    }

    public ImagenPerfil(String multimedia, String usuario) {
        this.multimedia = multimedia;
        this.usuario = usuario;
    }

    public String getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(String multimedia) {
        this.multimedia = multimedia;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
