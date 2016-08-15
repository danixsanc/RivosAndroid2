package com.YozziBeens.rivostaxi.actividades.Historial;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;


import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.controlador.HistorialController;
import com.YozziBeens.rivostaxi.modelo.Historial;
import com.YozziBeens.rivostaxi.utilerias.FechasBD;

public class Historial_Detalle extends AppCompatActivity {

    TextView txtcode,txt_code;
    TextView txtdate,txt_fecha;
    TextView txtplace_in;
    TextView txtplace_fn;
    TextView txtcab_name,txtTaxista;
    TextView txttime,txt_hora, precio, PrecioH, InicioH, DestinoH;

    String code;
    String date;
    String lat_in;
    String lon_in;
    String lat_fn;
    String lon_fn;
    String cab_name;
    String pricef;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historial_detalle);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String request_id = bundle.getString("request_id");


        HistorialController historialController = new HistorialController(getApplicationContext());
        Historial historial = historialController.obtenerHistorialPorRequestId(request_id);

        code = historial.getRef();
        date = historial.getDate();
        lat_in = historial.getInicio();
        lat_fn = historial.getDestino();
        cab_name = historial.getCabbie_Name();
        pricef = historial.getPrice();



        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(this.getAssets(), "RobotoCondensed-Regular.ttf");



        FechasBD fechasBD = new FechasBD();
        String fecha = fechasBD.ObtenerFecha(date);
        String hora = fechasBD.ObtenerHora(date);

        PrecioH = (TextView) findViewById(R.id.PrecioH);


        PrecioH.setTypeface(RobotoCondensed_Regular);

        txt_code = (TextView) findViewById(R.id.txt_code);
        txt_code.setTypeface(RobotoCondensed_Regular);

        txtcode = (TextView) findViewById(R.id.code);
        txtcode.setText(code);
        txtcode.setTypeface(RobotoCondensed_Regular);

        txt_fecha = (TextView) findViewById(R.id.txt_fecha);
        txt_fecha.setTypeface(RobotoCondensed_Regular);

        txtdate = (TextView) findViewById(R.id.date);
        txtdate.setText(fecha);
        txtdate.setTypeface(RobotoCondensed_Regular);

        txt_hora = (TextView) findViewById(R.id.txt_hora);
        txt_hora.setTypeface(RobotoCondensed_Regular);

        txttime = (TextView) findViewById(R.id.time);
        txttime.setText(hora);
        txttime.setTypeface(RobotoCondensed_Regular);


        txtplace_in = (TextView) findViewById(R.id.place_in);
        txtplace_in.setText(lat_in);
        txtplace_in.setTypeface(RobotoCondensed_Regular);

        txtplace_fn = (TextView) findViewById(R.id.place_fn);
        txtplace_fn.setText(lat_fn);
        txtplace_fn.setTypeface(RobotoCondensed_Regular);

        txtTaxista = (TextView) findViewById(R.id.txtTaxista);
        txtTaxista.setTypeface(RobotoCondensed_Regular);

        txtcab_name = (TextView) findViewById(R.id.cabbie);
        txtcab_name.setText(cab_name);
        txtcab_name.setTypeface(RobotoCondensed_Regular);

        precio = (TextView) findViewById(R.id.Price);
        precio.setText("$" + historial.getPrice()+".00");
        precio.setTypeface(RobotoCondensed_Regular);
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
