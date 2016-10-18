package com.YozziBeens.rivostaxi.app;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.modelo.Ciudad;
import com.YozziBeens.rivostaxi.modelo.RivosDB;
import com.YozziBeens.rivostaxi.modelosApp.Solicitud;
import com.YozziBeens.rivostaxi.respuesta.ResultadoObtenerPrecio;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudObtenerPrecio;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.actividades.Solicitar.Detalles_Solicitud;
import com.YozziBeens.rivostaxi.fragmentos.DrawerMenu;
import com.YozziBeens.rivostaxi.tutorial.TutorialActivity;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;

import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Main extends AppCompatActivity implements OnMapReadyCallback {

    private DrawerMenu mDrawerMenu;
    private GoogleMap mapa;
    private FloatingActionButton btnpedirtaxi;
    private String dirOrigen;
    private String dirDestino;
    private SweetAlertDialog pDialog;
    private LinearLayout layout_origen_destino;
    private Gson gson;
    private ResultadoObtenerPrecio resultadoObtenerPrecio;
    private Preferencias preferencias;
    private double latOrigen;
    private double longOrigen;
    private double latDestino;
    private double longDestino;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RivosDB.initializeInstance();
        setContentView(R.layout.main);
        this.gson = new Gson();


        this.preferencias = new Preferencias(getApplicationContext());
        boolean check = preferencias.getSesion();
        this.resultadoObtenerPrecio = new ResultadoObtenerPrecio();

        if (check) {
            Intent intent = new Intent(Main.this, Login.class);
            startActivity(intent);
            finish();
        } else {

            boolean checkTutorial = preferencias.getTutorial();

            /*if (checkTutorial) {
                Intent intent2 = new Intent(Main.this, TutorialActivity.class);
                startActivity(intent2);
            }*/

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            mDrawerMenu = (DrawerMenu) getSupportFragmentManager().findFragmentById(R.id.left_drawer);
            mDrawerMenu.setUp(R.id.left_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar, getSupportActionBar(), this);

            layout_origen_destino = (LinearLayout) findViewById(R.id.layout_origen_destino);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

            String locationProvider = LocationManager.NETWORK_PROVIDER;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location lastlocation = locationManager.getLastKnownLocation(locationProvider);

            latitude = lastlocation.getLatitude();
            longitude = lastlocation.getLongitude();


            btnpedirtaxi = (FloatingActionButton) findViewById(R.id.btn_ver_taxistas);
            btnpedirtaxi.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    if ((latOrigen == 0.0) && (latDestino == 0.0) && (longOrigen == 0.0) && (longDestino == 0.0)) {
                        new SweetAlertDialog(Main.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Primero debes seleccionar un destino!")
                                .setConfirmText("Entendido")
                                .show();

                    } else {
                        if (((Math.abs(latOrigen - latDestino)) < 0.0025) && ((Math.abs(longOrigen - longDestino)) < 0.0025)) {
                            new SweetAlertDialog(Main.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("No puedes seleccionar un destino tan corto")
                                    .setConfirmText("Entendido")
                                    .show();
                        } else {
                            LatLng l1 = new LatLng(latOrigen, longOrigen);
                            LatLng l2 = new LatLng(latDestino, longDestino);

                            double distance = SphericalUtil.computeDistanceBetween(l1, l2);
                            double distancef = (formatNumber(distance));

                            SolicitudObtenerPrecio oData = new SolicitudObtenerPrecio();
                            oData.setLatitude_In(String.valueOf(latOrigen));
                            oData.setLongitude_In(String.valueOf(longOrigen));
                            oData.setLatitude_Fn(String.valueOf(latDestino));
                            oData.setLongitude_Fn(String.valueOf(longDestino));
                            oData.setDistance(String.valueOf(distancef));
                            ObtenerPrecioWebService(gson.toJson(oData));

                        }
                    }
                }
            });

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                String c = bundle.getString("Notif");

                if (c.equals("C")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Main.this, R.style.AppCompatAlertDialogStyle);
                    dialog.setMessage("Tu taxista llego por ti.");
                    dialog.setCancelable(true);
                    dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
            }

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Main.this, CargarDirecciones.class);
                    startActivityForResult(intent, 218);
                }
            });

        }
    }


    @Override
    public void onMapReady(GoogleMap map) {
        this.mapa = map;

        LatLng myLocation = new LatLng(latitude, longitude);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16));

    }


    private double formatNumber(double distance) {
        distance /= 1000;
        return distance;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 218 && resultCode == RESULT_OK) {
            Bundle res = data.getExtras();

            latOrigen = res.getDouble("latOrigen");
            longOrigen = res.getDouble("longOrigen");
            latDestino = res.getDouble("latDestino");
            longDestino = res.getDouble("longDestino");
            dirOrigen = res.getString("dirOrigen");
            dirDestino = res.getString("dirDestino");

            layout_origen_destino.setVisibility(View.VISIBLE);



            final View viewMarker = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.mr_sucursal, null);
            final ImageView img = (ImageView) viewMarker.findViewById(R.id.imgSucursal);

            Bitmap bitmap= BitmapFactory.decodeResource(this.getResources(), R.drawable.logo);
            Bitmap bitmap2= BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_my_location_color_primary_24dp);
            img.setImageBitmap(bitmap);
            Bitmap sucursalView = createDrawableFromView(Main.this, viewMarker);

            mapa.clear();

            mapa.addMarker(new MarkerOptions()
                    .position(new LatLng(latOrigen , longOrigen))
                    .title("Origen")
                    .icon(BitmapDescriptorFactory.fromBitmap(sucursalView)));

            img.setImageBitmap(bitmap2);
            Bitmap sucursalView2 = createDrawableFromView(Main.this, viewMarker);
            mapa.addMarker(new MarkerOptions()
                    .position(new LatLng(latDestino , longDestino))
                    .title("Destino")
                    .icon(BitmapDescriptorFactory.fromBitmap(sucursalView2)));


        }
    }


    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_settings:
                Intent intent2 = new Intent(Main.this, AcercaDe.class);
                startActivity(intent2);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private void ObtenerPrecioWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.ObtenerPrecioWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {

                pDialog = new SweetAlertDialog(Main.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Verificando, espere");
                pDialog.setCancelable(false);
                pDialog.show();

            }

            @Override
            public void onTaskDownloadedFinished(HashMap<String, Object> result) {
                try {
                    int statusCode = Integer.parseInt(result.get("StatusCode").toString());
                    if (statusCode == 0) {
                        resultadoObtenerPrecio = gson.fromJson(result.get("Resultado").toString(), ResultadoObtenerPrecio.class);
                    }
                } catch (Exception error) {

                }
            }

            @Override
            public void onTaskUpdate(String result) {
            }

            @Override
            public void onTaskComplete(HashMap<String, Object> result) {
                if (resultadoObtenerPrecio.isError())
                {
                    pDialog.dismiss();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Main.this, R.style.AppCompatAlertDialogStyle);
                    dialog.setMessage("No hay servicio en esta zona.");
                    dialog.setCancelable(true);
                    dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
                else {
                    if (resultadoObtenerPrecio.getMessage().equals("Ok")){
                        pDialog.dismiss();
                        Solicitud solicitud = new Solicitud();
                        solicitud.setLatOrigen(String.valueOf(latOrigen));
                        solicitud.setLatDestino(String.valueOf(latDestino));
                        solicitud.setLongOrigen(String.valueOf(longOrigen));
                        solicitud.setLongDestino(String.valueOf(longDestino));
                        solicitud.setPrice(resultadoObtenerPrecio.getData().getPrice());
                        solicitud.setTimeRest("");
                        solicitud.setDirDestino(dirDestino);
                        solicitud.setDirOrigen(dirOrigen);
                        solicitud.setCabbie_Id(resultadoObtenerPrecio.getData().getCabbie_Id().toString());
                        solicitud.setCabbie(resultadoObtenerPrecio.getData().getCabbie_Name().toString());
                        solicitud.setLatCabbie(resultadoObtenerPrecio.getData().getLatCabbie());
                        solicitud.setLongCabbie(resultadoObtenerPrecio.getData().getLongCabbie());
                        solicitud.setGcmIdCabbie(resultadoObtenerPrecio.getData().getGcmIdCabbie());
                        solicitud.setBrand(resultadoObtenerPrecio.getData().getBrand());
                        solicitud.setModel(resultadoObtenerPrecio.getData().getModel());
                        solicitud.setPassengers(resultadoObtenerPrecio.getData().getPassengers());
                        solicitud.setImage(resultadoObtenerPrecio.getData().getImage());
                        solicitud.setDist(resultadoObtenerPrecio.getData().getDits());
                        Intent intent = new Intent(Main.this, Detalles_Solicitud.class);
                        intent.putExtra("Solicitud", solicitud);
                        startActivity(intent);
                    }
                    else {
                        pDialog.dismiss();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Main.this, R.style.AppCompatAlertDialogStyle);
                        dialog.setMessage("No hay taxista disponibles en este momento.");
                        dialog.setCancelable(true);
                        dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        dialog.show();
                    }
                }
            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
                pDialog.dismiss();
            }
        });
        servicioAsyncService.execute();
    }


}
