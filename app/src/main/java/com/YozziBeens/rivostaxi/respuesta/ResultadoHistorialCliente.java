package com.YozziBeens.rivostaxi.respuesta;

import com.YozziBeens.rivostaxi.modelo.Historial;

import java.util.ArrayList;


public class ResultadoHistorialCliente {

    private ArrayList<Historial> Data;
    private boolean Error;
    private String Message;
    private String Success;

    public ArrayList<Historial> getData() {
        return Data;
    }

    public void setData(ArrayList<Historial> data) {
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
