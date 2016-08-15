package com.YozziBeens.rivostaxi.actividades.Solicitar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.YozziBeens.rivostaxi.adaptadores.AddFavoriteCabbie;
import com.YozziBeens.rivostaxi.app.Main;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.modelosApp.TaxistasCercanos;
import com.YozziBeens.rivostaxi.modelosApp.TaxistasQueAtendieron;
import com.YozziBeens.rivostaxi.respuesta.ResultadoLogin;
import com.YozziBeens.rivostaxi.respuesta.ResultadoNotificacion;
import com.YozziBeens.rivostaxi.respuesta.ResultadoTaxistasCercanos;
import com.YozziBeens.rivostaxi.respuesta.ResultadoToken;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudNotificacion;
import com.YozziBeens.rivostaxi.solicitud.SolicitudObtenerTaxistasCercanos;
import com.YozziBeens.rivostaxi.solicitud.SolicitudRegistro;
import com.YozziBeens.rivostaxi.utilerias.FechasBD;
import com.google.android.gms.maps.model.LatLng;
import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.YozziBeens.rivostaxi.utilerias.Servicio;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Created by danixsanc on 30/12/2015.
 */
public class Compra_Final extends AppCompatActivity {

    TextView NombreTaxista,txt_taxista;
    TextView Fecha,txt_fecha;
    TextView Referencia,txt_referencia;
    TextView Distancia,txt_distancia;
    TextView Tiempo;
    TextView Costo;
    TextView txt_llegaPorTiEn,txt_aproximadamente,txt_aproximadamente2,txt_costo_delViaje;
    Button btn_perfil_cabbie;
    String fecha, referecia;

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
    ImageView imgQrCode;


    String id_cabbie;
    String name_cabbie;
    String gcm_id_cabbie;
    String latitude_cabbie;
    String longitude_cabbie;

    ImageView qrCodeImageview;
    String QRcode;
    public final static int WIDTH = 500;

    String date;
    String ref;
    private Gson gson;
    private ResultadoNotificacion resultadoNotificacion;

    double metros;
    double kilometros;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compra_final);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.gson = new Gson();

        Preferencias preferencias = new Preferencias(getApplicationContext());
        String Client_Id = preferencias.getClient_Id();

       //------Tipo de fuente---------------------------------------------------------------------------------
        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");






        NombreTaxista = (TextView) findViewById(R.id.NombreTaxista);
        NombreTaxista.setTypeface(RobotoCondensed_Regular);
        Fecha = (TextView) findViewById(R.id.Fecha);
        Fecha.setTypeface(RobotoCondensed_Regular);
        txt_fecha = (TextView) findViewById(R.id.txt_fecha);
        txt_fecha.setTypeface(RobotoCondensed_Regular);
        Referencia = (TextView) findViewById(R.id.Referencia);
        Referencia.setTypeface(RobotoCondensed_Regular);
        txt_referencia = (TextView) findViewById(R.id.txt_referencia);
        txt_referencia.setTypeface(RobotoCondensed_Regular);
        Distancia = (TextView) findViewById(R.id.Distancia);
        Distancia.setTypeface(RobotoCondensed_Regular);
        txt_distancia = (TextView) findViewById(R.id.txt_distancia);
        txt_distancia.setTypeface(RobotoCondensed_Regular);
        txt_taxista = (TextView) findViewById(R.id.txt_taxista);
        txt_taxista.setTypeface(RobotoCondensed_Regular);
        txt_aproximadamente = (TextView) findViewById(R.id.txt_aproximadamente);
        txt_aproximadamente.setTypeface(RobotoCondensed_Regular);
        txt_llegaPorTiEn = (TextView) findViewById(R.id.txt_llegaPorTiEn);
        txt_llegaPorTiEn.setTypeface(RobotoCondensed_Regular);
        Tiempo = (TextView) findViewById(R.id.Tiempo);
        Tiempo.setTypeface(RobotoCondensed_Regular);
        txt_aproximadamente2 = (TextView) findViewById(R.id.txt_aproximadamente2);
        txt_aproximadamente2.setTypeface(RobotoCondensed_Regular);
        txt_costo_delViaje = (TextView) findViewById(R.id.txt_costo_delViaje);
        txt_costo_delViaje.setTypeface(RobotoCondensed_Regular);
        Costo = (TextView) findViewById(R.id.Costo);
        Costo.setTypeface(RobotoCondensed_Regular);
        btn_perfil_cabbie = (Button) findViewById(R.id.btn_perfil_cabbie);
        btn_perfil_cabbie.setTypeface(RobotoCondensed_Regular);
        //--------------------------------------------------------------------------------------------------



        Bundle   bundle = getIntent().getExtras();
        if (bundle != null) {
            latautc_inicio = bundle.getDouble("latautc_inicio");
            lngautc_inicio = bundle.getDouble("lngautc_inicio");
            latautc_final = bundle.getDouble("latautc_final");
            lngautc_final = bundle.getDouble("lngautc_final");
            direccion = bundle.getString("direccion");
            price = bundle.getInt("Price");
            price_Id = bundle.getInt("price_Id");

            id_cabbie = bundle.getString("id_cabbie");
            name_cabbie = bundle.getString("name_cabbie");
            gcm_id_cabbie = bundle.getString("gcm_id_cabbie");
            latitude_cabbie = bundle.getString("latitude_cabbie");
            longitude_cabbie = bundle.getString("longitude_cabbie");

            date = bundle.getString("date");
            ref = bundle.getString("ref");
        }

        NombreTaxista.setText(name_cabbie);


        LatLng l1 = new LatLng(latautc_inicio, lngautc_inicio);
        LatLng l2 = new LatLng(Double.valueOf(latitude_cabbie), Double.valueOf(longitude_cabbie));

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


        /*JSONObject json2 = servicio.set_Client_History(String.valueOf(latautc_inicio), String.valueOf(lngautc_inicio), String.valueOf(latautc_final),
                String.valueOf(lngautc_final), Client_Id, cabbie_id, String.valueOf(price_Id));

        try {
            if (json2.getString(KEY_SUCCESS) != null) {
                String res = json.getString(KEY_SUCCESS);
                if (Integer.parseInt(res) == 1) {
                    fecha = json2.getString("Date");
                    referecia = json2.getString("Ref");


                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/


        FechasBD fechasBD = new FechasBD();
        String fechaf = fechasBD.ObtenerFecha(date);
        Fecha.setText(fechaf);

        Referencia.setText(ref);


        qrCodeImageview=(ImageView) findViewById(R.id.imgQrCode);
        Thread t = new Thread(new Runnable() {
            public void run() {
                QRcode=ref;

                try {
                    synchronized (this) {
                        wait(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Bitmap bitmap = null;
                                    bitmap = encodeAsBitmap(QRcode);
                                    qrCodeImageview.setImageBitmap(bitmap);
                                } catch (WriterException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();



        SolicitudNotificacion oData = new SolicitudNotificacion();
        oData.setGcm_Id(gcm_id_cabbie);
        oData.setMessage("Nueva Solicitud");
        oData.setType("A");
        NotificacionWebService(gson.toJson(oData));
    }

    private Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? getResources().getColor(R.color.colorPrimaryDark):getResources().getColor(R.color.colorWhite);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);
        return bitmap;
    }

    private void NotificacionWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.NotificationWebService, rawJson);
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
                try {
                    int statusCode = Integer.parseInt(result.get("StatusCode").toString());
                    if (statusCode == 0) {
                        resultadoNotificacion = gson.fromJson(result.get("Resultado").toString(), ResultadoNotificacion.class);
                        if (resultadoNotificacion.getSuccess() == 1 ) {

                        }
                    }
                }
                catch (Exception error) {

                }
            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
            }
        });
        servicioAsyncService.execute();
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
