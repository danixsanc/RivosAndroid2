package com.YozziBeens.rivostaxi.modelosApp;

import java.io.Serializable;

/**
 * Created by danixsanc on 28/09/16.
 */

public class Solicitud implements Serializable {


    public Solicitud() {
    }

    public Solicitud(String latOrigen, String longOrigen, String latDestino, String longDestino, String price, String timeRest, String dirOrigen, String dirDestino, String cabbie_Id, String cabbie, String latcabbie, String longCabbie, String gcmIdCabbie) {
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
        LatCabbie = latcabbie;
        LongCabbie = longCabbie;
        GcmIdCabbie = gcmIdCabbie;
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

    public void setLatCabbie(String latcabbie) {
        LatCabbie = latcabbie;
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
}
