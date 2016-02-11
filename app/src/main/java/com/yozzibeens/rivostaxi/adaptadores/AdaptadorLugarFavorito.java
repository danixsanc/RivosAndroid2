package com.YozziBeens.rivostaxi.adaptadores;

/**
 * Created by danixsanc on 27/12/2015.
 */
public class AdaptadorLugarFavorito {
    String id_favorite_place;
    String favorite_place;

    public String getFavorite_Place() {
        return favorite_place;
    }

    public void setFavorite_Place(String favorite_place) {
        this.favorite_place = favorite_place;
    }

    public String getId_Favorite_Place() {
        return id_favorite_place;
    }

    public void setId_Favorite_Place(String id_favorite_place) {
        this.id_favorite_place = id_favorite_place;
    }


    public AdaptadorLugarFavorito(String id_favorite_place, String favorite_place) {
        super();
        this.id_favorite_place = id_favorite_place;
        this.favorite_place = favorite_place;
    }

}