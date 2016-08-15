package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudRegistro {

    public SolicitudRegistro(String password, String name, String phone, String email) {
        Password = password;
        Name = name;
        Phone = phone;
        Email = email;
    }
    public SolicitudRegistro() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    private String Name;
    private String Phone;
    private String Email;
    private String Password;


}
