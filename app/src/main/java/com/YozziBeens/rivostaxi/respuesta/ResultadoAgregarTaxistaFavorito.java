package com.YozziBeens.rivostaxi.respuesta;


import com.YozziBeens.rivostaxi.modelo.Favorite_Cabbie;

import java.util.ArrayList;

public class ResultadoAgregarTaxistaFavorito {




    private ArrayList<Favorite_Cabbie> Data;
    private boolean Error;
    private String Message;
    private String Success;

    public ArrayList<Favorite_Cabbie> getData() {
        return Data;
    }

    public void setData(ArrayList<Favorite_Cabbie> data) {
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

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String success) {
        Success = success;
    }






}
