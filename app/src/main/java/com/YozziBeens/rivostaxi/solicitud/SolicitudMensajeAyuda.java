package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudMensajeAyuda {

    private String Client_Id;
    private String Subject;
    private String Message;

    public SolicitudMensajeAyuda(String client_Id, String subject, String message) {
        Client_Id = client_Id;
        Subject = subject;
        Message = message;
    }

    public SolicitudMensajeAyuda() {
    }

    public String getClient_Id() {
        return Client_Id;
    }

    public void setClient_Id(String client_Id) {
        Client_Id = client_Id;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }





}
