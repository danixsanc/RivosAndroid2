package com.YozziBeens.rivostaxi.respuesta;


import com.YozziBeens.rivostaxi.modelosApp.CardError;

public class ResultadoTokenError {

    private boolean Data;
    private boolean Error;
    private CardError Message;

    public boolean isData() {
        return Data;
    }

    public void setData(boolean data) {
        Data = data;
    }

    public boolean isError() {
        return Error;
    }

    public void setError(boolean error) {
        Error = error;
    }

    public CardError getMessage() {
        return Message;
    }

    public void setMessage(CardError message) {
        Message = message;
    }




}
