package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudAgregarHistorialCliente {


    public SolicitudAgregarHistorialCliente(String latitude_In, String longitude_In, String latitude_Fn, String longitude_Fn, String client_Id, String cabbie_Id, String price_Id, String inicio, String destino, String tipo) {
        Latitude_In = latitude_In;
        Longitude_In = longitude_In;
        Latitude_Fn = latitude_Fn;
        Longitude_Fn = longitude_Fn;
        Client_Id = client_Id;
        Cabbie_Id = cabbie_Id;
        Price_Id = price_Id;
        Inicio = inicio;
        Destino = destino;
        Tipo = tipo;
    }

    public SolicitudAgregarHistorialCliente() {
    }

    public String getLatitude_In() {
        return Latitude_In;
    }

    public void setLatitude_In(String latitude_In) {
        Latitude_In = latitude_In;
    }

    public String getLongitude_In() {
        return Longitude_In;
    }

    public void setLongitude_In(String longitude_In) {
        Longitude_In = longitude_In;
    }

    public String getLatitude_Fn() {
        return Latitude_Fn;
    }

    public void setLatitude_Fn(String latitude_Fn) {
        Latitude_Fn = latitude_Fn;
    }

    public String getLongitude_Fn() {
        return Longitude_Fn;
    }

    public void setLongitude_Fn(String longitude_Fn) {
        Longitude_Fn = longitude_Fn;
    }

    public String getClient_Id() {
        return Client_Id;
    }

    public void setClient_Id(String client_Id) {
        Client_Id = client_Id;
    }

    public String getCabbie_Id() {
        return Cabbie_Id;
    }

    public void setCabbie_Id(String cabbie_Id) {
        Cabbie_Id = cabbie_Id;
    }

    public String getPrice_Id() {
        return Price_Id;
    }

    public void setPrice_Id(String price_Id) {
        Price_Id = price_Id;
    }

    public String getInicio() {
        return Inicio;
    }

    public void setInicio(String inicio) {
        Inicio = inicio;
    }

    public String getDestino() {
        return Destino;
    }

    public void setDestino(String destino) {
        Destino = destino;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    private String Latitude_In;
    private String Longitude_In;
    private String Latitude_Fn;
    private String Longitude_Fn;
    private String Client_Id;
    private String Cabbie_Id;
    private String Price_Id;
    private String Inicio;
    private String Destino;
    private String Tipo;










}
