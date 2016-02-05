package com.YozziBeens.rivostaxi.adaptadores;

/**
 * Created by danixsanc on 27/12/2015.
 */
public class AdaptadorHistorial {

    String history;
    String historialId;

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getHistorialId() {
        return historialId;
    }

    public void setHistorialId(String historialId) {
        this.historialId = historialId;
    }


    public AdaptadorHistorial(String historialId, String history) {
        super();
        this.historialId = historialId;
        this.history = history;
    }

}