package com.YozziBeens.rivostaxi.respuesta;

import com.YozziBeens.rivostaxi.modelo.HistorialPendiente;

import java.util.ArrayList;


public class ResultadoHistorialPendienteCliente {

    private ArrayList<HistorialPendiente> Data;
    private boolean Error;
    private String Message;
    private String Success;

    public ArrayList<HistorialPendiente> getData() {
        return Data;
    }

    public void setData(ArrayList<HistorialPendiente> data) {
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
