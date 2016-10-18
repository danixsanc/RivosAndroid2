package com.YozziBeens.rivostaxi.actividades.Favoritos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.modelo.Ciudad;
import com.YozziBeens.rivostaxi.respuesta.Direcciones;
import com.YozziBeens.rivostaxi.respuesta.ResultadoAgregarLugarFavorito;
import com.YozziBeens.rivostaxi.servicios.AddresLocationAsyncTask;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudAgregarLugarFavorito;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.controlador.Favorite_PlaceController;
import com.YozziBeens.rivostaxi.modelo.Favorite_Place;
import com.google.gson.Gson;
import java.util.HashMap;


public class Agregar_Lugar_Favorito extends AppCompatActivity  implements OnMapReadyCallback ,GoogleMap.OnCameraChangeListener {

    ImageButton btn_add_favorite_place;
    Button btn_save_favorite_place;

    private GoogleMap mMap;
    private AddresLocationAsyncTask addresLocationAsyncTask;
    private String direccion;


    double latitude;
    double longitude;
    String placeId;
    String status;
    String Longitude, Latitude;
    FloatingActionButton fab;

    EditText edtDireccion, edtNombre;

    ImageView img_Marker_center;

    private Gson gson;

    private ResultadoAgregarLugarFavorito resultadoAgregarLugarFavorito;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_lugar_favorito);

        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");

        this.gson = new Gson();
        this.resultadoAgregarLugarFavorito = new ResultadoAgregarLugarFavorito();
        this.edtNombre = (EditText) findViewById(R.id.edtNombre);
        this.edtDireccion = (EditText) findViewById(R.id.edtDireccion);
        this.fab = (FloatingActionButton) findViewById(R.id.fab);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            placeId = bundle.getString("placeId");
            status = bundle.getString("Status");

            if (status.equals("detalle"))
            {
                //getSupportActionBar().setTitle("Detalle Direccion");
                Favorite_PlaceController favorite_placeController = new Favorite_PlaceController(getApplicationContext());
                Favorite_Place favorite_place = favorite_placeController.obtenerFavorite_PlacePorPlaceId(placeId);

                String name = favorite_place.getName();
                String desc = favorite_place.getDescription();
                latitude = Double.valueOf(favorite_place.getLatitude());
                longitude = Double.valueOf(favorite_place.getLongitude());

                edtDireccion.setText(desc);
                edtNombre.setText(name);


                //btn_add_favorite_place.setVisibility(View.GONE);
                //btn_save_favorite_place.setVisibility(View.GONE);
                //img_Marker_center.setVisibility(View.GONE);



            }

        }




        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SolicitudAgregarLugarFavorito oData = new SolicitudAgregarLugarFavorito();
                Preferencias p = new Preferencias(getApplicationContext());
                oData.setClient_Id(p.getClient_Id());
                oData.setDescription(edtDireccion.getText().toString());
                oData.setName(edtNombre.getText().toString());
                oData.setLatitude(Latitude);
                oData.setLongitude(Longitude);
                AgregarLugarFavoritoWebService(gson.toJson(oData));
            }
        });




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    private void AgregarLugarFavoritoWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(Agregar_Lugar_Favorito.this, WebService.SetFavoritePlaceWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
            }

            @Override
            public void onTaskDownloadedFinished(HashMap<String, Object> result) {
                try {
                    int statusCode = Integer.parseInt(result.get("StatusCode").toString());
                    if (statusCode == 0) {
                        resultadoAgregarLugarFavorito = gson.fromJson(result.get("Resultado").toString(), ResultadoAgregarLugarFavorito.class);
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
                String PlaceId = resultadoAgregarLugarFavorito.getData();
                Favorite_PlaceController favorite_placeController  = new Favorite_PlaceController(getApplicationContext());
                Favorite_Place favorite_place = new Favorite_Place(null , PlaceId,
                        edtNombre.getText().toString(), edtDireccion.getText().toString(),
                        Latitude, Longitude);
                favorite_placeController.guardarFavorite_Place(favorite_place);

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);

                finish();
            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
            }
        });
        servicioAsyncService.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

                if (!status.equals("detalle"))
                {
                    edtDireccion.setText(direccion);
                    Latitude = String.valueOf(coordenadas.latitude);
                    Longitude = String.valueOf(coordenadas.longitude);
                }



            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
            }
        });
        addresLocationAsyncTask.execute();
    }


    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            Ciudad ciudad = new Ciudad(null, "Culiacan", 24.736807, -107.468171, 24.848774, -107.366563, 24.790192, -107.401710, 14);
            if(ciudad != null)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ciudad.getLatitud(), ciudad.getLongitud()), 14));
            mMap.getUiSettings().setCompassEnabled(true);
            googleMap.setOnCameraChangeListener(this);
        }
    }
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        LatLng center = mMap.getCameraPosition().target;
        consultarUbicacion(center);
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