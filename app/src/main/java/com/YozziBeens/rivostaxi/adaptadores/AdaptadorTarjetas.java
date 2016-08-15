package com.YozziBeens.rivostaxi.adaptadores;

/**
 * Created by danixsanc on 27/12/2015.
 */
public class AdaptadorTarjetas {

    String tarjeta;
    String tarjetaId;

    public AdaptadorTarjetas(String tarjeta, String tarjetaId) {
        this.tarjeta = tarjeta;
        this.tarjetaId = tarjetaId;
    }

    public String getTarjetaId() {
        return tarjetaId;
    }

    public void setTarjetaId(String tarjetaId) {
        this.tarjetaId = tarjetaId;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }





}