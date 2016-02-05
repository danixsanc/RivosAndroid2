package com.YozziBeens.rivostaxi.adaptadores;

/**
 * Created by danixsanc on 27/12/2015.
 */
public class AdaptadorAgregarTaxistaFavorito {
    String add_favorite_cabbie;

    public String getAdd_Favorite_Cabbie() {
        return add_favorite_cabbie;
    }

    public void setAdd_Favorite_Cabbie(String add_favorite_cabbie) {
        this.add_favorite_cabbie = add_favorite_cabbie;
    }


    public AdaptadorAgregarTaxistaFavorito(String add_favorite_cabbie) {
        super();
        this.add_favorite_cabbie = add_favorite_cabbie;
    }

}