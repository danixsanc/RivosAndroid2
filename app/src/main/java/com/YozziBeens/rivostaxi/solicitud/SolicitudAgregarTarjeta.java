package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudAgregarTarjeta {

    private String Client_Id;
    private String Card;
    private String Month;
    private String Year;
    private String Name_Card;

    public SolicitudAgregarTarjeta(String client_Id, String card, String month, String year, String name_Card) {
        Client_Id = client_Id;
        Card = card;
        Month = month;
        Year = year;
        Name_Card = name_Card;
    }

    public SolicitudAgregarTarjeta() {
    }

    public String getClient_Id() {
        return Client_Id;
    }

    public void setClient_Id(String client_Id) {
        Client_Id = client_Id;
    }

    public String getCard() {
        return Card;
    }

    public void setCard(String card) {
        Card = card;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getName_Card() {
        return Name_Card;
    }

    public void setName_Card(String name_Card) {
        Name_Card = name_Card;
    }















}
