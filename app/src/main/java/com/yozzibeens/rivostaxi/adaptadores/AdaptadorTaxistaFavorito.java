package com.YozziBeens.rivostaxi.adaptadores;

/**
 * Created by danixsanc on 27/12/2015.
 */
public class AdaptadorTaxistaFavorito {
    String favorite_cabbie;
    String fcabbieId;

    public String getFavorite_Cabbie() {
        return favorite_cabbie;
    }

    public void setFavorite_Cabbie(String favorite_cabbie) {
        this.favorite_cabbie = favorite_cabbie;
    }

    public String getFavoriteCabbieId() {
        return fcabbieId;
    }

    public void setFavoriteCabbieId(String fcabbieId) {
        this.fcabbieId = fcabbieId;
    }


    public AdaptadorTaxistaFavorito(String fcabbieId , String favorite_cabbie) {
        super();
        this.favorite_cabbie = favorite_cabbie;
        this.fcabbieId = fcabbieId;
    }

}