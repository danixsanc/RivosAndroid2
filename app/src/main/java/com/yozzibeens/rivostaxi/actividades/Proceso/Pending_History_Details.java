package com.YozziBeens.rivostaxi.actividades.Proceso;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;


import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.utilerias.FechasBD;
import com.YozziBeens.rivostaxi.utilerias.Servicio;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by danixsanc on 04/01/2016.
 */
public class Pending_History_Details extends AppCompatActivity {

    String request_id;
    TextView txtFecha,Fecha;
    TextView txtId,txtReferencia;
    TextView txtNombre,txtTaxista;
    TextView txtPrecio,Precio;
    ImageButton back_button;
    //ImageView statusImg;
    private static String KEY_SUCCESS = "Success";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_history_details);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            request_id = bundle.getString("Request_Id");
        }

        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");

        txtId = (TextView) findViewById(R.id.txtid);
        txtId.setTypeface(RobotoCondensed_Regular);
        txtReferencia = (TextView) findViewById(R.id.txtReferencia);
        txtReferencia.setTypeface(RobotoCondensed_Regular);
        txtFecha = (TextView) findViewById(R.id.txtFecha);
        Fecha = (TextView) findViewById(R.id.Fecha);
        Fecha.setTypeface(RobotoCondensed_Regular);
        txtNombre = (TextView) findViewById(R.id.txtNombreTaxista);
        txtNombre.setTypeface(RobotoCondensed_Regular);
        txtTaxista = (TextView) findViewById(R.id.txtTaxista);
        txtTaxista.setTypeface(RobotoCondensed_Regular);
        txtPrecio = (TextView) findViewById(R.id.txtPrecio);
        txtPrecio.setTypeface(RobotoCondensed_Regular);
        Precio = (TextView) findViewById(R.id.Precio);
        Precio.setTypeface(RobotoCondensed_Regular);
        //statusImg = (ImageView) findViewById(R.id.status);


        Servicio servicio = new Servicio();
        JSONObject json = servicio.getRequestForId(request_id);
        try {

            if (json.getString(KEY_SUCCESS) != null) {
                String res = json.getString(KEY_SUCCESS);
                if (Integer.parseInt(res) == 1)
                {
                    txtNombre.setText(json.getString("Name"));
                    txtId.setText(request_id);

                    String date = json.getString("Date");
                    FechasBD fechasBD = new FechasBD();
                    String fecha = fechasBD.ObtenerFecha(date);
                    txtFecha.setText(fecha);



                    txtPrecio.setText(json.getString("Price"));
                    int status = json.getInt("Status");


                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
