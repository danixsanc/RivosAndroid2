package com.YozziBeens.rivostaxi.controlador;

import android.content.Context;
import android.util.Log;

import com.YozziBeens.rivostaxi.modelo.Favorite_Place;
import com.YozziBeens.rivostaxi.modelo.Favorite_PlaceDao;
import com.YozziBeens.rivostaxi.modelo.DaoSession;
import com.YozziBeens.rivostaxi.modelo.RivosDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by savidsalazar
 */
public class Favorite_PlaceController {

    private static final String TAG = "Favorite_Place";

    private Context context;

    public Favorite_PlaceController(Context prContext){
        this.context = prContext;
    }

    public boolean guardarFavorite_Place(Favorite_Place prFavorite_Place){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            Favorite_PlaceDao oFavorite_PlaceDao = oRivosDB.getFavorite_PlaceDao();
            oFavorite_PlaceDao.insert(prFavorite_Place);
            return true;
        }
        catch (Exception error){
            Log.e(TAG, error.getMessage());
            return false;
        }
        finally {
            RivosDB.getInstance().closeDatabase();
        }
    }
    
    public boolean guardarOActualizarFavorite_Place(Favorite_Place prFavorite_Place){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            Favorite_PlaceDao oFavorite_PlaceDao = oRivosDB.getFavorite_PlaceDao();
            oFavorite_PlaceDao.insertOrReplace(prFavorite_Place);
            return true;
        }
        catch (Exception error){
            Log.e(TAG, error.getMessage());
            return false;
        }
        finally {
            RivosDB.getInstance().closeDatabase();
        }
    }

    public boolean guardarOActualizarFavorite_Place(ArrayList<Favorite_Place> prFavorite_Places){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            Favorite_PlaceDao oFavorite_PlaceDao = oRivosDB.getFavorite_PlaceDao();
            oFavorite_PlaceDao.insertOrReplaceInTx(prFavorite_Places);
            return true;
        }
        catch (Exception error){
            Log.e(TAG, error.getMessage());
            return false;
        }
        finally {
            RivosDB.getInstance().closeDatabase();
        }
    }

    public boolean eliminarFavorite_Place(Favorite_Place prFavorite_Place){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            Favorite_PlaceDao oFavorite_PlaceDao = oRivosDB.getFavorite_PlaceDao();
            oFavorite_PlaceDao.delete(prFavorite_Place);
            return true;
        }
        catch (Exception error){
            Log.e(TAG, error.getMessage());
            return false;
        }
        finally {
            RivosDB.getInstance().closeDatabase();
        }
    }

    public boolean eliminarTodo(){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            Favorite_PlaceDao oFavorite_PlaceDao = oRivosDB.getFavorite_PlaceDao();
            oFavorite_PlaceDao.deleteAll();
            return true;
        }
        catch (Exception error){
            Log.e(TAG, error.getMessage());
            return false;
        }
        finally {
            RivosDB.getInstance().closeDatabase();
        }
    }

    public List<Favorite_Place> obtenerFavorite_Place(){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            Favorite_PlaceDao oFavorite_PlaceDao = oRivosDB.getFavorite_PlaceDao();
            return oFavorite_PlaceDao.loadAll();
        }
        catch (Exception error){
            Log.e(TAG, error.getMessage());
            return null;
        }
        finally {
           RivosDB.getInstance().closeDatabase();
        }
    }

    public Favorite_Place obtenerFavorite_Place(Long prKey){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            Favorite_PlaceDao oFavorite_PlaceDao = oRivosDB.getFavorite_PlaceDao();
            return oFavorite_PlaceDao.load(prKey);
        }
        catch (Exception error){
            Log.e(TAG, error.getMessage());
            return null;
        }
        finally {
            RivosDB.getInstance().closeDatabase();
        }
    }

    public Favorite_Place obtenerFavorite_PlacePorPlaceId(String PlaceId){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            Favorite_PlaceDao oFavorite_PlaceDao = oRivosDB.getFavorite_PlaceDao();
            return oFavorite_PlaceDao.queryBuilder().where(Favorite_PlaceDao.Properties.PlaceFavoriteId.eq(PlaceId)).unique();
        }
        catch (Exception error){
            Log.e(TAG, error.getMessage());
            return null;
        }
        finally {
            RivosDB.getInstance().closeDatabase();
        }
    }
}