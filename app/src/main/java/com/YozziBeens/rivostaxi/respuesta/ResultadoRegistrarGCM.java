package com.YozziBeens.rivostaxi.respuesta;


public class ResultadoRegistrarGCM {


    public boolean isData() {
        return Data;
    }

    public void setData(boolean data) {
        Data = data;
    }

    private boolean Data;
    private boolean Error;
    private String Message;
    private String Success;



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

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String success) {
        Success = success;
    }






}
