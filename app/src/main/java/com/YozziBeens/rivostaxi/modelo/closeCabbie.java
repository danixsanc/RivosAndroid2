package com.YozziBeens.rivostaxi.modelo;

/**
 * Created by danixsanc on 09/11/16.
 */

public class closeCabbie {

    public closeCabbie(String latitude, String longitude, String cabbie_Id) {
        Latitude = latitude;
        Longitude = longitude;
        Cabbie_Id = cabbie_Id;
    }

    public closeCabbie() {
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

    public String getCabbie_Id() {
        return Cabbie_Id;
    }

    public void setCabbie_Id(String cabbie_Id) {
        Cabbie_Id = cabbie_Id;
    }

    private String Latitude;
    private String Longitude;
    private String Cabbie_Id;
}
