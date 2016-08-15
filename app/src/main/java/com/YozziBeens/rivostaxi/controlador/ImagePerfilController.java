package com.YozziBeens.rivostaxi.controlador;

import android.content.Context;
import android.util.Log;

import com.YozziBeens.rivostaxi.modelo.ImagePerfil;
import com.YozziBeens.rivostaxi.modelo.ImagePerfilDao;
import com.YozziBeens.rivostaxi.modelo.DaoSession;
import com.YozziBeens.rivostaxi.modelo.RivosDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by savidsalazar
 */
public class ImagePerfilController {

    private static final String TAG = "ImagePerfil";

    private Context context;

    public ImagePerfilController(Context prContext){
        this.context = prContext;
    }

    public boolean guardarImagePerfil(ImagePerfil prImagePerfil){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            ImagePerfilDao oImagePerfilDao = oRivosDB.getImagePerfilDao();
            oImagePerfilDao.insert(prImagePerfil);
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
    
    public boolean guardarOActualizarImagePerfil(ImagePerfil prImagePerfil){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            ImagePerfilDao oImagePerfilDao = oRivosDB.getImagePerfilDao();
            oImagePerfilDao.insertOrReplace(prImagePerfil);
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

    public boolean guardarOActualizarImagePerfil(ArrayList<ImagePerfil> prImagePerfils){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            ImagePerfilDao oImagePerfilDao = oRivosDB.getImagePerfilDao();
            oImagePerfilDao.insertOrReplaceInTx(prImagePerfils);
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

    public boolean eliminarImagePerfil(ImagePerfil prImagePerfil){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            ImagePerfilDao oImagePerfilDao = oRivosDB.getImagePerfilDao();
            oImagePerfilDao.delete(prImagePerfil);
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
            ImagePerfilDao oImagePerfilDao = oRivosDB.getImagePerfilDao();
            oImagePerfilDao.deleteAll();
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

    public List<ImagePerfil> obtenerImagePerfil(){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            ImagePerfilDao oImagePerfilDao = oRivosDB.getImagePerfilDao();
            return oImagePerfilDao.loadAll();
        }
        catch (Exception error){
            Log.e(TAG, error.getMessage());
            return null;
        }
        finally {
           RivosDB.getInstance().closeDatabase();
        }
    }

    public ImagePerfil obtenerImagePerfil(Long prKey){
        try {
            DaoSession oRivosDB = RivosDB.getInstance().openDatabase(context);
            ImagePerfilDao oImagePerfilDao = oRivosDB.getImagePerfilDao();
            return oImagePerfilDao.load(prKey);
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