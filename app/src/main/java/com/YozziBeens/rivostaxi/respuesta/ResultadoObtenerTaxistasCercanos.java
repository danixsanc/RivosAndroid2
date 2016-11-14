package com.YozziBeens.rivostaxi.respuesta;


import com.YozziBeens.rivostaxi.modelo.closeCabbie;

import java.util.List;

public class ResultadoObtenerTaxistasCercanos {

    public List<closeCabbie> getData() {
        return Data;
    }

    public void setData(List<closeCabbie> data) {
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

    private List<closeCabbie> Data;
    private boolean Error;
    private String Message;




}
