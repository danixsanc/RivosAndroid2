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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String desc_Place) {
        Description = desc_Place;
    }

    public String getName() {
        return Name;
    }

    public void setName(String place_Name) {
        Name = place_Name;
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
    private String Description;
    private String Name;
    private String Longitude;
    private String Latitude;

    public SolicitudAgregarLugarFavorito(String client_Id, String desc_Place, String place_Name, String longitude, String latitude) {
        Client_Id = client_Id;
        Description = desc_Place;
        Name = place_Name;
        Longitude = longitude;
        Latitude = latitude;
    }








}
