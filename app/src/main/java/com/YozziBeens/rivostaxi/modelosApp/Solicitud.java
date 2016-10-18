package com.YozziBeens.rivostaxi.modelosApp;

import java.io.Serializable;

/**
 * Created by danixsanc on 28/09/16.
 */

public class Solicitud implements Serializable {


    public Solicitud() {
    }


    public Solicitud(String latOrigen, String longOrigen, String latDestino, String longDestino, String price, String timeRest, String dirOrigen, String dirDestino, String cabbie_Id, String cabbie, String latCabbie, String longCabbie, String gcmIdCabbie, String model, String passengers, String brand, String image, String dist) {
        this.latOrigen = latOrigen;
        this.longOrigen = longOrigen;
        this.latDestino = latDestino;
        this.longDestino = longDestino;
        this.price = price;
        this.timeRest = timeRest;
        this.dirOrigen = dirOrigen;
        this.dirDestino = dirDestino;
        Cabbie_Id = cabbie_Id;
        Cabbie = cabbie;
        LatCabbie = latCabbie;
        LongCabbie = longCabbie;
        GcmIdCabbie = gcmIdCabbie;
        Model = model;
        Passengers = passengers;
        Brand = brand;
        Image = image;
        Dist = dist;
    }

    public String getLatOrigen() {
        return latOrigen;
    }

    public void setLatOrigen(String latOrigen) {
        this.latOrigen = latOrigen;
    }

    public String getLongOrigen() {
        return longOrigen;
    }

    public void setLongOrigen(String longOrigen) {
        this.longOrigen = longOrigen;
    }

    public String getLatDestino() {
        return latDestino;
    }

    public void setLatDestino(String latDestino) {
        this.latDestino = latDestino;
    }

    public String getLongDestino() {
        return longDestino;
    }

    public void setLongDestino(String longDestino) {
        this.longDestino = longDestino;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTimeRest() {
        return timeRest;
    }

    public void setTimeRest(String timeRest) {
        this.timeRest = timeRest;
    }

    public String getDirOrigen() {
        return dirOrigen;
    }

    public void setDirOrigen(String dirOrigen) {
        this.dirOrigen = dirOrigen;
    }

    public String getDirDestino() {
        return dirDestino;
    }

    public void setDirDestino(String dirDestino) {
        this.dirDestino = dirDestino;
    }

    public String getCabbie_Id() {
        return Cabbie_Id;
    }

    public void setCabbie_Id(String cabbie_Id) {
        Cabbie_Id = cabbie_Id;
    }

    public String getCabbie() {
        return Cabbie;
    }

    public void setCabbie(String cabbie) {
        Cabbie = cabbie;
    }

    public String getLatCabbie() {
        return LatCabbie;
    }

    public void setLatCabbie(String latCabbie) {
        LatCabbie = latCabbie;
    }

    public String getLongCabbie() {
        return LongCabbie;
    }

    public void setLongCabbie(String longCabbie) {
        LongCabbie = longCabbie;
    }

    public String getGcmIdCabbie() {
        return GcmIdCabbie;
    }

    public void setGcmIdCabbie(String gcmIdCabbie) {
        GcmIdCabbie = gcmIdCabbie;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getPassengers() {
        return Passengers;
    }

    public void setPassengers(String passengers) {
        Passengers = passengers;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDist() {
        return Dist;
    }

    public void setDist(String dist) {
        Dist = dist;
    }

    private String latOrigen;
    private String longOrigen;
    private String latDestino;
    private String longDestino;
    private String price;
    private String timeRest;
    private String dirOrigen;
    private String dirDestino;
    private String Cabbie_Id;
    private String Cabbie;
    private String LatCabbie;
    private String LongCabbie;
    private String GcmIdCabbie;
    private String Model;
    private String Passengers;
    private String Brand;
    private String Image;
    private String Dist;
}
