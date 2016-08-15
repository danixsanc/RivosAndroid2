package com.YozziBeens.rivostaxi.respuesta;


import com.YozziBeens.rivostaxi.modelo.Tarjeta;
import com.YozziBeens.rivostaxi.modelosApp.TarjetaId;

import java.util.ArrayList;

public class ResultadoAgregarTarjeta {

    private ArrayList<TarjetaId> Data;
    private boolean Error;
    private String Message;
    private String Success;

    public ArrayList<TarjetaId> getData() {
        return Data;
    }

    public void setData(ArrayList<TarjetaId> data) {
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
