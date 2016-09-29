package com.YozziBeens.rivostaxi.respuesta;


public class ResultadoAgregarLugarFavorito {


    private String Data;
    private boolean Error;
    private String Message;

    public String getData() {
        return Data;
    }

    public void setData(String data) {
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
