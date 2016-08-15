package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudModificarDatos {

    private String Name;
    private String Phone;
    private String Email;
    private String Password;
    private String Client_id;

    public SolicitudModificarDatos(String name, String phone, String email, String password, String client_id) {
        Name = name;
        Phone = phone;
        Email = email;
        Password = password;
        Client_id = client_id;
    }

    public SolicitudModificarDatos() {
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

    public String getClient_id() {
        return Client_id;
    }

    public void setClient_id(String client_id) {
        Client_id = client_id;
    }

}
