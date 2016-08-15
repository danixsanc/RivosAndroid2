package com.YozziBeens.rivostaxi.app;



import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Layout;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.YozziBeens.rivostaxi.fragmentos.DrawerBottom;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.modelo.RivosDB;
import com.YozziBeens.rivostaxi.respuesta.ResultadoObtenerPrecio;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudObtenerPrecio;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.actividades.Solicitar.Detalles_Solicitud;
import com.YozziBeens.rivostaxi.adaptadores.PlaceArrayAdapter;
import com.YozziBeens.rivostaxi.controlador.Favorite_PlaceController;
import com.YozziBeens.rivostaxi.fragmentos.DrawerMenu;
import com.YozziBeens.rivostaxi.modelo.Favorite_Place;
import com.YozziBeens.rivostaxi.tutorial.TutorialActivity;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;

import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Main extends AppCompatActivity{

    private DrawerMenu mDrawerMenu;
    private DrawerBottom mDrawerbottom;
    private GoogleMap mapa;
    double latitude;
    double longitude;
    FloatingActionButton btnpedirtaxi;
    //FloatingActionButton btn_ver_favoritos;
    TextView txt_address;
    double latitudeIcon;
    double longitudeIcon;
    String address;
    String city;
    String state;
    String country;
    String direccion;
    //Button clear_text;
    String[] titulo;
    String[] titulo2;
    String[] lat;
    String[] lon;
    int itemFinal;
    private Location userLocation;

    double latOrigen;
    double longOrigen;
    double latDestino;
    double longDestino;
    String dirOrigen;
    String dirDestino;

    SweetAlertDialog pDialog;

    LinearLayout layout_origen_destino;
    private Gson gson;
    private ProgressDialog progressdialog;
    private ResultadoObtenerPrecio resultadoObtenerPrecio;
    private boolean isAirport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RivosDB.initializeInstance();
        setContentView(R.layout.main);
        this.gson = new Gson();



        Preferencias p = new Preferencias(getApplicationContext());
        boolean check = p.getSesion();
        if (check) {
            Intent intent2 = new Intent(Main.this, Login.class);
            startActivity(intent2);
            finish();
        }
        else {
            boolean checkTutorial = p.getTutorial();
            if (checkTutorial) {
                Intent intent2 = new Intent(Main.this, TutorialActivity.class);
                startActivity(intent2);
            }

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);



                mDrawerMenu = (DrawerMenu) getSupportFragmentManager().findFragmentById(R.id.left_drawer);
                mDrawerMenu.setUp(R.id.left_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar, getSupportActionBar(), this);

                layout_origen_destino = (LinearLayout) findViewById(R.id.layout_origen_destino);

                mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
                mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mapa.setMyLocationEnabled(true);
                mapa.getUiSettings().setZoomControlsEnabled(false);
                mapa.getUiSettings().setCompassEnabled(true);

                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria, true);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location location = locationManager.getLastKnownLocation(provider);


               if(location!=null){
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                   if ((latitude == 0) && (longitude == 0))
                   {
                       AlertDialog.Builder dialog1 = new AlertDialog.Builder(Main.this, R.style.AppCompatAlertDialogStyle);
                       dialog1.setMessage("El GPS esta desactivado, ¿Desea Activarlo?");
                       dialog1.setCancelable(false);
                       dialog1.setPositiveButton("Si", new DialogInterface.OnClickListener() {

                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                               startActivity(intent);
                           }
                       });
                       dialog1.setNegativeButton("No", new DialogInterface.OnClickListener() {

                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.cancel();
                           }
                       });
                       dialog1.show();
                   }
                   else{
                       //final MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Aqui Me Encuentro");
                       //marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                       //mapa.addMarker(marker);

                       //mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapa.getMyLocation().getLatitude(), mapa.getMyLocation().getLongitude()), 15));
                       CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(16).build();
                       mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                       /*mapa.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

                           @Override
                           public void onCameraChange(CameraPosition position) {

                               latitudeIcon = mapa.getCameraPosition().target.latitude;
                               longitudeIcon = mapa.getCameraPosition().target.longitude;

                               Geocoder geocoder;
                               List<Address> addresses = null;
                               geocoder = new Geocoder(Main.this, Locale.getDefault());
                               try {
                                   addresses = geocoder.getFromLocation(latitudeIcon, longitudeIcon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                   if (addresses.isEmpty())
                                   {
                                       mAutocomplete_final.setText("Selecciona una dirección valida");
                                       //txtDireccionDestino
                                   }
                                   else
                                   {
                                       address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                       city = addresses.get(0).getLocality();
                                       state = addresses.get(0).getAdminArea();
                                       country = addresses.get(0).getCountryName();
                                       direccion = address + " " + city + " " + state + " " + country;
                                       mAutocomplete_final.setText(direccion);
                                   }

                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           }
                       });*/
                   }

                }












                /*clear_text.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        mAutocomplete_final.setText("");
                    }
                });*/

                /*btn_ver_favoritos = (FloatingActionButton) findViewById(R.id.btn_ver_favoritos);
                btn_ver_favoritos.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Favorite_PlaceController favorite_placeController = new Favorite_PlaceController(getApplicationContext());
                        List<Favorite_Place> listfp = favorite_placeController.obtenerFavorite_Place();
                        titulo = new String[listfp.size()];
                        titulo2 = new String[listfp.size()];
                        lat = new String[listfp.size()];
                        lon = new String[listfp.size()];
                        for (int i = 0; i < listfp.size(); i++) {
                            String name = listfp.get(i).getName();
                            String desc = listfp.get(i).getDesc();
                            String lat1 = listfp.get(i).getLatitude();
                            String lat2 = listfp.get(i).getLongitude();
                            titulo[i] = name;
                            titulo2[i] = desc;
                            lat[i] = lat1;
                            lon[i] = lat2;
                        }
                        final CharSequence[] items = titulo;
                        AlertDialog.Builder builder = new AlertDialog.Builder(Main.this, R.style.AppCompatAlertDialogStyle);
                        builder.setTitle("Lugares Favoritos");
                        builder.setItems(items, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int item) {


                                LatLng l1 = new LatLng(latitude, longitude);
                                LatLng l2 = new LatLng(Double.valueOf(lat[item]), Double.valueOf(lon[item]));

                                double distance = SphericalUtil.computeDistanceBetween(l1, l2);
                                double distancef = (formatNumber(distance));


                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(Main.this, Locale.getDefault());
                                try {
                                    addresses = geocoder.getFromLocation(Double.valueOf(lat[item]), Double.valueOf(lon[item]), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                    if (!addresses.isEmpty())
                                    {
                                        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                        city = addresses.get(0).getLocality();
                                        state = addresses.get(0).getAdminArea();
                                        country = addresses.get(0).getCountryName();
                                        direccion = address + " " + city + " " + state + " " + country;
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                itemFinal = item;
                                SolicitudObtenerPrecio oData = new SolicitudObtenerPrecio();
                                oData.setLatitude_In(String.valueOf(latitude));
                                oData.setLongitude_In(String.valueOf(longitude));
                                oData.setLatitude_Fn(String.valueOf(lat[item]));
                                oData.setLongitude_Fn(String.valueOf(lon[item]));
                                oData.setDistance(String.valueOf(distancef));
                                ObtenerPrecioFavoriteWebService(gson.toJson(oData));
                            }

                        });
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder.show();
                    }
                });*/





                btnpedirtaxi = (FloatingActionButton) findViewById(R.id.btn_ver_taxistas);
                btnpedirtaxi.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {

                        if ((latOrigen == 0.0) && (latDestino == 0.0) && (longOrigen == 0.0) && (longDestino == 0.0))
                        {
                            new SweetAlertDialog(Main.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Primero debes seleccionar un destino!")
                                    .setConfirmText("Entendido")
                                    .show();

                        }
                        else{
                            if (((Math.abs(latOrigen - latDestino)) < 0.0025) && ((Math.abs(longOrigen - longDestino)) < 0.0025))
                            {

                                new SweetAlertDialog(Main.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("No puedes seleccionar un destino tan corto")
                                        .setConfirmText("Entendido")
                                        .show();
                            }
                            else
                            {
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
            if (bundle != null){
                String c = bundle.getString("Notif");

                if (c.equals("C")){
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




            /*MarkerOptions markerOrigen = new MarkerOptions().position(new LatLng(latOrigen, longOrigen)).title("Origen");
            markerOrigen.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_taxi_black_24dp));
            mapa.addMarker(markerOrigen);


            MarkerOptions markerDestino = new MarkerOptions().position(new LatLng(latDestino, longDestino)).title("Origen");
            markerOrigen.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_black_24dp));
            mapa.addMarker(markerDestino);*/

        }
    }
    public class DbBitmapUtility {

        // convert from bitmap to byte array
        public byte[] getBytes(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            return stream.toByteArray();
        }

        // convert from byte array to bitmap
        public Bitmap getImage(byte[] image) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
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



    /*private void ObtenerPrecioFavoriteWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.ObtenerPrecioWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
                progressdialog = new ProgressDialog(Main.this);
                progressdialog.setMessage("Verificando, espere");
                progressdialog.setCancelable(true);
                progressdialog.setCanceledOnTouchOutside(false);
                progressdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        progressdialog.dismiss();
                    }
                });
                progressdialog.show();
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
                    progressdialog.dismiss();
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
                else
                {
                    progressdialog.dismiss();

                    String price = resultadoObtenerPrecio.getData();

                    String[] priceSplit = price.split(",");
                    int pricef, price_id;
                    if (priceSplit.length == 2) {
                        pricef = Integer.valueOf(priceSplit[0]);
                        price_id = Integer.valueOf(priceSplit[1]);
                    } else {
                        pricef = Integer.valueOf(priceSplit[0]);
                        price_id = 1;
                    }
                    Intent intent = new Intent(Main.this, Detalles_Solicitud.class);
                    intent.putExtra("direccion", direccion);
                    intent.putExtra("Lat", latitude);
                    intent.putExtra("Long", longitude);
                    intent.putExtra("Lat_icon", Double.valueOf(lat[itemFinal]));
                    intent.putExtra("Long_icon", Double.valueOf(lon[itemFinal]));
                    intent.putExtra("Price", pricef);
                    intent.putExtra("price_Id", price_id);
                    startActivity(intent);
                }

            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
                progressdialog.dismiss();
            }
        });
        servicioAsyncService.execute();
    }
*/

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
                    pDialog.dismiss();

                    String price = resultadoObtenerPrecio.getData();

                    String[] priceSplit = price.split(",");
                    int pricef, price_id;
                    if (priceSplit.length == 2) {
                        pricef = Integer.valueOf(priceSplit[0]);
                        price_id = Integer.valueOf(priceSplit[1]);
                    } else {
                        pricef = Integer.valueOf(priceSplit[0]);
                        price_id = 1;
                    }
                    Intent intent = new Intent(Main.this, Detalles_Solicitud.class);
                    intent.putExtra("latOrigen", latOrigen);
                    intent.putExtra("longOrigen", longOrigen);
                    intent.putExtra("latDestino", latDestino);
                    intent.putExtra("longDestino", longDestino);
                    intent.putExtra("dirOrigen", dirOrigen);
                    intent.putExtra("dirDestino", dirDestino);
                    intent.putExtra("Price", pricef);
                    intent.putExtra("price_Id", price_id);
                    startActivity(intent);
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
