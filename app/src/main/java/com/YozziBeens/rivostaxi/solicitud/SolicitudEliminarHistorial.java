package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudEliminarHistorial {

    private String Client_Id;
    private String Request_Id;

    public String getClient_Id() {
        return Client_Id;
    }

    public void setClient_Id(String client_Id) {
        Client_Id = client_Id;
    }

    public String getRequest_Id() {
        return Request_Id;
    }

    public void setRequest_Id(String request_Id) {
        Request_Id = request_Id;
    }

    public SolicitudEliminarHistorial(String client_Id, String request_Id) {
        Client_Id = client_Id;
        Request_Id = request_Id;
    }
    public SolicitudEliminarHistorial() {
    }





}
