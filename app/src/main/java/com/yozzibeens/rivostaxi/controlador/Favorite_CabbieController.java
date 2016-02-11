package com.YozziBeens.rivostaxi.controlador;

import android.content.Context;
import android.util.Log;

import com.YozziBeens.rivostaxi.modelo.Favorite_Cabbie;
import com.YozziBeens.rivostaxi.modelo.Favorite_CabbieDao;
import com.YozziBeens.rivostaxi.modelo.DaoSession;
import com.YozziBeens.rivostaxi.modelo.RivosDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by savidsalazar
 */
public class Favorite_CabbieController {

    private static final String TAG = "Favorite_Cabbie";

    private Context context;

    public Favorite_CabbieController(Context prContext){
        this.context = prContext;
    }

    public boolean guardarFavorite_Cabbie(Favorite_Cabbie prFavorite_Cabbie){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            Favorite_CabbieDao oFavorite_CabbieDao = oRivosDB.getFavorite_CabbieDao();
            oFavorite_CabbieDao.insert(prFavorite_Cabbie);
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
    
    public boolean guardarOActualizarFavorite_Cabbie(Favorite_Cabbie prFavorite_Cabbie){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            Favorite_CabbieDao oFavorite_CabbieDao = oRivosDB.getFavorite_CabbieDao();
            oFavorite_CabbieDao.insertOrReplace(prFavorite_Cabbie);
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

    public boolean guardarOActualizarFavorite_Cabbie(ArrayList<Favorite_Cabbie> prFavorite_Cabbies){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            Favorite_CabbieDao oFavorite_CabbieDao = oRivosDB.getFavorite_CabbieDao();
            oFavorite_CabbieDao.insertOrReplaceInTx(prFavorite_Cabbies);
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

    public boolean eliminarFavorite_Cabbie(Favorite_Cabbie prFavorite_Cabbie){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            Favorite_CabbieDao oFavorite_CabbieDao = oRivosDB.getFavorite_CabbieDao();
            oFavorite_CabbieDao.delete(prFavorite_Cabbie);
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
            Favorite_CabbieDao oFavorite_CabbieDao = oRivosDB.getFavorite_CabbieDao();
            oFavorite_CabbieDao.deleteAll();
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

    public List<Favorite_Cabbie> obtenerFavorite_Cabbie(){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            Favorite_CabbieDao oFavorite_CabbieDao = oRivosDB.getFavorite_CabbieDao();
            return oFavorite_CabbieDao.loadAll();
        }
        catch (Exception error){
            Log.e(TAG, error.getMessage());
            return null;
        }
        finally {
           RivosDB.getInstance().closeDatabase();
        }
    }

    public Favorite_Cabbie obtenerFavorite_Cabbie(Long prKey){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            Favorite_CabbieDao oFavorite_CabbieDao = oRivosDB.getFavorite_CabbieDao();
            return oFavorite_CabbieDao.load(prKey);
        }
        catch (Exception error){
            Log.e(TAG, error.getMessage());
            return null;
        }
        finally {
            RivosDB.getInstance().closeDatabase();
        }
    }

    public Favorite_Cabbie obtenerFavorite_CabbiePorCabbieId(String CabbieId){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            Favorite_CabbieDao oFavorite_CabbieDao = oRivosDB.getFavorite_CabbieDao();
            return oFavorite_CabbieDao.queryBuilder().where(Favorite_CabbieDao.Properties.CabbieFavoriteId.eq(CabbieId)).unique();
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