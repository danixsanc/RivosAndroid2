package com.YozziBeens.rivostaxi.actividades.Proceso;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.controlador.HistorialPendienteController;
import com.YozziBeens.rivostaxi.modelo.HistorialPendiente;
import com.YozziBeens.rivostaxi.utilerias.FechasBD;
import com.YozziBeens.rivostaxi.utilerias.Servicio;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by danixsanc on 04/01/2016.
 */
public class Pending_History_Details extends AppCompatActivity {

    String request_id;
    /*TextView txtFecha,Fecha;
    TextView txtId,txtReferencia;
    TextView txtNombre;
    TextView txtPrecio,Precio;*/

    TextView place_in;
    TextView place_fn;
    TextView cabbie;
    TextView txtTaxista;
    TextView Price;
    TextView PrecioH;
    TextView date;
    TextView txt_fecha;
    TextView time;
    TextView txt_hora;
    TextView code;
    TextView txt_code;

    ImageView qrCodeImageview;
    String QRcode;
    public final static int WIDTH = 500;
    String ref;

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

        place_in = (TextView) findViewById(R.id.place_in);
        place_fn = (TextView) findViewById(R.id.place_fn);
        cabbie = (TextView) findViewById(R.id.cabbie);
        txtTaxista = (TextView) findViewById(R.id.txtTaxista);
        Price = (TextView) findViewById(R.id.Price);
        PrecioH = (TextView) findViewById(R.id.PrecioH);
        date = (TextView) findViewById(R.id.date);
        txt_fecha = (TextView) findViewById(R.id.txt_fecha);
        time = (TextView) findViewById(R.id.time);
        txt_hora = (TextView) findViewById(R.id.txt_hora);
        code = (TextView) findViewById(R.id.code);
        txt_code = (TextView) findViewById(R.id.txt_code);

        place_in.setTypeface(RobotoCondensed_Regular);
        place_fn.setTypeface(RobotoCondensed_Regular);
        cabbie.setTypeface(RobotoCondensed_Regular);
        txtTaxista.setTypeface(RobotoCondensed_Regular);
        Price.setTypeface(RobotoCondensed_Regular);
        PrecioH.setTypeface(RobotoCondensed_Regular);
        date.setTypeface(RobotoCondensed_Regular);
        txt_fecha.setTypeface(RobotoCondensed_Regular);
        time.setTypeface(RobotoCondensed_Regular);
        txt_hora.setTypeface(RobotoCondensed_Regular);
        code.setTypeface(RobotoCondensed_Regular);
        txt_code.setTypeface(RobotoCondensed_Regular);


        HistorialPendienteController historialPendienteController = new HistorialPendienteController(this);
        HistorialPendiente historialPendiente = historialPendienteController.obtenerHistorialPendientePorRequestId(Long.valueOf(request_id));


        place_in.setText(historialPendiente.getInicio());
        place_fn.setText(historialPendiente.getDestino());
        cabbie.setText(historialPendiente.getCabbie_Name());
        Price.setText("$" + historialPendiente.getPrice()+".00");
        code.setText(historialPendiente.getRef());
        ref = historialPendiente.getRef();


        String date1 = historialPendiente.getDate();
        FechasBD fechasBD = new FechasBD();
        String fecha = fechasBD.ObtenerFecha(date1);
        String hora = fechasBD.ObtenerHora(date1);

        date.setText(fecha);
        time.setText(hora);


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
