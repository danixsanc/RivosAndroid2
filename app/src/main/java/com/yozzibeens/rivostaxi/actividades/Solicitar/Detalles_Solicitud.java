package com.yozzibeens.rivostaxi.actividades.Solicitar;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yozzibeens.rivostaxi.R;
import com.yozzibeens.rivostaxi.actividades.Perfil.Data_In;
import com.yozzibeens.rivostaxi.utilerias.Servicio;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;



/**
 * Created by danixsanc on 03/11/2015.
 */
public class Detalles_Solicitud extends AppCompatActivity {

    private static String KEY_SUCCESS = "Success";
    int pricef;
    int price_id;

    TextView cabbie_id;
    TextView price;
    String address;
    String city;
    String state;
    String country;;

    TextView txt_Inicio;
    TextView txt_Destino;

    double longitudeInicio;
    double latitudeInicio;
    double longitudeIcono;
    double latitudeIcono;
    String direccionIcono;
    String direccionf;

    Button buyItBtn;
   private Activity activity = this;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalles_solicitud);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //----Tipo de Fuente-----------------------------------------------------------------------------------
        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");

        cabbie_id = (TextView) findViewById(R.id.cabbie_id);
        cabbie_id.setTypeface(RobotoCondensed_Regular);
        txt_Inicio = (TextView) findViewById(R.id.txtInicio);
        txt_Inicio.setTypeface(RobotoCondensed_Regular);
        txt_Destino = (TextView) findViewById(R.id.txtDestino);
        txt_Destino.setTypeface(RobotoCondensed_Regular);
        price = (TextView) findViewById(R.id.price);
        price.setTypeface(RobotoCondensed_Regular);
        buyItBtn = (Button) findViewById(R.id.buyItBtn);
        buyItBtn.setTypeface(RobotoCondensed_Regular);
        //----------------------------------------------------------------------------------------------------

        buyItBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(Detalles_Solicitud.this , Form.class);

                i.putExtra("latautc_inicio", latitudeInicio);
                i.putExtra("lngautc_inicio", longitudeInicio);
                i.putExtra("latautc_final", longitudeIcono);
                i.putExtra("lngautc_final", latitudeIcono);
                i.putExtra("direccion", direccionIcono);
                i.putExtra("price_Id", price_id);
                i.putExtra("Price", pricef);

                startActivity(i);
                finish();
            }
        });


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            latitudeInicio = bundle.getDouble("Lat");
            longitudeInicio = bundle.getDouble("Long");
            longitudeIcono = bundle.getDouble("Lat_icon");
            latitudeIcono = bundle.getDouble("Long_icon");
            direccionIcono = bundle.getString("direccion");

            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitudeInicio, longitudeInicio, 1);
                address = addresses.get(0).getAddressLine(0);
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                direccionf = address;
            } catch (IOException e) {
                e.printStackTrace();
            }

            cabbie_id.setText("Taxista: Por Asignar");
            txt_Inicio.setText(direccionf);
            txt_Destino.setText(direccionIcono);


            Servicio servicio = new Servicio();
            final JSONObject json = servicio.getPriceOfTravel(longitudeIcono, latitudeIcono);

            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    String res = json.getString(KEY_SUCCESS);
                    if (Integer.parseInt(res) == 1) {
                        pricef = Integer.valueOf(json.getString("Price"));
                        price_id = Integer.valueOf(json.getString("Price_Id"));
                        price.setText("$ " + String.valueOf(pricef)+".00");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
}
