package com.YozziBeens.rivostaxi.respuesta;

import com.YozziBeens.rivostaxi.modelo.Favorite_Cabbie;
import com.YozziBeens.rivostaxi.modelo.Favorite_Place;

import java.util.ArrayList;


public class ResultadoLugaresFavoritos {

    private ArrayList<Favorite_Place> Data;
    private boolean Error;
    private String Message;
    private String Success;

    public ArrayList<Favorite_Place> getData() {
        return Data;
    }

    public void setData(ArrayList<Favorite_Place> data) {
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
