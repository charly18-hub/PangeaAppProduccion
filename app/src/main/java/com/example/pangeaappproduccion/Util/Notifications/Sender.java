package com.example.pangeaappproduccion.Util.Notifications;

public class Sender {
    private Data data;
    private String to;
    public Sender(){

    }

    public Data getData() {
        return data;
    }

    public Sender(Data data, String to) {
        this.data = data;
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
