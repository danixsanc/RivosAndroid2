package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudRegistrarGCM {


    public SolicitudRegistrarGCM(String client_Id, String gcmId) {
        Client_Id = client_Id;
        GcmId = gcmId;
    }

    public SolicitudRegistrarGCM() {
    }

    public String getClient_Id() {
        return Client_Id;
    }

    public void setClient_Id(String client_Id) {
        Client_Id = client_Id;
    }

    public String getGcmId() {
        return GcmId;
    }

    public void setGcmId(String gcmId) {
        GcmId = gcmId;
    }

    private String Client_Id;
    private String GcmId;





}
