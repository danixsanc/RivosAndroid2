package com.YozziBeens.rivostaxi.respuesta;

import com.YozziBeens.rivostaxi.modelo.Client;

import java.util.ArrayList;


public class RespuestaCadena {

    private ArrayList<Client> Data;
    private boolean IsError;
    private String Message;

    public ArrayList<Client> getData() {
        return Data;
    }

    public void setData(ArrayList<Client> data) {
        Data = data;
    }

    public boolean isError() {
        return IsError;
    }

    public void setIsError(boolean isError) {
        IsError = isError;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}


