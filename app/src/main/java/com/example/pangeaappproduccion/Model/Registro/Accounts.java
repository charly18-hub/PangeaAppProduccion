package com.example.pangeaappproduccion.Model.Registro;

public class Accounts {
    private String facebook;
    private String iOS;
    private String instagram;
    private String twitter;

    public Accounts() {
    }

    public Accounts(String facebook, String iOS, String instagram, String twitter) {
        this.facebook = facebook;
        this.iOS = iOS;
        this.instagram = instagram;
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getiOS() {
        return iOS;
    }

    public void setiOS(String iOS) {
        this.iOS = iOS;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
}
