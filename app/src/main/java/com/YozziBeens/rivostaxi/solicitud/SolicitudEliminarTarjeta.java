package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudEliminarTarjeta {

    public SolicitudEliminarTarjeta(String client_Id, String card_id) {
        Client_Id = client_Id;
        Card_Id = card_id;
    }

    public SolicitudEliminarTarjeta() {

    }

    public String getClient_Id() {
        return Client_Id;
    }

    public void setClient_Id(String client_Id) {
        Client_Id = client_Id;
    }

    public String getCard() {
        return Card_Id;
    }

    public void setCard(String card_id) {
        Card_Id = card_id;
    }

    private String Client_Id;
    private String Card_Id;











}
