package com.YozziBeens.rivostaxi.respuesta;


import com.YozziBeens.rivostaxi.modelo.Tarjeta;

import java.util.ArrayList;

public class ResultadoTarjetas {


    private ArrayList<Tarjeta> Data;
    private boolean Error;
    private String Message;
    private String Success;

    public ArrayList<Tarjeta> getData() {
        return Data;
    }

    public void setData(ArrayList<Tarjeta> data) {
        Data = data;
    }

    public boolean isError() {
        return Error;
    }

    public void setError(boolean error) {
        Error = error;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String success) {
        Success = success;
    }






}
