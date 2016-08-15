package com.YozziBeens.rivostaxi.controlador;

import android.content.Context;
import android.util.Log;

import com.YozziBeens.rivostaxi.modelo.HistorialDao;
import com.YozziBeens.rivostaxi.modelo.HistorialPendiente;
import com.YozziBeens.rivostaxi.modelo.HistorialPendienteDao;
import com.YozziBeens.rivostaxi.modelo.DaoSession;
import com.YozziBeens.rivostaxi.modelo.RivosDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by savidsalazar
 */
public class HistorialPendienteController {

    private static final String TAG = "HistorialPendiente";

    private Context context;

    public HistorialPendienteController(Context prContext){
        this.context = prContext;
    }

    public boolean guardarHistorialPendiente(HistorialPendiente prHistorialPendiente){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            HistorialPendienteDao oHistorialPendienteDao = oRivosDB.getHistorialPendienteDao();
            oHistorialPendienteDao.insert(prHistorialPendiente);
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
    
    public boolean guardarOActualizarHistorialPendiente(HistorialPendiente prHistorialPendiente){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            HistorialPendienteDao oHistorialPendienteDao = oRivosDB.getHistorialPendienteDao();
            oHistorialPendienteDao.insertOrReplace(prHistorialPendiente);
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

    public boolean guardarOActualizarHistorialPendiente(ArrayList<HistorialPendiente> prHistorialPendientes){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            HistorialPendienteDao oHistorialPendienteDao = oRivosDB.getHistorialPendienteDao();
            oHistorialPendienteDao.insertOrReplaceInTx(prHistorialPendientes);
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

    public boolean eliminarHistorialPendiente(HistorialPendiente prHistorialPendiente){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            HistorialPendienteDao oHistorialPendienteDao = oRivosDB.getHistorialPendienteDao();
            oHistorialPendienteDao.delete(prHistorialPendiente);
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
            HistorialPendienteDao oHistorialPendienteDao = oRivosDB.getHistorialPendienteDao();
            oHistorialPendienteDao.deleteAll();
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

    public List<HistorialPendiente> obtenerHistorialPendiente(){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            HistorialPendienteDao oHistorialPendienteDao = oRivosDB.getHistorialPendienteDao();
            return oHistorialPendienteDao.loadAll();
        }
        catch (Exception error){
            Log.e(TAG, error.getMessage());
            return null;
        }
        finally {
           RivosDB.getInstance().closeDatabase();
        }
    }

    public HistorialPendiente obtenerHistorialPendiente(Long prKey){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            HistorialPendienteDao oHistorialPendienteDao = oRivosDB.getHistorialPendienteDao();
            return oHistorialPendienteDao.load(prKey);
        }
        catch (Exception error){
            Log.e(TAG, error.getMessage());
            return null;
        }
        finally {
            RivosDB.getInstance().closeDatabase();
        }
    }


    public HistorialPendiente obtenerHistorialPendientePorRequestId(Long RequestId){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            HistorialPendienteDao oHistorialPendienteDao = oRivosDB.getHistorialPendienteDao();
            return oHistorialPendienteDao.queryBuilder().where(HistorialPendienteDao.Properties.Request_Id.eq(RequestId)).unique();
            //return oHistorialPendienteDao.load(prKey);
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