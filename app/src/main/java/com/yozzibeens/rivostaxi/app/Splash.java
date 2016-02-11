package com.YozziBeens.rivostaxi.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.YozziBeens.rivostaxi.controlador.ClientController;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.modelo.Client;
import com.YozziBeens.rivostaxi.respuesta.RespuestaCadena;
import com.facebook.FacebookSdk;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.controlador.Favorite_CabbieController;
import com.YozziBeens.rivostaxi.controlador.Favorite_PlaceController;
import com.YozziBeens.rivostaxi.controlador.HistorialController;
import com.YozziBeens.rivostaxi.gcm.Config;
import com.YozziBeens.rivostaxi.modelo.Favorite_Cabbie;
import com.YozziBeens.rivostaxi.modelo.Favorite_Place;
import com.YozziBeens.rivostaxi.modelo.Historial;
import com.YozziBeens.rivostaxi.modelo.RivosDB;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.YozziBeens.rivostaxi.utilerias.Servicio;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;


/**
 * Created by danixsanc on 16/01/2016.
 */
public class Splash extends Activity {

    ServicioAsyncService servicioAsyncService;
    private RespuestaCadena respuestaCadena;
    private ClientController clientController;
    private Gson gson;

    String regId;
    Context context;
    GoogleCloudMessaging gcm;
    public static final String REG_ID = "regId";
    static final String TAG = "Register Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        RivosDB.initializeInstance();
        FacebookSdk.sdkInitialize(this);
        this.gson = new Gson();

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1000);




                    Preferencias preferencias = new Preferencias(getApplicationContext());
                    String Client_Id = preferencias.getClient_Id();
                    Servicio servicio = new Servicio();
                    int val = 0;
                    final JSONObject json = servicio.getClientHistory(Client_Id);
                    try {
                        if (json.getString("Success") != null) {
                            String res = json.getString("Success");
                            if (Integer.parseInt(res) == 1)
                            {
                                val = json.getInt("num");

                                HistorialController historialController2 = new HistorialController(getApplicationContext());
                                historialController2.eliminarTodo();
                                for (int i = 0; i < val; i++)
                                {
                                    JSONObject json_user = json.getJSONObject("Request"+(i+1));
                                    Historial historial = new Historial(null, json_user.getString("Request_Id"), json_user.getString("Latitude_In"),
                                            json_user.getString("Latitude_Fn"),json_user.getString("Date"),json_user.getString("Name"));

                                    HistorialController historialController = new HistorialController(getApplicationContext());
                                    historialController.guardarHistorial(historial);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }






                    final JSONObject json2 = servicio.getFavoriteCabbie(Client_Id);
                    try {
                        if (json2.getString("Success") != null) {
                            String res = json2.getString("Success");
                            if (Integer.parseInt(res) == 1)
                            {

                                int val2 = json2.getInt("num");
                                Favorite_CabbieController favorite_cabbieController = new Favorite_CabbieController(getApplicationContext());
                                favorite_cabbieController.eliminarTodo();
                                for (int i = 0; i < val2; i++)
                                {

                                    JSONObject json_user2 = json2.getJSONObject("Cabbie_Id"+(i+1));

                                    Favorite_Cabbie favorite_cabbie = new Favorite_Cabbie(null, json_user2.getString("Cabbie_Id"),
                                            json_user2.getString("Name"),json_user2.getString("Company"));

                                    favorite_cabbieController = new Favorite_CabbieController(getApplicationContext());
                                    favorite_cabbieController.guardarFavorite_Cabbie(favorite_cabbie);

                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                    final JSONObject json3 = servicio.getFavoritePlace(Client_Id);
                    try {
                        if (json3.getString("Success") != null) {
                            String res = json3.getString("Success");
                            if (Integer.parseInt(res) == 1)
                            {

                                int val3 = json3.getInt("num");
                                Favorite_PlaceController favorite_placeController = new Favorite_PlaceController(getApplicationContext());
                                favorite_placeController.eliminarTodo();
                                for (int i = 0; i < val3; i++)
                                {

                                    JSONObject json_user3 = json3.getJSONObject("Place"+(i+1));

                                    Favorite_Place favorite_place = new Favorite_Place(null, json_user3.getString("Place_Favorite_Id"),
                                            json_user3.getString("Place_Name"),json_user3.getString("Desc_Place"),
                                            json_user3.getString("Latitude"), json_user3.getString("Longitude"));

                                    favorite_placeController = new Favorite_PlaceController(getApplicationContext());
                                    favorite_placeController.guardarFavorite_Place(favorite_place);

                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    regId = registerGCM();
                    Log.d("RegistroSolicitud", "GCM RegId: " + regId);
                    servicio.Register_GcmId(regId, Client_Id);


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



    //gcm

    public String registerGCM() {

        gcm = GoogleCloudMessaging.getInstance(this);
        regId = getRegistrationId(context);

        if (TextUtils.isEmpty(regId)) {

            registerInBackground();

            Log.d("RegistroSolicitud",
                    "registerGCM - successfully registered with GCM server - regId: "
                            + regId);
        } else {
            //Toast.makeText(getApplicationContext(), "RegId already available. RegId: " + regId, Toast.LENGTH_LONG).show();
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
                    Log.d("RegistroSolicitud", "registerInBackground - regId: "
                            + regId);
                    msg = "Device registered, registration ID=" + regId;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegistroSolicitud", "Error: " + msg);
                }
                Log.d("RegistroSolicitud", "AsyncTask completed: " + msg);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                /*Toast.makeText(getApplicationContext(),
                        "Registered with GCM Server." + msg, Toast.LENGTH_LONG)
                        .show();*/
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

}
