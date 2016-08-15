package com.YozziBeens.rivostaxi.adaptadores;

/**
 * Created by danixsanc on 27/12/2015.
 */
public class AdaptadorHistorialPendiente {
    String pending_history;



    public String getPending_History() {
        return pending_history;
    }

    public void setPending_Histor(String pending_history) {
        this.pending_history = pending_history;
    }


    public AdaptadorHistorialPendiente(String id, String pending_history) {
        super();
        this.pending_history = pending_history;
    }

}