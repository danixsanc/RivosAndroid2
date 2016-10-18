package com.YozziBeens.rivostaxi.respuesta;


import com.YozziBeens.rivostaxi.modelo.Tarjeta;

import java.util.ArrayList;

public class ResultadoTarjetas {


    private ArrayList<Tarjeta> Data;
    private boolean IsError;
    private String Message;

    public ArrayList<Tarjeta> getData() {
        return Data;
    }

    public void setData(ArrayList<Tarjeta> data) {
        Data = data;
    }

    public boolean isError() {
        return IsError;
    }

    public void setError(boolean error) {
        IsError = error;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }




}
