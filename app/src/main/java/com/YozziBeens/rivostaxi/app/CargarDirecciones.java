package com.YozziBeens.rivostaxi.app;


import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.location.Address;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.adaptadores.DireccionesAdapter;
import com.YozziBeens.rivostaxi.adaptadores.LugarAdapter;
import com.YozziBeens.rivostaxi.controlador.CiudadController;
import com.YozziBeens.rivostaxi.controlador.Favorite_PlaceController;
import com.YozziBeens.rivostaxi.controlador.LugarController;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.LocationTaskListener;
import com.YozziBeens.rivostaxi.modelo.Ciudad;
import com.YozziBeens.rivostaxi.modelo.Favorite_Place;
import com.YozziBeens.rivostaxi.modelo.Lugar;
import com.YozziBeens.rivostaxi.respuesta.Direcciones;
import com.YozziBeens.rivostaxi.servicios.SearchAddressAsyncTask;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.YozziBeens.rivostaxi.utilerias.Utilerias;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by danixsanc on 02/04/2016.
 */



public class CargarDirecciones extends LocationActivity {

    private final static String TAG = "MAIN";
    //private Tracker mTracker;

    private FloatingActionButton btnConsultarRutas;
    private RelativeLayout rlCargando;
    private ListView lsvLugares, lsvDirecciones;
    private ImageButton btnCancelarOrigen, btnCancelarDestino;
    private CardView crdDireccicones;
    private LugarController lugarController;
    private Favorite_PlaceController favorite_placeController;
    private CiudadController ciudadController;
    private Ciudad ciudad;
    private Location userLocation;
    private LatLng mOrigen, mDestino;
    private int opcionSeleccionada = 0;
    private EditText edtOrigen, edtDestino;
    private Preferencias preferencias;
    private TextView txtMensaje;

    private String solicitud;
    private SearchAddressAsyncTask searchAddressAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cargar_direcciones);

        //Google Analytics
        /*AnalyticsEnrutate application = (AnalyticsEnrutate) getApplication();
        mTracker = application.getDefaultTracker();*/

        this.edtOrigen = (EditText) findViewById(R.id.edtOrigen);
        this.edtDestino = (EditText) findViewById(R.id.edtDestino);
        this.lsvLugares = (ListView) findViewById(R.id.lsvLugares);
        this.lsvDirecciones = (ListView) findViewById(R.id.lsvDirecciones);
        this.crdDireccicones = (CardView) findViewById(R.id.crdDireccicones);
        this.btnCancelarOrigen = (ImageButton) findViewById(R.id.btnCancelarOrigen);
        this.btnCancelarDestino = (ImageButton) findViewById(R.id.btnCancelarDestino);
        this.rlCargando = (RelativeLayout) findViewById(R.id.rlCargando);
        this.txtMensaje = (TextView) findViewById(R.id.txtMensaje);
        this.lugarController = new LugarController(this);
        this.favorite_placeController = new Favorite_PlaceController(this);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        btnConsultarRutas = (FloatingActionButton) findViewById(R.id.fab);
        btnConsultarRutas.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((mOrigen != null) && (mDestino != null))
                    {
                        Bundle conData = new Bundle();
                        conData.putDouble("latOrigen", mOrigen.latitude);
                        conData.putDouble("longOrigen", mOrigen.longitude);
                        conData.putDouble("latDestino", mDestino.latitude);
                        conData.putDouble("longDestino", mDestino.longitude);
                        conData.putString("dirOrigen", edtOrigen.getText().toString());
                        conData.putString("dirDestino", edtDestino.getText().toString());
                        //conData.putInt("opcionSeleccionada", seleccion);
                        Intent intent = new Intent();
                        intent.putExtras(conData);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else {
                        if ((mOrigen != null) && (edtOrigen.getText().toString().length() > 0))
                        {
                            new SweetAlertDialog(CargarDirecciones.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Parece que no has seleccionado un destino!")
                                    .setConfirmText("Entendido")
                                    .show();
                        }
                        else if ((mDestino != null) && (edtDestino.getText().toString().length() > 0))
                        {
                            new SweetAlertDialog(CargarDirecciones.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Parece que no has seleccionado un origen!")
                                    .setConfirmText("Entendido")
                                    .show();
                        }
                        else{
                            new SweetAlertDialog(CargarDirecciones.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Selecciona tu origen y el destino!")
                                    .setConfirmText("Entendido")
                                    .show();
                        }

                    }
                }
            }
        );

        //Cargar carrito
/*        ImageView img = (ImageView) findViewById(R.id.imgCamioncito);
        img.setBackgroundResource(R.drawable.ic_access_time_black_24dp);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();*/

        //activarTiempoDeExpiracion = true;
        activarMensajeDeUbicacion = true;

        //activarUbicacionAlIniciar = false;

/*
        this.preferencias = new Preferencias(this);
        ciudadController = new CiudadController(this);
        ciudad = ciudadController.obtenerCiudad(preferencias.getCiudad());
*/
        ciudad = new Ciudad(null, "Culiacan", 24.736807, -107.468171, 24.848774, -107.366563, 24.790192, -107.401710, 14);



        LinearLayout btnMiUbicacion = (LinearLayout) findViewById(R.id.btnMiUbicacion);
        btnMiUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLocationEnabled()) {
                    if (userLocation != null) {
                        if (opcionSeleccionada == 0) {
                            edtOrigen.setEnabled(false);
                            btnCancelarOrigen.setVisibility(View.VISIBLE);
                            edtOrigen.setText(getString(R.string.strMiUbicacion));
                            mOrigen = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                            edtDestino.requestFocus();
                        } else if (opcionSeleccionada == 1) {
                            edtDestino.setEnabled(false);
                            btnCancelarDestino.setVisibility(View.VISIBLE);
                            edtDestino.setText(getString(R.string.strMiUbicacion));
                            mDestino = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                        }
                    } else {
                        int permissionLocationCheck = ContextCompat.checkSelfPermission(CargarDirecciones.this,
                                Manifest.permission.ACCESS_FINE_LOCATION);
                        int permisionCoarseLocation = ContextCompat.checkSelfPermission(CargarDirecciones.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION);

                        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                            consultarUbicacion();
                        else if(permissionLocationCheck == 0 && permisionCoarseLocation == 0)
                            consultarUbicacion();
                        else
                            Toast.makeText(CargarDirecciones.this, getString(R.string.str_permiso_no_autorizado), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        this.edtOrigen.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    btnCancelarOrigen.setVisibility(View.VISIBLE);
                    if(!edtDestino.getText().toString().equals(getString(R.string.strMiUbicacion)))
                        btnCancelarDestino.setVisibility(View.GONE);
                    opcionSeleccionada = 0;
                }
            }
        });

        this.edtOrigen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCancelarOrigen.setVisibility(View.VISIBLE);
                if(!edtDestino.getText().toString().equals(getString(R.string.strMiUbicacion)))
                    btnCancelarDestino.setVisibility(View.GONE);
                opcionSeleccionada = 0;
            }
        });

        this.edtDestino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtOrigen.getText().toString().equals(getString(R.string.strMiUbicacion)))
                    btnCancelarOrigen.setVisibility(View.GONE);
                btnCancelarDestino.setVisibility(View.VISIBLE);
                opcionSeleccionada = 1;
            }
        });

        this.edtDestino.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if(!edtOrigen.getText().toString().equals(getString(R.string.strMiUbicacion)))
                        btnCancelarOrigen.setVisibility(View.GONE);
                    btnCancelarDestino.setVisibility(View.VISIBLE);
                    opcionSeleccionada = 1;
                }
            }
        });

        this.edtOrigen.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    buscarCoordenadasPorDireccion(edtOrigen.getText().toString());
                    Utilerias.esconderTeclado(CargarDirecciones.this);
                    return true;
                }
                return false;
            }
        });

        this.edtDestino.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    buscarCoordenadasPorDireccion(edtDestino.getText().toString());
                    Utilerias.esconderTeclado(CargarDirecciones.this);
                    return true;
                }
                return false;
            }
        });

        this.edtOrigen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 5)
                    buscarCoordenadasPorDireccion(s.toString());
                else {
                    cargarDirecciones(new ArrayList<Address>());
                    crdDireccicones.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        this.edtDestino.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 5)
                    buscarCoordenadasPorDireccion(s.toString());
                else {
                    cargarDirecciones(new ArrayList<Address>());
                    crdDireccicones.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        this.lsvDirecciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Address oAddress = (Address) parent.getItemAtPosition(position);
                if (opcionSeleccionada == 0) {
                    edtOrigen.setText(obtenerDireccionLimpia(oAddress));
                    mOrigen = new LatLng(oAddress.getLatitude(), oAddress.getLongitude());
                    edtDestino.requestFocus();
                } else if (opcionSeleccionada == 1) {
                    edtDestino.setText(obtenerDireccionLimpia(oAddress));
                    mDestino = new LatLng(oAddress.getLatitude(), oAddress.getLongitude());
                    //btnConsultarRutas.requestFocus();
                }
                crdDireccicones.setVisibility(View.GONE);

            }
        });

        this.lsvLugares.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Favorite_Place oLugar = (Favorite_Place) parent.getItemAtPosition(position);
                if (opcionSeleccionada == 0) {
                    edtOrigen.setText(oLugar.getDesc());
                    mOrigen = new LatLng(Double.valueOf(oLugar.getLatitude()), Double.valueOf(oLugar.getLongitude()));
                    edtDestino.requestFocus();
                } else if (opcionSeleccionada == 1) {
                    edtDestino.setText(oLugar.getDesc());
                    mDestino = new LatLng(Double.valueOf(oLugar.getLatitude()), Double.valueOf(oLugar.getLongitude()));
                    //btnConsultarRutas.requestFocus();
                }
            }
        });

        this.btnCancelarOrigen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtOrigen.setEnabled(true);
                edtOrigen.setText("");
                mOrigen = null;
                edtOrigen.requestFocus();
            }
        });

        this.btnCancelarDestino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtDestino.setEnabled(true);
                edtDestino.setText("");
                mDestino = null;
                edtDestino.requestFocus();
            }
        });

        ((LinearLayout) findViewById(R.id.btnUbicacionMapa)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iUbicacionMapa = new Intent(CargarDirecciones.this, UbicacionMapa.class);
                iUbicacionMapa.putExtra("opcionSeleccionada", opcionSeleccionada);
                startActivityForResult(iUbicacionMapa, 1);
            }
        });

        ((ImageButton) findViewById(R.id.btnCambiarOrden)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng tempOrigen = null, tempDestino = null;
                String nombreOrigen = "", nombreDestino = "";
                if (mOrigen != null && edtOrigen.getText().toString().length() > 0) {
                    tempDestino = new LatLng(mOrigen.latitude, mOrigen.longitude);
                    nombreOrigen = edtOrigen.getText().toString();
                    edtOrigen.setText("");
                }
                if (mDestino != null && edtDestino.getText().toString().length() > 0) {
                    tempOrigen = new LatLng(mDestino.latitude, mDestino.longitude);
                    nombreDestino = edtDestino.getText().toString();
                    edtDestino.setText("");

                }
                if (tempOrigen != null) {
                    mOrigen = new LatLng(tempOrigen.latitude, tempOrigen.longitude);
                    edtOrigen.setText(nombreDestino);
                }
                if (tempDestino != null) {
                    mDestino = new LatLng(tempDestino.latitude, tempDestino.longitude);
                    edtDestino.setText(nombreOrigen);
                }

            }
        });

        Bundle parametros = getIntent().getExtras();
        if (parametros != null) {
            Double latitudOrigen = parametros.getDouble("latitudOrigen");
            Double longitudOrigen = parametros.getDouble("longitudOrigen");
            if (latitudOrigen != 0 && longitudOrigen != 0) {
                edtOrigen.setEnabled(false);
                btnCancelarOrigen.setVisibility(View.VISIBLE);
                mOrigen = new LatLng(latitudOrigen, longitudOrigen);
                edtOrigen.setText(R.string.strMiUbicacion);
                edtOrigen.clearFocus();
                edtDestino.requestFocus();
            }
            Double latitudDestino = parametros.getDouble("latitudDestino");
            Double longitudDestino = parametros.getDouble("longitudDestino");
            String nombreLugar = parametros.getString("nombreLugar");
            if (latitudDestino != 0 && longitudDestino != 0) {
                edtDestino.setEnabled(false);
                btnCancelarDestino.setVisibility(View.VISIBLE);
                mDestino = new LatLng(latitudDestino, longitudDestino);
                edtDestino.setText(nombreLugar);
            }
        }

        cargarLugares();
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        userLocation = location;
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

    private void cargarLugares() {
        LugarAdapter lugarAdapter = new LugarAdapter(this, R.layout.item_lugar, favorite_placeController.obtenerFavorite_Place());
        lsvLugares.setAdapter(lugarAdapter);
        setListViewHeightBasedOnChildren(lsvLugares);
    }

    private void cargarDirecciones(List<Address> direcciones) {
        DireccionesAdapter direccionesAdapter = new DireccionesAdapter(this, R.layout.item_direcciones, direcciones);
        lsvDirecciones.setAdapter(direccionesAdapter);
        setListViewHeightBasedOnChildren(lsvDirecciones);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight() + 106;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /* Called when the second activity's finished */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle res = data.getExtras();
            if (res.getInt("opcionSeleccionada") == 0) {
                edtOrigen.setText(res.getString("direccion"));
                mOrigen = new LatLng(res.getDouble("latitud"), res.getDouble("longitud"));
                edtDestino.requestFocus();
            } else {
                edtDestino.setText(res.getString("direccion"));
                mDestino = new LatLng(res.getDouble("latitud"), res.getDouble("longitud"));
                //btnConsultarRutas.requestFocus();
            }
            String result = res.getString("param_result");
            Log.d("FIRST", "result:" + result);
        }
    }

    private void buscarCoordenadasPorDireccion(String prDireccion) {
        if (searchAddressAsyncTask != null) {
            searchAddressAsyncTask.cancel(true);
            searchAddressAsyncTask = null;
        }
        searchAddressAsyncTask = new SearchAddressAsyncTask(this, prDireccion, 10, ciudad);
        searchAddressAsyncTask.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
                crdDireccicones.setVisibility(View.VISIBLE);
                if (lsvDirecciones.getAdapter().isEmpty())
                    ((LinearLayout) findViewById(R.id.llBuscandoDirecciones)).setVisibility(View.VISIBLE);
            }

            @Override
            public void onTaskDownloadedFinished(HashMap<String, Object> result) {

            }

            @Override
            public void onTaskUpdate(String result) {

            }

            @Override
            public void onTaskComplete(HashMap<String, Object> result) {
                ((LinearLayout) findViewById(R.id.llBuscandoDirecciones)).setVisibility(View.GONE);
                if (result != null) {
                    if (Boolean.parseBoolean(result.get("esValido").toString())) {
                        Direcciones oDirecciones = (Direcciones) result.get("Resultado");
                        if (oDirecciones.getAddresses() != null && oDirecciones.getAddresses().size() > 0) {
                            cargarDirecciones(oDirecciones.getAddresses());
                        }
                    }
                }

            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
            }
        });
        searchAddressAsyncTask.execute();
    }

    private String obtenerDireccionLimpia(Address address) {
        String Thoroughfare = address.getThoroughfare() == null ? "" : address.getThoroughfare();
        String FeatureName = address.getFeatureName() == null ? "" : address.getFeatureName();
        //String Locality = address.getLocality() == null ? "" : address.getLocality();
        //String AdminArea = address.getAdminArea() == null ? "" : address.getAdminArea();
        return Thoroughfare + " " + FeatureName;//+ " " + Locality + " " + AdminArea;
    }

    public void consultarUbicacion() {
        this.stopPeriodicUpdates();
        this.startPeriodicUpdates();
        this.setLocationListener(new LocationTaskListener() {
            @Override
            public void onTaskStart() {
                txtMensaje.setText("Consultando ubicacion");
                rlCargando.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTaskComplete(Location result) {
                if (result == null)
                    Toast.makeText(CargarDirecciones.this, "Ubicacion no disponible", Toast.LENGTH_LONG).show();
                else {
                    if (opcionSeleccionada == 0) {
                        btnCancelarOrigen.setVisibility(View.VISIBLE);
                        edtOrigen.setEnabled(false);
                        edtOrigen.setText(getString(R.string.strMiUbicacion));
                        mOrigen = new LatLng(result.getLatitude(), result.getLongitude());
                    } else if (opcionSeleccionada == 1) {
                        btnCancelarDestino.setVisibility(View.VISIBLE);
                        edtOrigen.setEnabled(false);
                        edtDestino.setText(getString(R.string.strMiUbicacion));
                        mDestino = new LatLng(result.getLatitude(), result.getLongitude());
                    }
                }

                txtMensaje.setText("");
                rlCargando.setVisibility(View.GONE);
                stopPeriodicUpdates();
            }

            @Override
            public void onTaskCancelled() {
                stopPeriodicUpdates();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (searchAddressAsyncTask != null) {
            searchAddressAsyncTask.cancel(true);
            searchAddressAsyncTask = null;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        //mTracker.setScreenName(TAG);
        //mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
