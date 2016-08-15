package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudLugaresFavoritos {

    public SolicitudLugaresFavoritos(String client_Id) {
        Client_Id = client_Id;
    }

    public SolicitudLugaresFavoritos() {
    }

    public String getClient_Id() {
        return Client_Id;
    }

    public void setClient_Id(String client_Id) {
        Client_Id = client_Id;
    }

    private String Client_Id;



}
