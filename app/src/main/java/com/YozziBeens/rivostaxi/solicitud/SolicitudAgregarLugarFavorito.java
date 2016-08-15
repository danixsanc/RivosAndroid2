package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudAgregarLugarFavorito {

    public SolicitudAgregarLugarFavorito() {
    }

    public String getClient_Id() {
        return Client_Id;
    }

    public void setClient_Id(String client_Id) {
        Client_Id = client_Id;
    }

    public String getDesc_Place() {
        return Desc_Place;
    }

    public void setDesc_Place(String desc_Place) {
        Desc_Place = desc_Place;
    }

    public String getPlace_Name() {
        return Place_Name;
    }

    public void setPlace_Name(String place_Name) {
        Place_Name = place_Name;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    private String Client_Id;
    private String Desc_Place;
    private String Place_Name;
    private String Longitude;
    private String Latitude;

    public SolicitudAgregarLugarFavorito(String client_Id, String desc_Place, String place_Name, String longitude, String latitude) {
        Client_Id = client_Id;
        Desc_Place = desc_Place;
        Place_Name = place_Name;
        Longitude = longitude;
        Latitude = latitude;
    }








}
