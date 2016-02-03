package com.yozzibeens.rivostaxi.actividades.Solicitar;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import com.yozzibeens.rivostaxi.R;
import com.yozzibeens.rivostaxi.utilerias.Preferencias;
import com.yozzibeens.rivostaxi.utilerias.Servicio;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;



import com.google.maps.android.SphericalUtil;

/**
 * Created by danixsanc on 30/12/2015.
 */
public class Compra_Final extends AppCompatActivity {
    private static String KEY_SUCCESS = "Success";


    TextView NombreTaxista;
    TextView Fecha;
    TextView Referencia;
    TextView Distancia;
    TextView Tiempo;
    TextView Costo;

    double latautc_inicio;
    double lngautc_inicio;
    double latautc_final;
    double lngautc_final;
    double latcabbie;
    double lngcabbie;
    int price_id;
    int price, price_Id;
    double pricef;
    String cabbie_id;
    String direccion;

    double metros;
    double kilometros;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compra_final);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Preferencias preferencias = new Preferencias(getApplicationContext());
        String Client_Id = preferencias.getClient_Id();

        NombreTaxista = (TextView) findViewById(R.id.NombreTaxista);
        Fecha = (TextView) findViewById(R.id.Fecha);
        Referencia = (TextView) findViewById(R.id.Referencia);
        Distancia = (TextView) findViewById(R.id.Distancia);
        Tiempo = (TextView) findViewById(R.id.Tiempo);
        Costo = (TextView) findViewById(R.id.Costo);




        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            latautc_inicio = bundle.getDouble("latautc_inicio");
            lngautc_inicio = bundle.getDouble("lngautc_inicio");
            latautc_final = bundle.getDouble("latautc_final");
            lngautc_final = bundle.getDouble("lngautc_final");
            direccion = bundle.getString("direccion");
            price = bundle.getInt("Price");
            price_Id = bundle.getInt("price_Id");
        }


        Servicio servicio = new Servicio();
        final JSONObject json = servicio.GetCloseCabbie(latautc_inicio, lngautc_inicio);

        try {
            if (json.getString(KEY_SUCCESS) != null) {
                String res = json.getString(KEY_SUCCESS);
                if (Integer.parseInt(res) == 1)
                {
                    JSONObject json_user = json.getJSONObject("Cabbie1");
                    String reg_id = json_user.getString("gcm_Id");
                    String nombre = json_user.getString("Name");
                    servicio.sendNotification(reg_id);
                    cabbie_id = json_user.getString("Cabbie_Id");

                    latcabbie = Double.valueOf(json_user.getString("Latitude"));
                    lngcabbie = Double.valueOf(json_user.getString("Longitude"));
                    NombreTaxista.setText(nombre);
                }
                else if (Integer.parseInt(res) == 0)
                {
                    //se registra con -1
                    servicio.set_Client_History_Pending(String.valueOf(latautc_inicio), String.valueOf(lngautc_inicio), String.valueOf(latautc_final),
                            String.valueOf(lngautc_final), Client_Id, String.valueOf(price_Id));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LatLng l1 = new LatLng(latautc_inicio, lngautc_inicio);
        LatLng l2 = new LatLng(latcabbie, lngcabbie);

        double distance = SphericalUtil.computeDistanceBetween(l1, l2);
        Distancia.setText(formatNumber(distance));

        Costo.setText("$ "+String.valueOf(price)+".00");


        if (metros > 0)
        {
            Tiempo.setText("2 MIN");
        }
        else if (kilometros > 0)
        {
            double tiempo = kilometros / 35;
            tiempo = tiempo * 60;
            Tiempo.setText((int)tiempo + " MIN");
        }


        servicio.set_Client_History(String.valueOf(latautc_inicio), String.valueOf(lngautc_inicio), String.valueOf(latautc_final),
                String.valueOf(lngautc_final), Client_Id, cabbie_id, String.valueOf(price_Id));



    }


    private String formatNumber(double distance) {
        String unit = "M";
        metros = distance;
        kilometros = 0;
        if (distance < 1) {
            distance *= 1000;
            unit = "MM";
        } else if (distance > 1000) {
            distance /= 1000;
            unit = "KM";
            metros = 0;
            kilometros = distance;
        }
        return String.format("%4.0f%s", distance, unit);
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
