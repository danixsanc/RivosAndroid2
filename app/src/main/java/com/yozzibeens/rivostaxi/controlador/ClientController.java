package com.YozziBeens.rivostaxi.controlador;

import android.content.Context;
import android.util.Log;

import com.YozziBeens.rivostaxi.modelo.Client;
import com.YozziBeens.rivostaxi.modelo.ClientDao;
import com.YozziBeens.rivostaxi.modelo.DaoSession;
import com.YozziBeens.rivostaxi.modelo.RivosDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by savidsalazar
 */
public class ClientController {

    private static final String TAG = "Client";

    private Context context;

    public ClientController(Context prContext){
        this.context = prContext;
    }

    public boolean guardarClient(Client prClient){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            ClientDao oClientDao = oRivosDB.getClientDao();
            oClientDao.insert(prClient);
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
    
    public boolean guardarOActualizarClient(Client prClient){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            ClientDao oClientDao = oRivosDB.getClientDao();
            oClientDao.insertOrReplace(prClient);
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

    public boolean guardarOActualizarClient(ArrayList<Client> prClients){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            ClientDao oClientDao = oRivosDB.getClientDao();
            oClientDao.insertOrReplaceInTx(prClients);
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

    public boolean eliminarClient(Client prClient){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            ClientDao oClientDao = oRivosDB.getClientDao();
            oClientDao.delete(prClient);
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
            ClientDao oClientDao = oRivosDB.getClientDao();
            oClientDao.deleteAll();
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

    public List<Client> obtenerClient(){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            ClientDao oClientDao = oRivosDB.getClientDao();
            return oClientDao.loadAll();
        }
        catch (Exception error){
            Log.e(TAG, error.getMessage());
            return null;
        }
        finally {
           RivosDB.getInstance().closeDatabase();
        }
    }

    public Client obtenerClient(Long prKey){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            ClientDao oClientDao = oRivosDB.getClientDao();
            return oClientDao.load(prKey);
        }
        catch (Exception error){
            Log.e(TAG, error.getMessage());
            return null;
        }
        finally {
            RivosDB.getInstance().closeDatabase();
        }
    }

    public Client obtenerClientPorClientId(String ClientId){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            ClientDao oClientDao = oRivosDB.getClientDao();
            return oClientDao.queryBuilder().where(ClientDao.Properties.Client_Id.eq(ClientId)).unique();
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