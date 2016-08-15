package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudLoginFacebook {

    public SolicitudLoginFacebook(String email) {
        Email = email;
    }

    public SolicitudLoginFacebook() {
    }

    private String Email;


    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }


}
