package com.YozziBeens.rivostaxi.respuesta;

import com.YozziBeens.rivostaxi.modelo.Historial;
import com.YozziBeens.rivostaxi.modelosApp.AgregarHistorialCliente;
import com.YozziBeens.rivostaxi.modelosApp.RespSolicitud;

import java.util.ArrayList;


public class ResultadoAgregarHistorialCliente {

    private RespSolicitud Data;
    private boolean Error;
    private String Message;


    public RespSolicitud getData() {
        return Data;
    }

    public void setData(RespSolicitud data) {
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
