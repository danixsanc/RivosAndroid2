package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudToken {


    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
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

    public SolicitudToken() {
    }

    private String Price;
    private String Token;
    private String Name;
    private String Phone;
    private String Email;

    public SolicitudToken(String price, String token, String name, String phone, String email) {
        Price = price;
        Token = token;
        Name = name;
        Phone = phone;
        Email = email;
    }




}
