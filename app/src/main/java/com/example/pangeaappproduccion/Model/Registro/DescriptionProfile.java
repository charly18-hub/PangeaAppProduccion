package com.example.pangeaappproduccion.Model.Registro;

public class DescriptionProfile {
    private String cv;
    private String description;
    private String videoLink;

    public DescriptionProfile() {
    }

    public DescriptionProfile(String cv, String description, String videoLink) {
        this.cv = cv;
        this.description = description;
        this.videoLink = videoLink;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }
}
