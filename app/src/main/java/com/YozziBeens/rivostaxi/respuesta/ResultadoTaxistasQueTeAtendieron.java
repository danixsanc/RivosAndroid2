package com.YozziBeens.rivostaxi.respuesta;

import com.YozziBeens.rivostaxi.modelo.HistorialPendiente;
import com.YozziBeens.rivostaxi.modelosApp.TaxistasQueAtendieron;

import java.util.ArrayList;


public class ResultadoTaxistasQueTeAtendieron {

    private ArrayList<TaxistasQueAtendieron> Data;
    private boolean Error;
    private String Message;
    private String Success;

    public ArrayList<TaxistasQueAtendieron> getData() {
        return Data;
    }

    public void setData(ArrayList<TaxistasQueAtendieron> data) {
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
