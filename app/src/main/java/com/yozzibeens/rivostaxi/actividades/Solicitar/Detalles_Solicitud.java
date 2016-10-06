package com.YozziBeens.rivostaxi.actividades.Solicitar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.modelosApp.Solicitud;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudCancelCabbie;
import com.google.gson.Gson;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.Serializable;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by danixsanc on 03/11/2015.
 */
public class Detalles_Solicitud extends AppCompatActivity {


    private Button btnCancelarPago, btnPagar;
    private TextView cabbie_name, price, time, txt_Inicio, txt_Destino;
    private boolean canRequest = false;
    private Gson gson;
    private Typeface Roboto;
    private Solicitud solicitud;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalles_solicitud);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.gson = new Gson();
        this.Roboto = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");
        this.cabbie_name = (TextView) findViewById(R.id.cabbie_name);
        this.cabbie_name.setTypeface(Roboto);
        this.txt_Inicio = (TextView) findViewById(R.id.txtInicio);
        this.txt_Inicio.setTypeface(Roboto);
        this.txt_Destino = (TextView) findViewById(R.id.txtDestino);
        this.txt_Destino.setTypeface(Roboto);
        this.price = (TextView) findViewById(R.id.price);
        this.time = (TextView) findViewById(R.id.time);
        this.solicitud = new Solicitud();
        this.btnPagar = (Button) findViewById(R.id.btn_pagar);
        this.btnCancelarPago = (Button) findViewById(R.id.btnCancelarPago);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            solicitud = (Solicitud) bundle.getSerializable("Solicitud");
            price.setText(solicitud.getPrice().toString());
            cabbie_name.setText(solicitud.getCabbie().toString());
            txt_Inicio.setText(solicitud.getDirOrigen().toString());
            txt_Destino.setText(solicitud.getDirDestino().toString());
        }

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                time.setText(""+millisUntilFinished / 1000);
                canRequest = true;
            }

            public void onFinish() {
                time.setText("Tiempo de espera agotado!");
                SolicitudCancelCabbie oData = new SolicitudCancelCabbie();
                oData.setCabbie_Id(solicitud.getCabbie_Id());
                CancelCabbieWebService(gson.toJson(oData));
                canRequest = false;
            }
        }.start();


        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (canRequest)
                {
                    DialogPlus dialog = DialogPlus.newDialog(Detalles_Solicitud.this)
                            .setExpanded(true)
                            .setContentHolder(new ViewHolder(R.layout.tipo_pago))
                            .create();

                    Button btnPagarConTarjeta = (Button) dialog.getHolderView().findViewById(R.id.btn_pagar_con_tarjeta);
                    Button btnPagarAlTaxista = (Button) dialog.getHolderView().findViewById(R.id.btn_pagar_al_taxista);
                    btnPagarConTarjeta.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Detalles_Solicitud.this, Form.class);
                            intent.putExtra("tipo", "T");
                            solicitud.setTimeRest(time.getText().toString());
                            intent.putExtra("Solicitud",  solicitud);
                            startActivity(intent);
                        }
                    });
                    btnPagarAlTaxista.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Detalles_Solicitud.this, Form.class);
                            intent.putExtra("tipo", "P");
                            solicitud.setTimeRest(time.getText().toString());
                            intent.putExtra("Solicitud", solicitud);
                            startActivity(intent);
                        }
                    });
                    dialog.show();
                }
                else {
                    new SweetAlertDialog(Detalles_Solicitud.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("El tiempo se ha agotado, regresa a la seccion anterior!")
                            .setConfirmText("Entendido")
                            .show();
                }

            }
        });


        btnCancelarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                SolicitudCancelCabbie oData = new SolicitudCancelCabbie();
                oData.setCabbie_Id(solicitud.getCabbie_Id());
                CancelCabbieWebService(gson.toJson(oData));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void CancelCabbieWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.CancelCabbieWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
            }

            @Override
            public void onTaskDownloadedFinished(HashMap<String, Object> result) {
            }

            @Override
            public void onTaskUpdate(String result) {
            }

            @Override
            public void onTaskComplete(HashMap<String, Object> result) {
            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
            }
        });
        servicioAsyncService.execute();
    }
}
