package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudLogin {

    public SolicitudLogin(String email, String password) {
        Email = email;
        Password = password;
    }

    public SolicitudLogin() {
    }

    private String Email;

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    private String Password;


}
