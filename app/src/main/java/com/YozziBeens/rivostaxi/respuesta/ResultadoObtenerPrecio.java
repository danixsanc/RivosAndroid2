package com.YozziBeens.rivostaxi.respuesta;


import com.YozziBeens.rivostaxi.modelosApp.RespPrice;

public class ResultadoObtenerPrecio {

    private RespPrice Data;
    private boolean Error;
    private String Message;

    public RespPrice getData() {
        return Data;
    }

    public void setData(RespPrice data) {
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
