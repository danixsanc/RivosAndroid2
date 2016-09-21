package com.YozziBeens.rivostaxi.respuesta;

import com.YozziBeens.rivostaxi.modelo.Client;

import java.util.ArrayList;


public class ResultadoLogin {

    private ArrayList<Client> UserData;
    private boolean IsError;
    private String Message;


    public ArrayList<Client> getData() {
        return UserData;
    }

    public void setData(ArrayList<Client> data) {
        UserData = data;
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
