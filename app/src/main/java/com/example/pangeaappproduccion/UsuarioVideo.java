package com.example.pangeaappproduccion;


public class UsuarioVideo {

    private String firts_name;
    private String email;
    private String last_name;
    private String password;
    private String fcm_token;
    private String telefono;
    private String profilePicture;
    private String userName;
    private String telephoneNumber;


    public String getTelephoneNumber() {
        return telephoneNumber;
    }
    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getFirts_name() {
        return firts_name;
    }
    public void setFirts_name(String firts_name) {
        this.firts_name = firts_name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getLast_name() {
        return last_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return fcm_token;
    }
    public void setToken(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(String  profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String  userName) {
        this.userName = userName;
    }
}
