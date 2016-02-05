package com.YozziBeens.rivostaxi.actividades.Favoritos;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.adaptadores.PlaceArrayAdapter;
import com.YozziBeens.rivostaxi.controlador.Favorite_PlaceController;
import com.YozziBeens.rivostaxi.modelo.Favorite_Place;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.YozziBeens.rivostaxi.utilerias.Servicio;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Agregar_Lugar_Favorito extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleMap.OnMarkerDragListener {

    ImageButton btn_add_favorite_place;
    Button btn_save_favorite_place;

    AutoCompleteTextView autoCompleteTextView;
    EditText place_name;

    private static String KEY_SUCCESS = "Success";

    private static final String LOG_TAG = "MainActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;
    private EditText PlaceName;
    private TextView mNameTextView;
    private TextView mAddressTextView;
    private TextView mIdTextView;
    private TextView mPhoneTextView;
    private TextView mWebTextView;
    private TextView mAttTextView;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    MarkerOptions markerEdt = new MarkerOptions();


    MapView mMapView;
    private GoogleMap googleMap;
    double latitude;
    double longitude;
    String codes[];

    private GoogleMap mapa1;

    double latMarkerf;
    double lngMarkerf;

    String latitude1;
    String longitude1;
    LatLng position;
    int var = 0;
    int place_id = 0;
    TextView titulo_add_place;
    ImageButton back_button;

    String placeId;
    String status;

    double latitudeIcon;
    double longitudeIcon;

    String address;
    String city;
    String state;
    String country;
    String direccion;

    ImageView img_Marker_center;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_lugar_favorito);

        //----------TIPO DE FUENTE-----------------------------------------------------------------------------
        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setTypeface(RobotoCondensed_Regular);

        place_name = (EditText) findViewById(R.id.place_name);
        place_name.setTypeface(RobotoCondensed_Regular);
        //------------------------------------------------------------------------------------------------------

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapa1= ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa1)).getMap();
        mapa1.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapa1.setMyLocationEnabled(true);
        mapa1.getUiSettings().setZoomControlsEnabled(false);
        mapa1.getUiSettings().setCompassEnabled(true);

        Preferencias preferencias = new Preferencias(getApplicationContext());
        String Client_Id = preferencias.getClient_Id();

        PlaceName = (EditText) findViewById(R.id.place_name);

        img_Marker_center = (ImageView) findViewById(R.id.img_Marker_center);


        mGoogleApiClient = new GoogleApiClient.Builder(Agregar_Lugar_Favorito.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        mAutocompleteTextView.setThreshold(3);
        mNameTextView = (TextView) findViewById(R.id.name);
        mAddressTextView = (TextView) findViewById(R.id.address);
        mIdTextView = (TextView) findViewById(R.id.place_id);
        mPhoneTextView = (TextView) findViewById(R.id.phone);
        mWebTextView = (TextView) findViewById(R.id.web);
        mAttTextView = (TextView) findViewById(R.id.att);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

        btn_add_favorite_place = (ImageButton) findViewById(R.id.btn_add_favorite_place);
        btn_save_favorite_place = (Button) findViewById(R.id.btn_save_favorite_place);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        final Location location = locationManager.getLastKnownLocation(provider);


        if(location!=null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        /*Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            var = Integer.valueOf(bundle.getString("type"));
            place_id = Integer.valueOf(bundle.getString("place_id"));
        }
*/
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            placeId = bundle.getString("placeId");
            status = bundle.getString("Status");

            if (status.equals("detalle"))
            {
                getSupportActionBar().setTitle("Detalle Direccion");
                Favorite_PlaceController favorite_placeController = new Favorite_PlaceController(getApplicationContext());
                Favorite_Place favorite_place = favorite_placeController.obtenerFavorite_PlacePorPlaceId(placeId);

                String name = favorite_place.getName();
                String desc = favorite_place.getDesc();
                double lat = Double.valueOf(favorite_place.getLatitude());
                double lon = Double.valueOf(favorite_place.getLongitude());

                mAutocompleteTextView.setText(desc);
                mAutocompleteTextView.setEnabled(false);
                PlaceName.setText(name);
                PlaceName.setEnabled(false);

                btn_add_favorite_place.setVisibility(View.GONE);
                btn_save_favorite_place.setVisibility(View.GONE);
                img_Marker_center.setVisibility(View.GONE);

                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, lon)).zoom(16).build();
                mapa1.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mapa1.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lon))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            }
            else{

                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(16).build();
                mapa1.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                mapa1.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

                    @Override
                    public void onCameraChange(CameraPosition position) {
                        latitudeIcon = mapa1.getCameraPosition().target.latitude;
                        longitudeIcon = mapa1.getCameraPosition().target.longitude;

                        Geocoder geocoder;
                        List<Address> addresses = null;
                        geocoder = new Geocoder(Agregar_Lugar_Favorito.this, Locale.getDefault());
                        try {
                            addresses = geocoder.getFromLocation(latitudeIcon, longitudeIcon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            city = addresses.get(0).getLocality();
                            state = addresses.get(0).getAdminArea();
                            country = addresses.get(0).getCountryName();
                            direccion = address + "" + city + "" + state + "" + country;
                            mAutocompleteTextView.setText(direccion);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        }





















       /*mMapView = (MapView) findViewById(R.id.map1);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        googleMap.setMyLocationEnabled(true);


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
        }


        marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Aqui Me Encuentro");
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        markerVer.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        markerEdt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        markerEdt.draggable(true);

        googleMap.addMarker(marker);

        //latMarkerf = marker.getPosition().latitude;
        //lngMarkerf = marker.getPosition().longitude;




        if ((latitude == 0) && (longitude == 0))
        {

            AlertDialog.Builder dialog1 = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
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


        //googleMap.setOnMarkerClickListener(this);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        if (var == 2)
        {
            mAutocompleteTextView.setKeyListener(null);
            mAutocompleteTextView.setCursorVisible(false);
            mAutocompleteTextView.setPressed(false);
            mAutocompleteTextView.setFocusable(false);
            PlaceName.setKeyListener(null);
            PlaceName.setCursorVisible(false);
            PlaceName.setPressed(false);
            PlaceName.setFocusable(false);

            Servicio servicio = new Servicio();
            try {
                JSONObject json = servicio.getFavoritePlaceForId(String.valueOf(place_id));

                if (json.getString(KEY_SUCCESS) != null) {
                    String res = json.getString(KEY_SUCCESS);
                    if (Integer.parseInt(res) == 1) {
                        JSONObject json_user = json.getJSONObject("Place1");
                        String namePlace = json_user.getString("Place_Name");
                        PlaceName.setText(namePlace);
                        String direccion = json_user.getString("Desc_Place");
                        mAutocompleteTextView.setText(direccion);
                        titulo_add_place.setText("Ver Sitio");

                        double lat = Double.valueOf(json_user.getString("Latitude"));
                        double lng = Double.valueOf(json_user.getString("Longitude"));

                        markerVer.position(new LatLng(lat, lng));
                        googleMap.addMarker(markerVer);



                        btn_add_favorite_place.setVisibility(View.GONE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (var == 3)
        {
            Servicio servicio = new Servicio();
            try {
                JSONObject json = servicio.getFavoritePlaceForId(String.valueOf(place_id));

                if (json.getString(KEY_SUCCESS) != null) {
                    String res = json.getString(KEY_SUCCESS);
                    if (Integer.parseInt(res) == 1) {
                        JSONObject json_user = json.getJSONObject("Place1");
                        String namePlace = json_user.getString("Place_Name");
                        PlaceName.setText(namePlace);
                        String direccion = json_user.getString("Desc_Place");
                        mAutocompleteTextView.setText(direccion);
                        titulo_add_place.setText("Editar Sitio");

                        double lat = Double.valueOf(json_user.getString("Latitude"));
                        double lng = Double.valueOf(json_user.getString("Longitude"));

                        markerEdt.position(new LatLng(lat, lng));
                        googleMap.addMarker(markerEdt);
                        markerEdt.draggable(true);

                        btn_add_favorite_place.setVisibility(View.GONE);
                        btn_save_favorite_place.setVisibility(View.VISIBLE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        googleMap.setOnMarkerDragListener(this);
*/

        btn_add_favorite_place.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Preferencias preferencias = new Preferencias(getApplicationContext());
                String Client_Id = preferencias.getClient_Id();
                String place_name = PlaceName.getText().toString();

                /*if (position == null)
                {
                    marker123.position(new LatLng(0.0, 0.0));
                    googleMap.addMarker(marker123);
                }*/


                    //latMarkerf = marker123.getPosition().latitude;
                    //lngMarkerf = marker123.getPosition().longitude;

                    if ((place_name.length() > 0) && (latitudeIcon != 0.0) && (longitudeIcon != 0.0)) {
                        Servicio servicio = new Servicio();
                        try {
                            JSONObject json = servicio.setFavoritePlace(Client_Id, place_name,
                                    String.valueOf(latitudeIcon), String.valueOf(longitudeIcon), mAutocompleteTextView.getText().toString());

                            if (json.getString(KEY_SUCCESS) != null) {
                                String res = json.getString(KEY_SUCCESS);
                                if (Integer.parseInt(res) == 1) {

                                    String PlaceId = json.getString("Place_Favorite_Id");
                                    Favorite_PlaceController favorite_placeController  = new Favorite_PlaceController(getApplicationContext());
                                    Favorite_Place favorite_place = new Favorite_Place(null , PlaceId,
                                            PlaceName.getText().toString(), mAutocompleteTextView.getText().toString(),
                                            String.valueOf(latitudeIcon), String.valueOf(longitudeIcon));
                                    favorite_placeController.guardarFavorite_Place(favorite_place);

                                    Intent returnIntent = new Intent();
                                    setResult(Activity.RESULT_OK, returnIntent);

                                    finish();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if ((mAutocompleteTextView.length() > 0) && (place_name.length() > 0)) {
                        Snackbar.make(view, "Parece que no has escrito una direccion valida.", Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(view, "Hay datos vacios.", Snackbar.LENGTH_LONG).show();
                    }






            }
        });

        btn_save_favorite_place.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                Preferencias preferencias = new Preferencias(getApplicationContext());
                String Client_Id = preferencias.getClient_Id();
                String place_name = PlaceName.getText().toString();



                latMarkerf = markerEdt.getPosition().latitude;
                lngMarkerf = markerEdt.getPosition().longitude;

                if ((place_name.length() > 0) && (latMarkerf != 0.0) && (lngMarkerf != 0.0))
                {
                    Servicio servicio = new Servicio();
                    try {
                        JSONObject json = servicio.updateFavoritePlace(Client_Id, place_name,
                                String.valueOf(latMarkerf), String.valueOf(lngMarkerf),
                                mAutocompleteTextView.getText().toString(), String.valueOf(place_id));

                        if (json.getString(KEY_SUCCESS) != null) {
                            String res = json.getString(KEY_SUCCESS);
                            if (Integer.parseInt(res) == 1) {
                                Snackbar.make(view, "Lugar Actualizado Correctamente.", Snackbar.LENGTH_LONG).show();
                                finish();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if ((mAutocompleteTextView.length() > 0) && (place_name.length() > 0))
                {
                    Snackbar.make(view, "Parece que no has escrito una direccion valida.", Snackbar.LENGTH_LONG).show();
                }
                else
                {
                    Snackbar.make(view, "Hay datos vacios.", Snackbar.LENGTH_LONG).show();
                }



            }
        });

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


    @Override
    public void onMarkerDragStart(Marker marker) {
        //Toast.makeText(getApplicationContext(), "Marker " + marker.getId() + " DragStart", Toast.LENGTH_LONG).show();
    }

    public void onMarkerDrag(Marker marker) {
        //Toast.makeText(getApplicationContext(), "Marker " + marker.getId() + " Drag@" + marker.getPosition(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Toast.makeText(getApplicationContext(), "Marker " + marker.getId() + " DragEnd", Toast.LENGTH_LONG).show();


            double la = marker.getPosition().latitude;
            double ln = marker.getPosition().longitude;
            String address = null; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = null;
            String state = null;
            String country = null;

            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(la, ln, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mAutocompleteTextView.setText( address + " " + city + " " + state + " " + country);



    }




   /* public boolean onMarkerClick(final Marker marker) {
        Log.i("GoogleMapActivity", "onMarkerClick");
        double latitude = marker.getPosition().latitude;
        double longitude = marker.getPosition().longitude;

        String address = null;
        String city = null;
        String state = null;
        String country = null;
        String direccion = null;
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();

        } catch (IOException e) {
            e.printStackTrace();
        }

        mAutocompleteTextView.setText(address + " " + city + " " + state + " " + country + " " + direccion);
        return false;
    }*/

    public void cargarDatosPlacesVisitadas(String Client_Id)
    {
        Servicio servicio = new Servicio();
        final JSONObject json = servicio.getPlaceHistory(Client_Id);
        try {
            codes = new String[Integer.valueOf(json.getInt("num"))];
            if (json.getString(KEY_SUCCESS) != null) {
                String res = json.getString(KEY_SUCCESS);
                if (Integer.parseInt(res) == 1)
                {
                    for (int i = 0; i < codes.length; i++)
                    {
                        JSONObject json_user = json.getJSONObject("Place"+(i+1));

                        latitude1 = json_user.getString("Latitude");
                        longitude1 = json_user.getString("Longitude");

                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.valueOf(latitude1), Double.valueOf(longitude1)))
                                .title("Aqui Estuve")
                                .snippet(json_user.getString("Date"))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);

        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            mNameTextView.setText(Html.fromHtml(place.getName() + ""));
            mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
            mIdTextView.setText(Html.fromHtml(place.getId() + ""));
            mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
            mWebTextView.setText(place.getWebsiteUri() + "");


            if (status.equals("agregar"))
            {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(place.getLatLng().latitude
                        , place.getLatLng().longitude)).zoom(16).build();
                mapa1.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }


            /*if (var == 3)
            {
                position = place.getLatLng();

                googleMap.clear();
                markerEdt.draggable(true);
                markerEdt.position(position);
                googleMap.addMarker(markerEdt);


                latMarkerf = markerEdt.getPosition().latitude;
                lngMarkerf = markerEdt.getPosition().longitude;
            }
            else
            {
                position = place.getLatLng();

                googleMap.clear();
                marker123.title("Aqio");
                marker123.draggable(true);
                marker123.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                marker123.position(position);
                googleMap.addMarker(marker123);


                latMarkerf = marker123.getPosition().latitude;
                lngMarkerf = marker123.getPosition().longitude;
            }
*/



            if (attributions != null) {
                mAttTextView.setText(Html.fromHtml(attributions.toString()));
            }
        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }
}