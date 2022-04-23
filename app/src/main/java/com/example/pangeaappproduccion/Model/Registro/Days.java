package com.example.pangeaappproduccion.Model.Registro;

public class Days {
    private String day;
    private String hour;

    public Days() {
    }

    public Days(String day, String hour) {
        this.day = day;
        this.hour = hour;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
