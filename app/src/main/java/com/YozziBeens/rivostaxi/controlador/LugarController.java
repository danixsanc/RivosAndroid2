package com.YozziBeens.rivostaxi.controlador;

import android.content.Context;
import android.util.Log;

import com.YozziBeens.rivostaxi.modelo.Lugar;
import com.YozziBeens.rivostaxi.modelo.LugarDao;
import com.YozziBeens.rivostaxi.modelo.DaoSession;
import com.YozziBeens.rivostaxi.modelo.RivosDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by savidsalazar
 */
public class LugarController {

    private static final String TAG = "Lugar";

    private Context context;

    public LugarController(Context prContext){
        this.context = prContext;
    }

    public boolean guardarLugar(Lugar prLugar){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            LugarDao oLugarDao = oRivosDB.getLugarDao();
            oLugarDao.insert(prLugar);
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
    
    public boolean guardarOActualizarLugar(Lugar prLugar){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            LugarDao oLugarDao = oRivosDB.getLugarDao();
            oLugarDao.insertOrReplace(prLugar);
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

    public boolean guardarOActualizarLugar(ArrayList<Lugar> prLugars){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            LugarDao oLugarDao = oRivosDB.getLugarDao();
            oLugarDao.insertOrReplaceInTx(prLugars);
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

    public boolean eliminarLugar(Lugar prLugar){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            LugarDao oLugarDao = oRivosDB.getLugarDao();
            oLugarDao.delete(prLugar);
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
            LugarDao oLugarDao = oRivosDB.getLugarDao();
            oLugarDao.deleteAll();
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

    public List<Lugar> obtenerLugar(){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            LugarDao oLugarDao = oRivosDB.getLugarDao();
            return oLugarDao.loadAll();
        }
        catch (Exception error){
            Log.e(TAG, error.getMessage());
            return null;
        }
        finally {
           RivosDB.getInstance().closeDatabase();
        }
    }

    public Lugar obtenerLugar(Long prKey){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            LugarDao oLugarDao = oRivosDB.getLugarDao();
            return oLugarDao.load(prKey);
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