package com.YozziBeens.rivostaxi.respuesta;

import android.location.Address;

import java.util.List;

/**
 * Created by savidsalazar on 05/02/15.
 */
public class Direcciones {

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    private List<Address> addresses;


}
