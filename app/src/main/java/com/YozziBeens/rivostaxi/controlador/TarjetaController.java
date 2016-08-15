package com.YozziBeens.rivostaxi.controlador;

import android.content.Context;
import android.util.Log;

import com.YozziBeens.rivostaxi.modelo.HistorialPendiente;
import com.YozziBeens.rivostaxi.modelo.HistorialPendienteDao;
import com.YozziBeens.rivostaxi.modelo.Tarjeta;
import com.YozziBeens.rivostaxi.modelo.TarjetaDao;
import com.YozziBeens.rivostaxi.modelo.DaoSession;
import com.YozziBeens.rivostaxi.modelo.RivosDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by savidsalazar
 */
public class TarjetaController {

    private static final String TAG = "Tarjeta";

    private Context context;

    public TarjetaController(Context prContext){
        this.context = prContext;
    }

    public boolean guardarTarjeta(Tarjeta prTarjeta){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            TarjetaDao oTarjetaDao = oRivosDB.getTarjetaDao();
            oTarjetaDao.insert(prTarjeta);
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
    
    public boolean guardarOActualizarTarjeta(Tarjeta prTarjeta){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            TarjetaDao oTarjetaDao = oRivosDB.getTarjetaDao();
            oTarjetaDao.insertOrReplace(prTarjeta);
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

    public boolean guardarOActualizarTarjeta(ArrayList<Tarjeta> prTarjetas){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            TarjetaDao oTarjetaDao = oRivosDB.getTarjetaDao();
            oTarjetaDao.insertOrReplaceInTx(prTarjetas);
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

    public boolean eliminarTarjeta(Tarjeta prTarjeta){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            TarjetaDao oTarjetaDao = oRivosDB.getTarjetaDao();
            oTarjetaDao.delete(prTarjeta);
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
            TarjetaDao oTarjetaDao = oRivosDB.getTarjetaDao();
            oTarjetaDao.deleteAll();
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

    public List<Tarjeta> obtenerTarjeta(){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            TarjetaDao oTarjetaDao = oRivosDB.getTarjetaDao();
            return oTarjetaDao.loadAll();
        }
        catch (Exception error){
            Log.e(TAG, error.getMessage());
            return null;
        }
        finally {
           RivosDB.getInstance().closeDatabase();
        }
    }

    public Tarjeta obtenerTarjeta(Long prKey){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            TarjetaDao oTarjetaDao = oRivosDB.getTarjetaDao();
            return oTarjetaDao.load(prKey);
        }
        catch (Exception error){
            Log.e(TAG, error.getMessage());
            return null;
        }
        finally {
            RivosDB.getInstance().closeDatabase();
        }
    }

    public Tarjeta obtenerTarjetaPorTarjetaId(Long prKey){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            TarjetaDao oTarjetaDao = oRivosDB.getTarjetaDao();
            return oTarjetaDao.queryBuilder().where(TarjetaDao.Properties.Card_Id.eq(prKey)).unique();
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