package com.YozziBeens.rivostaxi.respuesta;

import com.YozziBeens.rivostaxi.modelo.Client;

import java.util.ArrayList;


public class ResultadoLogin {

    private Client Data;
    private boolean IsError;
    private String Message;


    public Client getData() {
        return Data;
    }

    public void setData(Client data) {
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
