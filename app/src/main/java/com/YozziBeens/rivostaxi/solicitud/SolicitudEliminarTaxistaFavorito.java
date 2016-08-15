package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudEliminarTaxistaFavorito {

    private String Client_Id;
    private String Cabbie_Id;

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



    public SolicitudEliminarTaxistaFavorito(String client_Id, String cabbie_Id) {
        Client_Id = client_Id;
        Cabbie_Id = cabbie_Id;
    }

    public SolicitudEliminarTaxistaFavorito() {

    }









}
