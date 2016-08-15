package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudObtenerPrecio {

    public String getLatitude_In() {
        return Latitude_In;
    }

    public void setLatitude_In(String latitude_In) {
        Latitude_In = latitude_In;
    }

    public String getLongitude_In() {
        return Longitude_In;
    }

    public void setLongitude_In(String longitude_In) {
        Longitude_In = longitude_In;
    }

    public String getLatitude_Fn() {
        return Latitude_Fn;
    }

    public void setLatitude_Fn(String latitude_Fn) {
        Latitude_Fn = latitude_Fn;
    }

    public String getLongitude_Fn() {
        return Longitude_Fn;
    }

    public void setLongitude_Fn(String longitude_Fn) {
        Longitude_Fn = longitude_Fn;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }




    public SolicitudObtenerPrecio(String latitude_In, String longitude_In, String latitude_Fn, String longitude_Fn, String distance) {
        Latitude_In = latitude_In;
        Longitude_In = longitude_In;
        Latitude_Fn = latitude_Fn;
        Longitude_Fn = longitude_Fn;
        Distance = distance;
    }

    public SolicitudObtenerPrecio() {
    }

    private String Latitude_In;
    private String Longitude_In;
    private String Latitude_Fn;
    private String Longitude_Fn;
    private String Distance;









}
