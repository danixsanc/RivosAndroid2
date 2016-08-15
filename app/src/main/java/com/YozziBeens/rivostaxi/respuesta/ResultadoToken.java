package com.YozziBeens.rivostaxi.respuesta;


public class ResultadoToken {

    private boolean Data;
    private boolean Error;
    private String Message;

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

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }




}
