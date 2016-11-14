package com.YozziBeens.rivostaxi.modelo;

/**
 * Created by danixsanc on 10/11/16.
 */

public class Coords {

    public Coords(String latitude, String longitude) {
        this.Latitude = latitude;
        this.Longitude = longitude;
    }

    public Coords() {
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        this.Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        this.Longitude = longitude;
    }

    private String Latitude;
    private String Longitude;
}
