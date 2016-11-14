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
import android.graphics.Interpolator;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.modelo.Ciudad;
import com.YozziBeens.rivostaxi.modelo.Coords;
import com.YozziBeens.rivostaxi.modelo.RivosDB;
import com.YozziBeens.rivostaxi.modelo.closeCabbie;
import com.YozziBeens.rivostaxi.modelosApp.Solicitud;
import com.YozziBeens.rivostaxi.respuesta.ResultadoAgregarTaxistaFavorito;
import com.YozziBeens.rivostaxi.respuesta.ResultadoObtenerPrecio;
import com.YozziBeens.rivostaxi.respuesta.ResultadoObtenerTaxistasCercanos;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudLugaresFavoritos;
import com.YozziBeens.rivostaxi.solicitud.SolicitudObtenerPrecio;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.actividades.Solicitar.Detalles_Solicitud;
import com.YozziBeens.rivostaxi.fragmentos.DrawerMenu;
import com.YozziBeens.rivostaxi.tutorial.TutorialActivity;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;

import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Main extends AppCompatActivity implements OnMapReadyCallback, RoutingListener {

    private DrawerMenu mDrawerMenu;
    private GoogleMap mapa;
    private FloatingActionButton btnpedirtaxi;
    private String dirOrigen, dirDestino;
    private SweetAlertDialog pDialog;
    private LinearLayout layout_origen_destino;
    private Gson gson;
    private ResultadoObtenerPrecio resultadoObtenerPrecio;
    private ResultadoObtenerTaxistasCercanos resultadoObtenerTaxistasCercanos;
    private Preferencias preferencias;
    private double latOrigen, longOrigen, latDestino, longDestino, latitude, longitude;
    //private TextView nameWindows;

    Marker marker1, marker2, marker3, marker4, marker5;
    static final LatLng MELBOURNE = new LatLng(24, -107);

    Handler h;
    int delay = 2000; //milliseconds
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RivosDB.initializeInstance();
        setContentView(R.layout.main);
        this.gson = new Gson();



        this.preferencias = new Preferencias(getApplicationContext());
        boolean check = preferencias.getSesion();
        this.resultadoObtenerPrecio = new ResultadoObtenerPrecio();
        this.resultadoObtenerTaxistasCercanos = new ResultadoObtenerTaxistasCercanos();
        //this.nameWindows = (TextView) findViewById(R.id.nameWindows);
        //this.nameWindows.setText("Rivos Taxi");
        Typeface Orbi = Typeface.createFromAsset(getAssets(), "orbitron-medium.otf");
        //this.nameWindows.setTypeface(Orbi);

        this.h = new Handler();




        if (check) {
            Intent intent = new Intent(Main.this, Login.class);
            startActivity(intent);
            finish();
        } else {

            boolean checkTutorial = preferencias.getTutorial();

            if (checkTutorial) {
                Intent intent2 = new Intent(Main.this, TutorialActivity.class);
                startActivity(intent2);
            }

            this.toolbar = (Toolbar) findViewById(R.id.toolbar2);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);



            h.postDelayed(new Runnable(){
                public void run(){
                    LatLng latlng = mapa.getCameraPosition().target;
                    Coords oData = new Coords();
                    oData.setLatitude(String.valueOf(latlng.latitude));
                    oData.setLongitude(String.valueOf(latlng.longitude));
                    ObtenerTaxistasCercanos(gson.toJson(oData));
                    h.postDelayed(this, delay);
                }
            }, delay);

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

            if (lastlocation != null){
                latitude = lastlocation.getLatitude();
                longitude = lastlocation.getLongitude();
            }
            else {
                latitude = 0;
                longitude = 0;
            }



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

    private void ObtenerTaxistasCercanos(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.ObtenerTaxistasCernanos, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
            }

            @Override
            public void onTaskDownloadedFinished(HashMap<String, Object> result) {
                try {
                    int statusCode = Integer.parseInt(result.get("StatusCode").toString());
                    if (statusCode == 0) {
                        resultadoObtenerTaxistasCercanos = gson.fromJson(result.get("Resultado").toString(), ResultadoObtenerTaxistasCercanos.class);
                    }
                } catch (Exception error) {

                }
            }

            @Override
            public void onTaskUpdate(String result) {
            }

            @Override
            public void onTaskComplete(HashMap<String, Object> result) {
                //mapa.clear();

                //for (closeCabbie temp: resultadoObtenerTaxistasCercanos.getData()){

                LatLng latLng1 = new LatLng(Double.valueOf(resultadoObtenerTaxistasCercanos.getData().get(0).getLatitude()),
                        Double.valueOf(resultadoObtenerTaxistasCercanos.getData().get(0).getLongitude()));
                animateMarker(marker1, latLng1, false);

                LatLng latLng2 = new LatLng(Double.valueOf(resultadoObtenerTaxistasCercanos.getData().get(1).getLatitude()),
                        Double.valueOf(resultadoObtenerTaxistasCercanos.getData().get(1).getLongitude()));
                animateMarker(marker2, latLng2, false);

                LatLng latLng3 = new LatLng(Double.valueOf(resultadoObtenerTaxistasCercanos.getData().get(2).getLatitude()),
                        Double.valueOf(resultadoObtenerTaxistasCercanos.getData().get(2).getLongitude()));
                animateMarker(marker3, latLng3, false);

                LatLng latLng4 = new LatLng(Double.valueOf(resultadoObtenerTaxistasCercanos.getData().get(3).getLatitude()),
                        Double.valueOf(resultadoObtenerTaxistasCercanos.getData().get(3).getLongitude()));
                animateMarker(marker4, latLng4, false);

                LatLng latLng5 = new LatLng(Double.valueOf(resultadoObtenerTaxistasCercanos.getData().get(4).getLatitude()),
                        Double.valueOf(resultadoObtenerTaxistasCercanos.getData().get(4).getLongitude()));
                animateMarker(marker5, latLng5, false);

                //}
            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
                pDialog.dismiss();
            }
        });
        servicioAsyncService.execute();
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

        final View viewMarker = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.mr_sucursal, null);
        final ImageView img = (ImageView) viewMarker.findViewById(R.id.imgSucursal);
        Bitmap bitmap= BitmapFactory.decodeResource(this.getResources(), R.drawable.logo);
        img.setImageBitmap(bitmap);
        Bitmap mark = createDrawableFromView(Main.this, viewMarker);


        MarkerOptions a = new MarkerOptions().position(new LatLng(24.8,-107.4))
                .icon(BitmapDescriptorFactory.fromBitmap(mark));

        marker1 = mapa.addMarker(a);
        marker2 = mapa.addMarker(a);
        marker3 = mapa.addMarker(a);
        marker4 = mapa.addMarker(a);
        marker5 = mapa.addMarker(a);

        mapa.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                float a = toolbar.getTranslationY();
                if (toolbar.getTranslationY() == 0)
                {
                    toolbar.animate().translationY(-toolbar.getHeight());
                }
                else{
                    toolbar.animate().translationY(0);
                }

            }
        });


    }




    public void animateMarker(final Marker marker, final LatLng toPosition, final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mapa.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final LinearInterpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
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


    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {

    }

    @Override
    public void onRoutingCancelled() {

    }
}
