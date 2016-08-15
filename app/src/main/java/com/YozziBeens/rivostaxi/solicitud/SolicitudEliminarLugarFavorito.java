package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudEliminarLugarFavorito {

    public SolicitudEliminarLugarFavorito(String client_Id, String place_Id) {
        Client_Id = client_Id;
        Place_Id = place_Id;
    }

    public SolicitudEliminarLugarFavorito() {

    }

    public String getClient_Id() {
        return Client_Id;
    }

    public void setClient_Id(String client_Id) {
        Client_Id = client_Id;
    }

    public String getPlace_Id() {
        return Place_Id;
    }

    public void setPlace_Id(String place_Id) {
        Place_Id = place_Id;
    }

    private String Client_Id;
    private String Place_Id;











}
