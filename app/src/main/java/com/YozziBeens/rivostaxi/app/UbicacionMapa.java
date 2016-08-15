package com.YozziBeens.rivostaxi.app;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.controlador.CiudadController;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.modelo.Ciudad;
import com.YozziBeens.rivostaxi.respuesta.Direcciones;
import com.YozziBeens.rivostaxi.servicios.AddresLocationAsyncTask;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public class UbicacionMapa extends AppCompatActivity implements OnMapReadyCallback {

    private final static String TAG = "UBICACION_CON_MAPA";
    //private Tracker mTracker;


    private GoogleMap mMap;
    private Preferencias preferencias;
    private CiudadController ciudarController;
    private AddresLocationAsyncTask addresLocationAsyncTask;
    private String direccion;
    private int seleccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ubicacion_mapa);

        //Google Analytics
        //AnalyticsEnrutate application = (AnalyticsEnrutate) getApplication();
        //mTracker = application.getDefaultTracker();

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        this.preferencias = new Preferencias(this);
        this.ciudarController = new CiudadController(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle parametros = getIntent().getExtras();
        if (parametros != null) {
            seleccion = parametros.getInt("opcionSeleccionada");
            if(seleccion == 0)
                getSupportActionBar().setTitle("Origen");
            else
                getSupportActionBar().setTitle("Destino");
        }

        /*((Button) findViewById(R.id.btnCancelar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });*/


        ((FloatingActionButton) findViewById(R.id.btnAceptar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMap != null) {
                    LatLng center = mMap.getCameraPosition().target;
                    consultarUbicacion(center);
                }
            }
        });
    }

    private void consultarUbicacion(final LatLng coordenadas){
        addresLocationAsyncTask = new AddresLocationAsyncTask(this, coordenadas.latitude ,coordenadas.longitude, 1);
        addresLocationAsyncTask.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
            }

            @Override
            public void onTaskDownloadedFinished(HashMap<String, Object> result) {

            }

            @Override
            public void onTaskUpdate(String result) {

            }

            @Override
            public void onTaskComplete(HashMap<String, Object> result) {
                if(result != null)
                {
                    if(Boolean.parseBoolean(result.get("esValido").toString())){
                        Direcciones oDirecciones = (Direcciones) result.get("Resultado");
                        if(oDirecciones.getAddresses() != null && oDirecciones.getAddresses().size() > 0) {
                            Address oDireccion = oDirecciones.getAddresses().get(0);
                            direccion = obtenerDireccionLimpia(oDireccion);
                        }
                    }
                }
                Bundle conData = new Bundle();
                conData.putString("direccion", direccion);
                conData.putDouble("latitud", coordenadas.latitude);
                conData.putDouble("longitud", coordenadas.longitude);
                conData.putInt("opcionSeleccionada", seleccion);
                Intent intent = new Intent();
                intent.putExtras(conData);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
            }
        });
        addresLocationAsyncTask.execute();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            Ciudad ciudad = new Ciudad(null, "Culiacan", 24.736807, -107.468171, 24.848774, -107.366563, 24.790192, -107.401710, 14);
            if(ciudad != null)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ciudad.getLatitud(), ciudad.getLongitud()), 14));
            mMap.getUiSettings().setCompassEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String obtenerDireccionLimpia(Address address)
    {
        String Thoroughfare = address.getThoroughfare() == null ? "" : address.getThoroughfare();
        String FeatureName = address.getFeatureName() == null ? "" : address.getFeatureName();
        //String Locality = address.getLocality() == null ? "" : address.getLocality();
        //String AdminArea = address.getAdminArea() == null ? "" : address.getAdminArea();
        return Thoroughfare + " " + FeatureName; // + " " + Locality  + " " + AdminArea;
    }

    @Override
    public void onResume(){
        super.onResume();
        //mTracker.setScreenName(TAG);
        //mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

}
