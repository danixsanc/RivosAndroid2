package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudObtenerTaxistasCercanos {

    public SolicitudObtenerTaxistasCercanos(String longitude, String latitude) {
        Longitude = longitude;
        Latitude = latitude;
    }

    public SolicitudObtenerTaxistasCercanos() {
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    private String Longitude;
    private String Latitude;








}
