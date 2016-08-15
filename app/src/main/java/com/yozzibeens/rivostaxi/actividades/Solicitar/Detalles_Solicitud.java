package com.YozziBeens.rivostaxi.actividades.Solicitar;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.utilerias.Servicio;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;



/**
 * Created by danixsanc on 03/11/2015.
 */
public class Detalles_Solicitud extends AppCompatActivity {


    TextView cabbie_id;
    TextView price;
    String address;
    String city;
    String state;
    String country;

    TextView txt_Inicio;
    TextView txt_Destino;




    double latOrigen;
    double longOrigen;
    double latDestino;
    double longDestino;
    String dirOrigen;
    String dirDestino;
    int price_Id2;
    int pricef2;

    Button btnCancelarPago;
    Button btn123;

    //Button buyItBtn;
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
        //buyItBtn = (Button) findViewById(R.id.buyItBtn);
        //buyItBtn.setTypeface(RobotoCondensed_Regular);
        //----------------------------------------------------------------------------------------------------

        btn123 = (Button) findViewById(R.id.btn123);
        btn123.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialog = DialogPlus.newDialog(Detalles_Solicitud.this)
                        .setExpanded(true)
                        .setContentHolder(new ViewHolder(R.layout.tipo_pago))
                        .create();

                Button button = (Button) dialog.getHolderView().findViewById(R.id.button1234);
                Button button2 = (Button) dialog.getHolderView().findViewById(R.id.button2123);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(), "Pago con tarjeta", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Detalles_Solicitud.this, Form.class);
                        intent.putExtra("tipo", "T");

                        intent.putExtra("latautc_inicio", latOrigen);
                        intent.putExtra("lngautc_inicio", longOrigen);
                        intent.putExtra("latautc_final", latDestino);
                        intent.putExtra("lngautc_final", longDestino);
                        intent.putExtra("Price", pricef2);
                        intent.putExtra("price_Id", price_Id2);
                        intent.putExtra("inicio", dirOrigen);
                        intent.putExtra("destino", dirDestino);


                        startActivity(intent);
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(), "Pago al taxista", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Detalles_Solicitud.this, Form.class);
                        intent.putExtra("tipo", "P");

                        intent.putExtra("latautc_inicio", latOrigen);
                        intent.putExtra("lngautc_inicio", longOrigen);
                        intent.putExtra("latautc_final", latDestino);
                        intent.putExtra("lngautc_final", longDestino);
                        intent.putExtra("Price", pricef2);
                        intent.putExtra("price_Id", price_Id2);
                        intent.putExtra("inicio", dirOrigen);
                        intent.putExtra("destino", dirDestino);

                        startActivity(intent);
                    }
                });
                dialog.show();
            }
        });


        btnCancelarPago = (Button) findViewById(R.id.btnCancelarPago);
        btnCancelarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*buyItBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(Detalles_Solicitud.this , Form.class);

                i.putExtra("latautc_inicio", latOrigen);
                i.putExtra("lngautc_inicio", longOrigen);
                i.putExtra("latautc_final", latDestino);
                i.putExtra("lngautc_final", longDestino);
                //i.putExtra("direccion", direccionIcono);
                i.putExtra("Price", pricef2);
                i.putExtra("price_Id", price_Id2);
                i.putExtra("inicio", dirOrigen);
                i.putExtra("destino", dirDestino);
                startActivity(i);
                finish();
            }
        });*/



        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            latOrigen = bundle.getDouble("latOrigen");
            longOrigen = bundle.getDouble("longOrigen");
            latDestino = bundle.getDouble("latDestino");
            longDestino = bundle.getDouble("longDestino");
            dirOrigen = bundle.getString("dirOrigen");
            dirDestino= bundle.getString("dirDestino");
            price_Id2 = bundle.getInt("price_Id");
            pricef2 = bundle.getInt("Price");

            /*Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitudeInicio, longitudeInicio, 1);
                address = addresses.get(0).getAddressLine(0);
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                direccionf = address + " " + city + " " + state;
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            cabbie_id.setText("Taxista: Por Asignar");
            txt_Inicio.setText(dirOrigen);
            txt_Destino.setText(dirDestino);

            price.setText("$ " + String.valueOf(pricef2)+".00");

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
