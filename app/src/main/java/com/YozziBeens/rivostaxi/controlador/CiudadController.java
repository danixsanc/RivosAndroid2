package com.YozziBeens.rivostaxi.controlador;

import android.content.Context;
import android.util.Log;

import com.YozziBeens.rivostaxi.modelo.Ciudad;
import com.YozziBeens.rivostaxi.modelo.CiudadDao;
import com.YozziBeens.rivostaxi.modelo.DaoSession;
import com.YozziBeens.rivostaxi.modelo.RivosDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by savidsalazar
 */
public class CiudadController {

    private static final String TAG = "Ciudad";

    private Context context;

    public CiudadController(Context prContext){
        this.context = prContext;
    }

    public boolean guardarCiudad(Ciudad prCiudad){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            CiudadDao oCiudadDao = oRivosDB.getCiudadDao();
            oCiudadDao.insert(prCiudad);
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
    
    public boolean guardarOActualizarCiudad(Ciudad prCiudad){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            CiudadDao oCiudadDao = oRivosDB.getCiudadDao();
            oCiudadDao.insertOrReplace(prCiudad);
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

    public boolean guardarOActualizarCiudad(ArrayList<Ciudad> prCiudads){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            CiudadDao oCiudadDao = oRivosDB.getCiudadDao();
            oCiudadDao.insertOrReplaceInTx(prCiudads);
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

    public boolean eliminarCiudad(Ciudad prCiudad){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            CiudadDao oCiudadDao = oRivosDB.getCiudadDao();
            oCiudadDao.delete(prCiudad);
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
            CiudadDao oCiudadDao = oRivosDB.getCiudadDao();
            oCiudadDao.deleteAll();
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

    public List<Ciudad> obtenerCiudad(){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            CiudadDao oCiudadDao = oRivosDB.getCiudadDao();
            return oCiudadDao.loadAll();
        }
        catch (Exception error){
            Log.e(TAG, error.getMessage());
            return null;
        }
        finally {
           RivosDB.getInstance().closeDatabase();
        }
    }

    public Ciudad obtenerCiudad(Long prKey){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            CiudadDao oCiudadDao = oRivosDB.getCiudadDao();
            return oCiudadDao.load(prKey);
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