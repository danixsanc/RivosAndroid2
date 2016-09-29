package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudAgregarTarjeta {

    public SolicitudAgregarTarjeta(String client_Id, String conekta_Token) {
        Client_Id = client_Id;
        Conekta_Token = conekta_Token;
    }

    public SolicitudAgregarTarjeta() {
    }

    public String getClient_Id() {
        return Client_Id;
    }

    public void setClient_Id(String client_Id) {
        Client_Id = client_Id;
    }

    public String getConekta_Token() {
        return Conekta_Token;
    }

    public void setConekta_Token(String conekta_Token) {
        Conekta_Token = conekta_Token;
    }

    private String Client_Id;
    private String Conekta_Token;

}
