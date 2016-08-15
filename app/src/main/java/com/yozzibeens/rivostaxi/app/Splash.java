package com.YozziBeens.rivostaxi.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import com.YozziBeens.rivostaxi.actividades.Proceso.Nav_Proceso;
import com.YozziBeens.rivostaxi.adaptadores.PendingHistory;
import com.YozziBeens.rivostaxi.controlador.ClientController;
import com.YozziBeens.rivostaxi.controlador.HistorialPendienteController;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.modelo.HistorialPendiente;
import com.YozziBeens.rivostaxi.respuesta.ResultadoHistorialCliente;
import com.YozziBeens.rivostaxi.respuesta.ResultadoHistorialPendienteCliente;
import com.YozziBeens.rivostaxi.respuesta.ResultadoLugaresFavoritos;
import com.YozziBeens.rivostaxi.respuesta.ResultadoRegistrarGCM;
import com.YozziBeens.rivostaxi.respuesta.ResultadoTaxistasFavoritos;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudHistorialCliente;
import com.YozziBeens.rivostaxi.solicitud.SolicitudLugaresFavoritos;
import com.YozziBeens.rivostaxi.solicitud.SolicitudRegistrarGCM;
import com.YozziBeens.rivostaxi.solicitud.SolicitudTaxistasFavoritos;
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
import java.util.List;


/**
 * Created by danixsanc on 16/01/2016.
 */
public class Splash extends Activity {

    ServicioAsyncService servicioAsyncService;
    private ClientController clientController;
    private Gson gson;

    String regId;
    Context context;
    GoogleCloudMessaging gcm;
    public static final String REG_ID = "regId";
    static final String TAG = "Register Activity";

    private ResultadoHistorialCliente resultadoHistorialCliente;
    private ResultadoTaxistasFavoritos resultadoTaxistasFavoritos;
    private ResultadoLugaresFavoritos resultadoLugaresFavoritos;
    private ResultadoRegistrarGCM resultadoRegistrarGCM;

    private HistorialController historialController;
    private Favorite_CabbieController favorite_cabbieController;
    private Favorite_PlaceController favorite_placeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        historialController = new HistorialController(this);
        favorite_cabbieController = new Favorite_CabbieController(this);
        favorite_placeController = new Favorite_PlaceController(this);

        RivosDB.initializeInstance();
        FacebookSdk.sdkInitialize(this);
        this.gson = new Gson();

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1000);

                    regId = registerGCM();
                    Log.d("SolicitudRegistro", "GCM RegId: " + regId);




                    Preferencias preferencias = new Preferencias(getApplicationContext());
                    String Client_Id = preferencias.getClient_Id();

                    if (!Client_Id.equals(null))
                    {
                        SolicitudRegistrarGCM oData0 = new SolicitudRegistrarGCM();
                        oData0.setClient_Id(Client_Id);
                        oData0.setGcmId(regId);
                        RegisterGCMWebService(gson.toJson(oData0));

                        SolicitudHistorialCliente oData1 = new SolicitudHistorialCliente();
                        oData1.setClient_Id(Client_Id);
                        HistorialClienteWebService(gson.toJson(oData1));

                        SolicitudTaxistasFavoritos oData2 = new SolicitudTaxistasFavoritos();
                        oData2.setClient_Id(Client_Id);
                        TaxistasFavoritosWebService(gson.toJson(oData2));

                        SolicitudLugaresFavoritos oData3 = new SolicitudLugaresFavoritos();
                        oData3.setClient_Id(Client_Id);
                        LugaresFavoritosWebService(gson.toJson(oData3));
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




    private void RegisterGCMWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.RegisterGcmIdWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {

            }

            @Override
            public void onTaskDownloadedFinished(HashMap<String, Object> result) {
                try {
                    int statusCode = Integer.parseInt(result.get("StatusCode").toString());
                    if (statusCode == 0) {
                        resultadoRegistrarGCM = gson.fromJson(result.get("Resultado").toString(), ResultadoRegistrarGCM.class);
                    }
                } catch (Exception error) {
                }
            }

            @Override
            public void onTaskUpdate(String result) {
            }

            @Override
            public void onTaskComplete(HashMap<String, Object> result) {

            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {

            }
        });
        servicioAsyncService.execute();
    }


    private void HistorialClienteWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.GetClientHistoryWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {

            }

            @Override
            public void onTaskDownloadedFinished(HashMap<String, Object> result) {
                try {
                    int statusCode = Integer.parseInt(result.get("StatusCode").toString());
                    if (statusCode == 0) {
                        resultadoHistorialCliente = gson.fromJson(result.get("Resultado").toString(), ResultadoHistorialCliente.class);
                        if ((!resultadoHistorialCliente.isError()) && resultadoHistorialCliente.getData() != null) {
                            historialController.eliminarTodo();
                            historialController.guardarOActualizarHistorial(resultadoHistorialCliente.getData());
                        }
                    }
                }
                catch (Exception error) {
                }
            }

            @Override
            public void onTaskUpdate(String result) {}

            @Override
            public void onTaskComplete(HashMap<String, Object> result) {

            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {

            }
        });
        servicioAsyncService.execute();
    }

    private void TaxistasFavoritosWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.GetFavoriteCabbieWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
            }

            @Override
            public void onTaskDownloadedFinished(HashMap<String, Object> result) {
                try {
                    int statusCode = Integer.parseInt(result.get("StatusCode").toString());
                    if (statusCode == 0) {
                        resultadoTaxistasFavoritos = gson.fromJson(result.get("Resultado").toString(), ResultadoTaxistasFavoritos.class);
                        if ((!resultadoTaxistasFavoritos.isError()) && resultadoTaxistasFavoritos.getData() != null) {
                            favorite_cabbieController.eliminarTodo();
                            favorite_cabbieController.guardarOActualizarFavorite_Cabbie(resultadoTaxistasFavoritos.getData());
                        }
                    }
                }
                catch (Exception error) {

                }
            }

            @Override
            public void onTaskUpdate(String result) {
            }

            @Override
            public void onTaskComplete(HashMap<String, Object> result) {
            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
            }
        });
        servicioAsyncService.execute();
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
                        if ((!resultadoLugaresFavoritos.isError()) && resultadoLugaresFavoritos.getData() != null) {
                            favorite_placeController.eliminarTodo();
                            favorite_placeController.guardarOActualizarFavorite_Place(resultadoLugaresFavoritos.getData());
                        }
                    }
                } catch (Exception error) {

                }
            }

            @Override
            public void onTaskUpdate(String result) {
            }

            @Override
            public void onTaskComplete(HashMap<String, Object> result) {
            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
            }
        });
        servicioAsyncService.execute();
    }

}
