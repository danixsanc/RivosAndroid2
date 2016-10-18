package com.YozziBeens.rivostaxi.actividades.Solicitar;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.controlador.TarjetaController;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.modelo.Tarjeta;
import com.YozziBeens.rivostaxi.modelosApp.Solicitud;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudCancelCabbie;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import java.util.HashMap;
import java.util.List;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by danixsanc on 03/11/2015.
 */
public class Detalles_Solicitud extends AppCompatActivity {


    private Button btnPagar;
    private TextView cabbie_name, price, time, txt_Inicio, txt_Destino, passenger, model, brand;
    private boolean canRequest = false;
    private Gson gson;
    private Typeface Roboto;
    private Solicitud solicitud;
    private CircleImageView imageCabbie;
    private TextView timeCabbie;

    double metros;
    double kilometros;


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
        this.brand = (TextView) findViewById(R.id.brand);
        this.model = (TextView) findViewById(R.id.model);
        this.passenger = (TextView) findViewById(R.id.passengers);
        this.imageCabbie = (CircleImageView) findViewById(R.id.imageCabbie);
        this.timeCabbie = (TextView) findViewById(R.id.timeCabbie);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            solicitud = (Solicitud) bundle.getSerializable("Solicitud");
            price.setText("$ " + solicitud.getPrice().toString() + ".00");
            cabbie_name.setText(solicitud.getCabbie().toString());
            txt_Inicio.setText(solicitud.getDirOrigen().toString());
            txt_Destino.setText(solicitud.getDirDestino().toString());
            brand.setText(solicitud.getBrand().toString());
            model.setText(solicitud.getModel().toString());
            passenger.setText(solicitud.getPassengers().toString() + " Pasajeros");


            String base = solicitud.getImage();

            byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);

            imageCabbie.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

            LatLng l1 = new LatLng(Double.valueOf(solicitud.getLatOrigen()), Double.valueOf(solicitud.getLongOrigen()));
            LatLng l2 = new LatLng(Double.valueOf(solicitud.getLatCabbie()), Double.valueOf(solicitud.getLongCabbie()));

            double distance = SphericalUtil.computeDistanceBetween(l1, l2);
            formatNumber(distance);
            if (metros > 0)
            {
                timeCabbie.setText("2 MIN");
            }
            else if (kilometros > 0)
            {
                double tiempo = kilometros / 35;
                tiempo = tiempo * 60;
                timeCabbie.setText((int)tiempo + " MIN");

            }


        }

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                time.setText(""+millisUntilFinished / 1000);
                canRequest = true;
            }

            public void onFinish() {
                time.setText("00");
                time.setTextColor(Color.RED);
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

                            TarjetaController tarjetaController = new TarjetaController(getApplicationContext());
                            List<Tarjeta> list = tarjetaController.obtenerTarjeta();
                            if (list.size() > 0){
                                Intent intent = new Intent(Detalles_Solicitud.this, Form.class);
                                intent.putExtra("tipo", "T");
                                solicitud.setTimeRest(time.getText().toString());
                                intent.putExtra("Solicitud",  solicitud);
                                startActivity(intent);
                            }else {
                                new SweetAlertDialog(Detalles_Solicitud.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("No titenes tarjetas registradas, ve a la seccion de pagos" +
                                                "para registrar tus tarjetas.")
                                        .setConfirmText("Entendido")
                                        .show();
                            }
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
