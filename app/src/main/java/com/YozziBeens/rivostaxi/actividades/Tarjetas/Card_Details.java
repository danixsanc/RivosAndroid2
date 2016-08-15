package com.YozziBeens.rivostaxi.actividades.Tarjetas;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.controlador.TarjetaController;
import com.YozziBeens.rivostaxi.modelo.Tarjeta;
import com.YozziBeens.rivostaxi.utilerias.TarjetasBD;

/**
 * Created by danixsanc on 04/01/2016.
 */
public class Card_Details extends AppCompatActivity {

    String request_id;
    TextView txtFecha,Fecha;
    TextView txtId,txtReferencia;
    TextView txtNombre,txtTaxista;
    TextView txtPrecio,Precio;
    ImageButton back_button;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_details);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            request_id = bundle.getString("Card_Id");
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


        //HistorialPendienteController historialPendienteController = new HistorialPendienteController(this);
        //HistorialPendiente historialPendiente = historialPendienteController.obtenerHistorialPendientePorRequestId(Long.valueOf(request_id));

        TarjetaController tarjetaController = new TarjetaController(this);
        Tarjeta tarjeta = tarjetaController.obtenerTarjetaPorTarjetaId(Long.valueOf(request_id));


        TarjetasBD tarjetasBD = new TarjetasBD();
        String tarjF = tarjetasBD.ocultarTarjeta(tarjeta.getNumber_Card());
        txtId.setText(tarjF);

        String date = tarjeta.getMonth();
        txtNombre.setText(date);
        txtFecha.setText(tarjeta.getName_Card());

        txtPrecio.setText(tarjeta.getYear());

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
