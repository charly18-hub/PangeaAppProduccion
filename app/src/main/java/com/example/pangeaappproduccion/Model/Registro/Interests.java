package com.example.pangeaappproduccion.Model.Registro;

import java.util.List;

public class Interests {
    private String idealPartner;
    private List<String> list;

    public Interests() {
    }


    public Interests(String idealPartner) {
        this.idealPartner = idealPartner;
    }
    public Interests(String idealPartner, List<String> list) {
        this.idealPartner = idealPartner;
        this.list = list;
    }

    public String getIdealPartner() {
        return idealPartner;
    }

    public void setIdealPartner(String idealPartner) {
        this.idealPartner = idealPartner;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
