package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudCancelCabbie {


    public SolicitudCancelCabbie(String cabbie_Id) {
        Cabbie_Id = cabbie_Id;
    }

    public String getCabbie_Id() {
        return Cabbie_Id;
    }

    public void setCabbie_Id(String cabbie_Id) {
        Cabbie_Id = cabbie_Id;
    }

    public SolicitudCancelCabbie() {
    }

    private String Cabbie_Id;

}
