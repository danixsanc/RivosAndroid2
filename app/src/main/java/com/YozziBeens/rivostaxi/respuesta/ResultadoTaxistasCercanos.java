package com.YozziBeens.rivostaxi.respuesta;

import com.YozziBeens.rivostaxi.modelosApp.TaxistasCercanos;
import com.YozziBeens.rivostaxi.modelosApp.TaxistasQueAtendieron;

import java.util.ArrayList;


public class ResultadoTaxistasCercanos {

    private ArrayList<TaxistasCercanos> Data;
    private boolean Error;
    private String Message;
    private String Success;

    public ArrayList<TaxistasCercanos> getData() {
        return Data;
    }

    public void setData(ArrayList<TaxistasCercanos> data) {
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
