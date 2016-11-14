package com.YozziBeens.rivostaxi.actividades.Solicitar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.actividades.Tarjetas.Card_Details;
import com.YozziBeens.rivostaxi.actividades.Tarjetas.Nav_Tarjetas;
import com.YozziBeens.rivostaxi.adaptadores.AdaptadorTarjetas;
import com.YozziBeens.rivostaxi.controlador.ClientController;
import com.YozziBeens.rivostaxi.controlador.Favorite_PlaceController;
import com.YozziBeens.rivostaxi.controlador.TarjetaController;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.modelo.Client;
import com.YozziBeens.rivostaxi.modelo.Favorite_Place;
import com.YozziBeens.rivostaxi.modelo.Tarjeta;
import com.YozziBeens.rivostaxi.modelosApp.AgregarHistorialCliente;
import com.YozziBeens.rivostaxi.modelosApp.Solicitud;
import com.YozziBeens.rivostaxi.modelosApp.TaxistasCercanos;
import com.YozziBeens.rivostaxi.respuesta.ResultadoAgregarHistorialCliente;
import com.YozziBeens.rivostaxi.respuesta.ResultadoLugaresFavoritos;
import com.YozziBeens.rivostaxi.respuesta.ResultadoTaxistasCercanos;
import com.YozziBeens.rivostaxi.respuesta.ResultadoToken;
import com.YozziBeens.rivostaxi.respuesta.ResultadoTokenError;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudAgregarHistorialCliente;
import com.YozziBeens.rivostaxi.solicitud.SolicitudCancelCabbie;
import com.YozziBeens.rivostaxi.solicitud.SolicitudEliminarTarjeta;
import com.YozziBeens.rivostaxi.solicitud.SolicitudObtenerPrecio;
import com.YozziBeens.rivostaxi.solicitud.SolicitudObtenerTaxistasCercanos;
import com.YozziBeens.rivostaxi.solicitud.SolicitudToken;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.YozziBeens.rivostaxi.utilerias.Servicio;

import com.YozziBeens.rivostaxi.utilerias.TarjetasBD;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by danixsanc on 30/01/2016.
 */
public class Form extends AppCompatActivity {


    private Button btnTokenize;
    private LinearLayout PagoConTarjeta;
    private LinearLayout PagoAlTaxista;
    private Button btnFinalizar;
    private Switch sw_deAcuerdo;
    private Preferencias preferencias;
    private int mSelectedPosition = -1;



    SweetAlertDialog pDialog;

    String Cabbie_Id;

    private Gson gson;

    private ResultadoAgregarHistorialCliente resultadoAgregarHistorialCliente;

    String tipo;

    TextView timer;
    TextView timer2;
    boolean canRequest = false;

    private Solicitud solicitud;
    private CountDownTimer c;
    private static ListView listCards;

    private String finalCardId;
    private TextView nameWindows;


    Form.TarjetasCustomAdapter tarjetasAdapter;
    ArrayList<AdaptadorTarjetas> tarjetasArray = new ArrayList<AdaptadorTarjetas>();
    String request_id[] = new String[0];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.nameWindows = (TextView) findViewById(R.id.nameWindows);
        this.nameWindows.setText("Forma de pago");

        this.gson = new Gson();
        this.preferencias = new Preferencias(getApplicationContext());

        this.solicitud = new Solicitud();
        this.timer = (TextView) findViewById(R.id.timer);
        this.timer2 = (TextView) findViewById(R.id.timer2);
        this.resultadoAgregarHistorialCliente = new ResultadoAgregarHistorialCliente();

        this.listCards = (ListView) findViewById(R.id.cards);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PagoConTarjeta = (LinearLayout) findViewById(R.id.PagoConTarjeta);
        PagoAlTaxista = (LinearLayout) findViewById(R.id.PagoAlTaxista);


        TarjetaController tarjetaController = new TarjetaController(getApplicationContext());
        List<Tarjeta> tarjetasLista = tarjetaController.obtenerTarjeta();

        request_id = new String[tarjetasLista.size()];
        for (int i=0; i < tarjetasLista.size(); i++)
        {
            String id = String.valueOf(tarjetasLista.get(i).getCard_Id());
            String tarjeta = tarjetasLista.get(i).getLast4();

            TarjetasBD tarjetasBD = new TarjetasBD();
            String tarjF = tarjetasBD.ocultarTarjeta(tarjetasLista.get(i).getLast4());
            tarjetasArray.add(new AdaptadorTarjetas(tarjF, id));
            request_id[i] = String.valueOf(tarjetasLista.get(i).getCard_Id());
        }


        tarjetasAdapter = new Form.TarjetasCustomAdapter(getApplicationContext(), R.layout.row_tarjetas_pago, tarjetasArray);
        listCards.setItemsCanFocus(false);
        listCards.setAdapter(tarjetasAdapter);



        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tipo = bundle.getString("tipo");
            solicitud = (Solicitud) bundle.getSerializable("Solicitud");

            this.c = new CountDownTimer(Integer.parseInt(solicitud.getTimeRest().toString()) * 1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    timer.setText(""+millisUntilFinished / 1000);
                    timer2.setText(""+millisUntilFinished / 1000);
                    canRequest = true;
                }

                public void onFinish() {
                    timer.setText("00");
                    timer.setBackgroundColor(Color.RED);
                    timer2.setText("00");
                    timer2.setBackgroundColor(Color.RED);
                    SolicitudCancelCabbie oData = new SolicitudCancelCabbie();
                    oData.setCabbie_Id(Cabbie_Id);
                    CancelCabbieWebService(gson.toJson(oData));
                    canRequest = false;
                }
            };
            this.c.start();

            if (tipo.equals("T")){

                PagoConTarjeta.setVisibility(View.VISIBLE);
                PagoAlTaxista.setVisibility(View.GONE);

                btnTokenize = (Button) findViewById(R.id.btnTokenize);
                btnTokenize.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (canRequest){
                            if (mSelectedPosition == -1){
                                new SweetAlertDialog(Form.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Primero debes selecciona una tarjeta!")
                                        .setConfirmText("Entendido")
                                        .show();
                            }
                            else {
                                RealizarPago();
                            }

                        }
                        else
                        {

                            new SweetAlertDialog(Form.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("El tiempo se ha agotado, regresa a la seccion anterior!")
                                    .setConfirmText("Entendido")
                                    .show();
                        }


                    }
                });


            }
            else if (tipo.equals("P")){
                PagoConTarjeta.setVisibility(View.GONE);
                PagoAlTaxista.setVisibility(View.VISIBLE);
                sw_deAcuerdo = (Switch) findViewById(R.id.sw_deAcuerdo);


                btnFinalizar = (Button) findViewById(R.id.btnFinalizar);
                btnFinalizar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (canRequest){
                            if (sw_deAcuerdo.isChecked()){
                                RealizarPago();
                            }
                            else{
                                new SweetAlertDialog(Form.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Primero debes aceptar el acuerdo!")
                                        .setConfirmText("Entendido")
                                        .show();
                            }
                        }
                        else
                        {
                            new SweetAlertDialog(Form.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("El tiempo se ha agotado, regresa a la seccion anterior!")
                                    .setConfirmText("Entendido")
                                    .show();
                        }


                    }
                });
            }
            else{
                Toast.makeText(getApplicationContext(), "Error Inesperado", Toast.LENGTH_LONG).show();
                finish();
            }

        }
        else{
            Toast.makeText(getApplicationContext(), "Error Inesperado", Toast.LENGTH_LONG).show();
            finish();
        }
    }


    private void RealizarPago(){

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Realizando pago, espere");
        pDialog.setCancelable(false);
        pDialog.show();


        if (tipo.equals("T")){

            String cardId = request_id[mSelectedPosition];
            String Client_Id = preferencias.getClient_Id();
            SolicitudAgregarHistorialCliente oData = new SolicitudAgregarHistorialCliente();
            oData.setLatitude_In(solicitud.getLatOrigen());
            oData.setLongitude_In(solicitud.getLongOrigen());
            oData.setLatitude_Fn(solicitud.getLatDestino());
            oData.setLongitude_Fn(solicitud.getLongDestino());
            oData.setInicio(solicitud.getDirOrigen());
            oData.setDestino(solicitud.getDirDestino());
            oData.setPrecio(solicitud.getPrice());
            oData.setClient_Id(Client_Id);
            oData.setCabbie_Id(solicitud.getCabbie_Id());
            oData.setCard_Id(cardId);
            oData.setPaymentType_Id("1");
            SetClientHistoryWebService(gson.toJson(oData));

        }
        else if (tipo.equals("P")){

            String Client_Id = preferencias.getClient_Id();
            SolicitudAgregarHistorialCliente oData = new SolicitudAgregarHistorialCliente();

            oData.setLatitude_In(solicitud.getLatOrigen());
            oData.setLongitude_In(solicitud.getLongOrigen());
            oData.setLatitude_Fn(solicitud.getLatDestino());
            oData.setLongitude_Fn(solicitud.getLongDestino());
            oData.setInicio(solicitud.getDirOrigen());
            oData.setDestino(solicitud.getDirDestino());
            oData.setPrecio(solicitud.getPrice());
            oData.setClient_Id(Client_Id);
            oData.setCabbie_Id(solicitud.getCabbie_Id());
            oData.setCard_Id("");
            oData.setPaymentType_Id("2");
            SetClientHistoryWebService(gson.toJson(oData));

        }
    }



    private void SetClientHistoryWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.SetClientHistoryWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
            }

            @Override
            public void onTaskDownloadedFinished(HashMap<String, Object> result) {
                try {
                    int statusCode = Integer.parseInt(result.get("StatusCode").toString());
                    if (statusCode == 0) {
                        resultadoAgregarHistorialCliente = gson.fromJson(result.get("Resultado").toString(), ResultadoAgregarHistorialCliente.class);

                    }
                } catch (Exception error) {

                }
            }

            @Override
            public void onTaskUpdate(String result) {
            }

            @Override
            public void onTaskComplete(HashMap<String, Object> result) {



                if ((!resultadoAgregarHistorialCliente.isError()) && resultadoAgregarHistorialCliente.getData() != null) {


                    String ref = resultadoAgregarHistorialCliente.getData().getRef();
                    String date = resultadoAgregarHistorialCliente.getData().getDate();

                    Intent i = new Intent(Form.this, Compra_Final.class);
                    i.putExtra("Solicitud", solicitud);
                    i.putExtra("Ref", ref);
                    i.putExtra("Date", date);
                    pDialog.dismiss();
                    c.cancel();
                    startActivity(i);
                    finish();

                }

            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
            }
        });
        servicioAsyncService.execute();
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

    public class TarjetasCustomAdapter extends ArrayAdapter<AdaptadorTarjetas>{
        Context context;
        int layoutResourceId;

        private RadioButton mSelectedRB;
        ArrayList<AdaptadorTarjetas> data = new ArrayList<AdaptadorTarjetas>();

        public TarjetasCustomAdapter(Context context, int layoutResourceId, ArrayList<AdaptadorTarjetas> data) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = convertView;
            Form.TarjetasCustomAdapter.UserHolder holder = null;

            if (row == null) {


                Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");
                //LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new Form.TarjetasCustomAdapter.UserHolder();
                holder.textTarjeta = (TextView) row.findViewById(R.id.txtTarjeta);
                holder.textTarjeta.setTypeface(RobotoCondensed_Regular);
                holder.textIdTarjeta = (TextView) row.findViewById(R.id.txtIdTarjeta);
                holder.textIdTarjeta.setTypeface(RobotoCondensed_Regular);

                holder.rbtn_tarjeta = (RadioButton) row.findViewById(R.id.rbtn_tarjeta);

                final UserHolder finalHolder = holder;
                holder.rbtn_tarjeta.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if(position != mSelectedPosition && mSelectedRB != null){
                            mSelectedRB.setChecked(false);
                        }

                        mSelectedPosition = position;
                        mSelectedRB = (RadioButton)v;

                    }
                });

                if(mSelectedPosition != position){
                    holder.rbtn_tarjeta.setChecked(false);
                }else{
                    holder.rbtn_tarjeta.setChecked(true);
                    if(mSelectedRB != null && holder.rbtn_tarjeta != mSelectedRB){
                        mSelectedRB = holder.rbtn_tarjeta;
                    }
                }




                row.setTag(holder);


                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });





            } else {
                holder = (Form.TarjetasCustomAdapter.UserHolder) row.getTag();
            }
            final AdaptadorTarjetas adaptadorTarjetas = data.get(position);
            holder.textTarjeta.setText(adaptadorTarjetas.getTarjeta());


            return row;

        }


        class UserHolder {
            TextView textTarjeta;
            TextView textIdTarjeta;
            RadioButton rbtn_tarjeta;
            TableLayout tlcard;
        }
    }


}
