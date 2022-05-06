package com.example.pangeaappproduccion;

public class MensajeChat {
    private String idUserEnvio;
    private String message;
    private String dateUtc;
    private String isActive;
    private String type;







    public String getIdUserEnvio() {
        return idUserEnvio;
    }
    public void setIdUserEnvio(String idUserEnvio) {
        this.idUserEnvio = idUserEnvio;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }


    public String getDateUtc() {
        return dateUtc;
    }
    public void setDateUtc(String dateUtc) { this.dateUtc = dateUtc; }


    public String getIsActive() {
        return isActive;
    }
    public void setIsActive(String isActive) { this.isActive = isActive; }


    public String getType() {
        return type;
    }
    public void setType(String type) { this.type = type; }


}

