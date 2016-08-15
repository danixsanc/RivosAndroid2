package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudAgregarTaxistaFavorito {


    public SolicitudAgregarTaxistaFavorito(String client_Id, String cabbie_Id) {
        Client_Id = client_Id;
        Cabbie_Id = cabbie_Id;
    }

    private String Client_Id;
    private String Cabbie_Id;

    public SolicitudAgregarTaxistaFavorito() {
    }

    public String getCabbie_Id() {
          return Cabbie_Id;
    }

    public void setCabbie_Id(String cabbie_Id) {
        Cabbie_Id = cabbie_Id;
    }

    public String getClient_Id() {
        return Client_Id;
    }

    public void setClient_Id(String client_Id) {
        Client_Id = client_Id;
    }









}
