package com.YozziBeens.rivostaxi.controlador;

import android.content.Context;
import android.util.Log;

import com.YozziBeens.rivostaxi.modelo.Historial;
import com.YozziBeens.rivostaxi.modelo.HistorialDao;
import com.YozziBeens.rivostaxi.modelo.DaoSession;
import com.YozziBeens.rivostaxi.modelo.RivosDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by savidsalazar
 */
public class HistorialController {

    private static final String TAG = "Historial";

    private Context context;

    public HistorialController(Context prContext){
        this.context = prContext;
    }

    public boolean guardarHistorial(Historial prHistorial){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            HistorialDao oHistorialDao = oRivosDB.getHistorialDao();
            oHistorialDao.insert(prHistorial);
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
    
    public boolean guardarOActualizarHistorial(Historial prHistorial){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            HistorialDao oHistorialDao = oRivosDB.getHistorialDao();
            oHistorialDao.insertOrReplace(prHistorial);
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

    public boolean guardarOActualizarHistorial(ArrayList<Historial> prHistorials){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            HistorialDao oHistorialDao = oRivosDB.getHistorialDao();
            oHistorialDao.insertOrReplaceInTx(prHistorials);
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

    public boolean eliminarHistorial(Historial prHistorial){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            HistorialDao oHistorialDao = oRivosDB.getHistorialDao();
            oHistorialDao.delete(prHistorial);
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
            HistorialDao oHistorialDao = oRivosDB.getHistorialDao();
            oHistorialDao.deleteAll();
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

    public List<Historial> obtenerHistorial(){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            HistorialDao oHistorialDao = oRivosDB.getHistorialDao();
            return oHistorialDao.loadAll();
        }
        catch (Exception error){
            Log.e(TAG, error.getMessage());
            return null;
        }
        finally {
           RivosDB.getInstance().closeDatabase();
        }
    }

    public Historial obtenerHistorial(Long prKey){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            HistorialDao oHistorialDao = oRivosDB.getHistorialDao();
            return oHistorialDao.load(prKey);
        }
        catch (Exception error){
            Log.e(TAG, error.getMessage());
            return null;
        }
        finally {
            RivosDB.getInstance().closeDatabase();
        }
    }

    public Historial obtenerHistorialPorRequestId(String RequestId){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            HistorialDao ohistorialDao = oRivosDB.getHistorialDao();
            return ohistorialDao.queryBuilder().where(HistorialDao.Properties.Request_Id.eq(RequestId)).unique();
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