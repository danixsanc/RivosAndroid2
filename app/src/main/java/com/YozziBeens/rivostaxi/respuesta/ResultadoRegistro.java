package com.YozziBeens.rivostaxi.respuesta;

import com.YozziBeens.rivostaxi.modelo.Client;

import java.util.ArrayList;


public class ResultadoRegistro {

    private Client Data;
    private boolean Error;
    private String Message;

    public Client getData() {
        return Data;
    }

    public void setData(Client data) {
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

}
