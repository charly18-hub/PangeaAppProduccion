package com.example.pangeaappproduccion.Model.Registro;

import java.util.List;

public class Availability {
    private List<Days> days;
    private String zone;

    public Availability() {
    }

    public Availability(List<Days> days, String zone) {
        this.days = days;
        this.zone = zone;
    }

    public List<Days> getDays() {
        return days;
    }

    public void setDays(List<Days> days) {
        this.days = days;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
