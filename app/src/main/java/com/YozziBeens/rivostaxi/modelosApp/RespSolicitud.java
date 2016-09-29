package com.YozziBeens.rivostaxi.modelosApp;

/**
 * Created by danixsanc on 28/09/16.
 */

public class RespSolicitud {


    public RespSolicitud(String ref, String date) {
        Ref = ref;
        Date = date;
    }

    public RespSolicitud() {
    }

    public String getRef() {
        return Ref;
    }

    public void setRef(String ref) {
        Ref = ref;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    private String Ref;
    private String Date;
}
