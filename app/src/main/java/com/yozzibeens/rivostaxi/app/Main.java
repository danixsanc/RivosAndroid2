package com.YozziBeens.rivostaxi.app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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
import com.YozziBeens.rivostaxi.utilerias.Servicio;
import com.google.maps.android.SphericalUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Main extends AppCompatActivity implements GoogleMap.OnMapClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{

    private DrawerMenu mDrawerMenu;
    private GoogleMap mapa;
    double latitude;
    double longitude;
    FloatingActionButton btnpedirtaxi;
    FloatingActionButton btn_ver_favoritos;
    private static String KEY_SUCCESS = "Success";
    TextView txt_address;
    double latitudeIcon;
    double longitudeIcon;
    String address;
    String city;
    String state;
    String country;
    String direccion;
    Button clear_text;
    String[] titulo;
    String[] titulo2;
    String[] lat;
    String[] lon;

    private static final String LOG_TAG = "Main";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocomplete_final;
    private TextView mNameTextView_final;
    private TextView mAddressTextView_final;
    private TextView mIdTextView_final;
    private TextView mPhoneTextView_final;
    private TextView mWebTextView_final;
    private TextView mAttTextView_final;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds latLngBounds = new LatLngBounds(new LatLng(22.810343, -105.783098),new LatLng(26.275327, -109.157343));



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


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

                // txt_address = (TextView) findViewById(R.id.txt_address);

            Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");

                mGoogleApiClient = new GoogleApiClient.Builder(Main.this)
                        .addApi(Places.GEO_DATA_API)
                        .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                        .addConnectionCallbacks(this)
                        .build();
                mAutocomplete_final = (AutoCompleteTextView) findViewById(R.id.mAutocomplete_destino);
                mAutocomplete_final.setThreshold(3);
                mAutocomplete_final.setTypeface(RobotoCondensed_Regular);
                mNameTextView_final = (TextView) findViewById(R.id.name_final);
                mNameTextView_final.setTypeface(RobotoCondensed_Regular);
                mAddressTextView_final = (TextView) findViewById(R.id.address_final);
                mAddressTextView_final.setTypeface(RobotoCondensed_Regular);
                mIdTextView_final = (TextView) findViewById(R.id.place_id_final);
                mIdTextView_final.setTypeface(RobotoCondensed_Regular);
                mPhoneTextView_final = (TextView) findViewById(R.id.phone_final);
                mPhoneTextView_final.setTypeface(RobotoCondensed_Regular);
                mWebTextView_final = (TextView) findViewById(R.id.web_final);
                mWebTextView_final.setTypeface(RobotoCondensed_Regular);
                mAttTextView_final = (TextView) findViewById(R.id.att_final);
                mAttTextView_final.setTypeface(RobotoCondensed_Regular);
                mAutocomplete_final.setOnItemClickListener(mAutocompleteClickListener_final);
                mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1, latLngBounds, null);
                mAutocomplete_final.setAdapter(mPlaceArrayAdapter);

                clear_text = (Button) findViewById(R.id.clear_text);
                clear_text.setTypeface(RobotoCondensed_Regular);

                mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
                mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mapa.setMyLocationEnabled(true);
                mapa.getUiSettings().setZoomControlsEnabled(false);
                mapa.getUiSettings().setCompassEnabled(true);
/*        mapa.addMarker(new MarkerOptions()
                .position(new LatLng(mapa.getMyLocation().getLatitude(), mapa.getMyLocation().getLongitude()))
                .title("UPV")
                .snippet("Universidad Politécnica de Valencia")
                .icon(BitmapDescriptorFactory
                        .fromResource(android.R.drawable.ic_menu_compass))
                .anchor(0.5f, 0.5f));*/



                //mapa.setOnMapClickListener(this);

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
                       final MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Aqui Me Encuentro");
                       marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                       mapa.addMarker(marker);

                       //mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapa.getMyLocation().getLatitude(), mapa.getMyLocation().getLongitude()), 15));
                       CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(16).build();
                       mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                       mapa.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

                           @Override
                           public void onCameraChange(CameraPosition position) {
                               latitudeIcon = mapa.getCameraPosition().target.latitude;
                               longitudeIcon = mapa.getCameraPosition().target.longitude;

                               Geocoder geocoder;
                               List<Address> addresses = null;
                               geocoder = new Geocoder(Main.this, Locale.getDefault());
                               try {
                                   addresses = geocoder.getFromLocation(latitudeIcon, longitudeIcon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                   address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                   city = addresses.get(0).getLocality();
                                   state = addresses.get(0).getAdminArea();
                                   country = addresses.get(0).getCountryName();
                                   direccion = address + "" + city + "" + state + "" + country;
                                   mAutocomplete_final.setText(direccion);
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }


                           }
                       });
                   }

                }












                clear_text.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        mAutocomplete_final.setText("");
                    }
                });

                btn_ver_favoritos = (FloatingActionButton) findViewById(R.id.btn_ver_favoritos);
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


                                if (exiteConexionInternet()) {
                                    try {
                                        Servicio servicio = new Servicio();
                                        final JSONObject json2 = servicio.GetIfIsAriport(latitude, longitude);
                                        //final JSONObject json = userFunctions.getUser(Client_Id);
                                        if (json2.getString(KEY_SUCCESS) != null) {
                                            String res = json2.getString(KEY_SUCCESS);
                                            if (Integer.parseInt(res) == 1)
                                            {
                                                int pricef = 0;
                                                int price_id = 0;
                                                final JSONObject json = servicio.getPriceAirportToColony(latitudeIcon, longitudeIcon);
                                                try {
                                                    if (json.getString(KEY_SUCCESS) != null) {
                                                        String res4 = json.getString(KEY_SUCCESS);
                                                        if (Integer.parseInt(res4) == 1) {
                                                            pricef = Integer.valueOf(json.getString("Price"));
                                                            price_id = Integer.valueOf(json.getString("Price_Id"));
                                                        }
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                Intent intent = new Intent(Main.this, Detalles_Solicitud.class);
                                                intent.putExtra("direccion", titulo2[item]);
                                                intent.putExtra("Lat", latitude);
                                                intent.putExtra("Long", longitude);
                                                intent.putExtra("Lat_icon", Double.valueOf(lat[item]));
                                                intent.putExtra("Long_icon", Double.valueOf(lon[item]));
                                                intent.putExtra("Price", pricef);
                                                intent.putExtra("price_Id", price_id);
                                                startActivity(intent);
                                            }
                                            else if (Integer.parseInt(res) == 2){

                                                final JSONObject json3 = servicio.VerifyDestination(String.valueOf(latitudeIcon), String.valueOf(longitudeIcon));
                                                if (json3.getString(KEY_SUCCESS) != null) {
                                                    String res2 = json3.getString(KEY_SUCCESS);
                                                    if (Integer.parseInt(res2) == 1) {

                                                        int pricef = 0;
                                                        int price_id = 0;
                                                        final JSONObject json = servicio.getPriceColonyToAirport(latitude, longitude);
                                                        try {
                                                            if (json.getString(KEY_SUCCESS) != null) {
                                                                String res4 = json.getString(KEY_SUCCESS);
                                                                if (Integer.parseInt(res4) == 1) {
                                                                    pricef = Integer.valueOf(json.getString("Price_D"));
                                                                    price_id = Integer.valueOf(json.getString("Price_Id"));
                                                                }
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        Intent intent = new Intent(Main.this, Detalles_Solicitud.class);
                                                        intent.putExtra("direccion", titulo2[item]);
                                                        intent.putExtra("Lat", latitude);
                                                        intent.putExtra("Long", longitude);
                                                        intent.putExtra("Lat_icon", Double.valueOf(lat[item]));
                                                        intent.putExtra("Long_icon", Double.valueOf(lon[item]));
                                                        intent.putExtra("Price", pricef);
                                                        intent.putExtra("price_Id", price_id);
                                                        startActivity(intent);

                                                    }
                                                    else if (Integer.parseInt(res2) == 2)
                                                    {
                                                        int pricef = 0;
                                                        int price_id = 0;

                                                        LatLng l1 = new LatLng(latitude, longitude);
                                                        LatLng l2 = new LatLng(Double.valueOf(lat[item]), Double.valueOf(lon[item]));

                                                        double distance = SphericalUtil.computeDistanceBetween(l1, l2);
                                                        double distancef = (formatNumber(distance));



                                                        final JSONObject json = servicio.getPriceColonyToColony(distancef);
                                                        try {
                                                            if (json.getString(KEY_SUCCESS) != null) {
                                                                String res4 = json.getString(KEY_SUCCESS);
                                                                if (Integer.parseInt(res4) == 1) {
                                                                    pricef = Integer.valueOf(json.getString("Price"));
                                                                    price_id = Integer.valueOf("1");
                                                                }
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        Intent intent = new Intent(Main.this, Detalles_Solicitud.class);
                                                        intent.putExtra("direccion", titulo2[item]);
                                                        intent.putExtra("Lat", latitude);
                                                        intent.putExtra("Long", longitude);
                                                        intent.putExtra("Lat_icon", Double.valueOf(lat[item]));
                                                        intent.putExtra("Long_icon", Double.valueOf(lon[item]));
                                                        intent.putExtra("Price", pricef);
                                                        intent.putExtra("price_Id", 1);
                                                        startActivity(intent);
                                                    }
                                                }

                                            }
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(Main.this, R.style.MyAlertDialogStyle);
                                    builder.setTitle("Ups..");
                                    builder.setMessage("Parece que no esta conectado a Internet. Porfavor revise su conexion e intentelo de nuevo.");
                                    builder.setPositiveButton("OK", null);
                                    //builder.setNegativeButton("Cancel", null);
                                    builder.show();
                                }


                                /*Intent intent = new Intent(Main.this, Detalles_Solicitud.class);
                                intent.putExtra("direccion", titulo2[item]);
                                intent.putExtra("Lat", latitude);
                                intent.putExtra("Long", longitude);
                                intent.putExtra("Lat_icon", Double.valueOf(lat[item]));
                                intent.putExtra("Long_icon", Double.valueOf(lon[item]));
                                startActivity(intent);*/

                            }

                        });
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder.show();
                    }
                });





                btnpedirtaxi = (FloatingActionButton) findViewById(R.id.btn_ver_taxistas);
                btnpedirtaxi.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {


                        if (mAutocomplete_final.getText().toString().equals(""))
                        {
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(Main.this, R.style.MyAlertDialogStyle);
                            builder2.setTitle("No hay un destino seleccionado");
                            builder2.setMessage(mAutocomplete_final.getText().toString());
                            builder2.setNegativeButton("Ok", null);
                            builder2.show();

                        }
                        else
                        {

                            if (((Math.abs(latitude - latitudeIcon)) < 0.0025) && ((Math.abs(longitude - longitudeIcon)) < 0.0025))
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Main.this, R.style.MyAlertDialogStyle);
                                builder.setTitle("Ups..");
                                builder.setMessage("No puedes seleccionar un destino tan corto");
                                builder.setPositiveButton("OK", null);
                                builder.show();
                            }
                            else
                            {
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(Main.this, R.style.MyAlertDialogStyle);
                                builder2.setTitle("¿Es correcto?");
                                builder2.setMessage(mAutocomplete_final.getText().toString());
                                builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (exiteConexionInternet()) {
                                            try {
                                                Servicio servicio = new Servicio();
                                                final JSONObject json2 = servicio.GetIfIsAriport(latitude, longitude);
                                                //final JSONObject json = userFunctions.getUser(Client_Id);
                                                if (json2.getString(KEY_SUCCESS) != null) {
                                                    String res = json2.getString(KEY_SUCCESS);
                                                    if (Integer.parseInt(res) == 1)
                                                    {
                                                        int pricef = 0;
                                                        int price_id = 0;
                                                        final JSONObject json = servicio.getPriceAirportToColony(latitudeIcon, longitudeIcon);
                                                        try {
                                                            if (json.getString(KEY_SUCCESS) != null) {
                                                                String res4 = json.getString(KEY_SUCCESS);
                                                                if (Integer.parseInt(res4) == 1) {
                                                                    pricef = Integer.valueOf(json.getString("Price"));
                                                                    price_id = Integer.valueOf(json.getString("Price_Id"));
                                                                }
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        Intent intent = new Intent(Main.this, Detalles_Solicitud.class);
                                                        intent.putExtra("direccion", direccion);
                                                        intent.putExtra("Lat", latitude);
                                                        intent.putExtra("Long", longitude);
                                                        intent.putExtra("Lat_icon", latitudeIcon);
                                                        intent.putExtra("Long_icon", longitudeIcon);
                                                        intent.putExtra("Price", pricef);
                                                        intent.putExtra("price_Id", price_id);
                                                        startActivity(intent);
                                                    }
                                                    else if (Integer.parseInt(res) == 2){

                                                        final JSONObject json3 = servicio.VerifyDestination(String.valueOf(latitudeIcon), String.valueOf(longitudeIcon));
                                                        if (json3.getString(KEY_SUCCESS) != null) {
                                                            String res2 = json3.getString(KEY_SUCCESS);
                                                            if (Integer.parseInt(res2) == 1) {

                                                                int pricef = 0;
                                                                int price_id = 0;
                                                                final JSONObject json = servicio.getPriceColonyToAirport(latitude, longitude);
                                                                try {
                                                                    if (json.getString(KEY_SUCCESS) != null) {
                                                                        String res4 = json.getString(KEY_SUCCESS);
                                                                        if (Integer.parseInt(res4) == 1) {
                                                                            pricef = Integer.valueOf(json.getString("Price_D"));
                                                                            price_id = Integer.valueOf(json.getString("Price_Id"));
                                                                        }
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                Intent intent = new Intent(Main.this, Detalles_Solicitud.class);
                                                                intent.putExtra("direccion", direccion);
                                                                intent.putExtra("Lat", latitude);
                                                                intent.putExtra("Long", longitude);
                                                                intent.putExtra("Lat_icon", latitudeIcon);
                                                                intent.putExtra("Long_icon", longitudeIcon);
                                                                intent.putExtra("Price", pricef);
                                                                intent.putExtra("price_Id", price_id);
                                                                startActivity(intent);

                                                            }
                                                            else if (Integer.parseInt(res2) == 2)
                                                            {
                                                                int pricef = 0;
                                                                int price_id = 0;

                                                                LatLng l1 = new LatLng(latitude, longitude);
                                                                LatLng l2 = new LatLng(latitudeIcon, longitudeIcon);

                                                                double distance = SphericalUtil.computeDistanceBetween(l1, l2);
                                                                double distancef = (formatNumber(distance));



                                                                final JSONObject json = servicio.getPriceColonyToColony(distancef);
                                                                try {
                                                                    if (json.getString(KEY_SUCCESS) != null) {
                                                                        String res4 = json.getString(KEY_SUCCESS);
                                                                        if (Integer.parseInt(res4) == 1) {
                                                                            pricef = Integer.valueOf(json.getString("Price"));
                                                                            price_id = Integer.valueOf("1");
                                                                        }
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                Intent intent = new Intent(Main.this, Detalles_Solicitud.class);
                                                                intent.putExtra("direccion", direccion);
                                                                intent.putExtra("Lat", latitude);
                                                                intent.putExtra("Long", longitude);
                                                                intent.putExtra("Lat_icon", latitudeIcon);
                                                                intent.putExtra("Long_icon", longitudeIcon);
                                                                intent.putExtra("Price", pricef);
                                                                intent.putExtra("price_Id", price_id);
                                                                startActivity(intent);
                                                            }
                                                        }

                                                    }
                                                }


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {

                                            AlertDialog.Builder builder = new AlertDialog.Builder(Main.this, R.style.MyAlertDialogStyle);
                                            builder.setTitle("Ups..");
                                            builder.setMessage("Parece que no esta conectado a Internet. Porfavor revise su conexion e intentelo de nuevo.");
                                            builder.setPositiveButton("OK", null);
                                            //builder.setNegativeButton("Cancel", null);
                                            builder.show();
                                        }
                                    }
                                });

                                builder2.setNegativeButton("Cancel", null);
                                builder2.show();
                            }

                        }
                    }
                });
            }






    }

    private double formatNumber(double distance) {
        distance /= 1000;
        return distance;
    }


    private AdapterView.OnItemClickListener mAutocompleteClickListener_final = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback_final);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback_final = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            mNameTextView_final.setText(Html.fromHtml(place.getName() + ""));
            mAddressTextView_final.setText(Html.fromHtml(place.getAddress() + ""));
            mIdTextView_final.setText(Html.fromHtml(place.getId() + ""));
            mPhoneTextView_final.setText(Html.fromHtml(place.getPhoneNumber() + ""));
            mWebTextView_final.setText(place.getWebsiteUri() + "");

            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(place.getLatLng().latitude
                    , place.getLatLng().longitude)).zoom(16).build();
            mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            if (attributions != null) {
                mAttTextView_final.setText(Html.fromHtml(attributions.toString()));
            }
        }
    };





    public void moveCamera(View view) {
        mapa.moveCamera(CameraUpdateFactory.newLatLng(new LatLng( mapa.getMyLocation().getLatitude(), mapa.getMyLocation().getLongitude())));
    }

    public void animateCamera(View view) {
        if (mapa.getMyLocation() != null)
            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng( mapa.getMyLocation().getLatitude(), mapa.getMyLocation().getLongitude()), 15));
}

    public void addMarker(View view) {
        mapa.addMarker(new MarkerOptions().position(
                new LatLng(mapa.getCameraPosition().target.latitude,
                        mapa.getCameraPosition().target.longitude)));
    }

    @Override
    public void onMapClick(LatLng puntoPulsado) {
        mapa.addMarker(new MarkerOptions().position(puntoPulsado)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
    }


    public boolean exiteConexionInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
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

    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");

    }


    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }


    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }
}
