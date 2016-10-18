package com.YozziBeens.rivostaxi.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.respuesta.ResultadoLugaresFavoritos;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudLugaresFavoritos;
import com.facebook.FacebookSdk;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.controlador.Favorite_PlaceController;
import com.YozziBeens.rivostaxi.gcm.Config;
import com.YozziBeens.rivostaxi.modelo.RivosDB;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;


/**
 * Created by danixsanc on 16/01/2016.
 */
public class Splash extends Activity {


    private Gson gson;

    String regId;
    Context context;
    GoogleCloudMessaging gcm;
    public static final String REG_ID = "regId";
    static final String TAG = "Register Activity";

    private ResultadoLugaresFavoritos resultadoLugaresFavoritos;
    private Favorite_PlaceController favorite_placeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        favorite_placeController = new Favorite_PlaceController(this);

        RivosDB.initializeInstance();
        FacebookSdk.sdkInitialize(this);
        this.gson = new Gson();

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1000);

                    regId = registerGCM();

                    Preferencias preferencias = new Preferencias(getApplicationContext());
                    String Client_Id = preferencias.getClient_Id();
                    preferencias.setGcm_Id(regId);

                    if (!Client_Id.equals(null))
                    {
                        SolicitudLugaresFavoritos oData = new SolicitudLugaresFavoritos();
                        oData.setClient_Id(Client_Id);
                        LugaresFavoritosWebService(gson.toJson(oData));
                    }

                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{

                    Intent intent = new Intent(Splash.this, Main.class);
                    startActivity(intent);

                }
            }
        };
        timerThread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public String registerGCM() {

        gcm = GoogleCloudMessaging.getInstance(this);
        regId = getRegistrationId(context);

        if (TextUtils.isEmpty(regId)) {

            registerInBackground();

            Log.d("SolicitudRegistro",
                    "registerGCM - successfully registered with GCM server - regId: "
                            + regId);
        } else {
            System.out.print("RegId already available. RegId: " + regId);
        }
        return regId;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getSharedPreferences(
                Splash.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        return registrationId;
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register(Config.GOOGLE_PROJECT_ID);
                    Log.d("SolicitudRegistro", "registerInBackground - regId: "
                            + regId);
                    msg = "Device registered, registration ID=" + regId;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("SolicitudRegistro", "Error: " + msg);
                }
                Log.d("SolicitudRegistro", "AsyncTask completed: " + msg);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                saveRegisterId(context, regId);
            }
        }.execute(null, null, null);
    }

    private void saveRegisterId(Context context, String regId) {
        final SharedPreferences prefs = getSharedPreferences(
                Splash.class.getSimpleName(), Context.MODE_PRIVATE);
        Log.i(TAG, "Saving regId on app version ");
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.commit();
    }




    private void LugaresFavoritosWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.GetFavoritePlaceWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
            }

            @Override
            public void onTaskDownloadedFinished(HashMap<String, Object> result) {
                try {
                    int statusCode = Integer.parseInt(result.get("StatusCode").toString());
                    if (statusCode == 0) {
                        resultadoLugaresFavoritos = gson.fromJson(result.get("Resultado").toString(), ResultadoLugaresFavoritos.class);
                    }
                } catch (Exception error) {

                }
            }

            @Override
            public void onTaskUpdate(String result) {
            }

            @Override
            public void onTaskComplete(HashMap<String, Object> result) {
                if ((!resultadoLugaresFavoritos.isError()) && resultadoLugaresFavoritos.getData() != null) {
                    favorite_placeController.eliminarTodo();
                    favorite_placeController.guardarOActualizarFavorite_Place(resultadoLugaresFavoritos.getData());
                }
            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
            }
        });
        servicioAsyncService.execute();
    }

}
