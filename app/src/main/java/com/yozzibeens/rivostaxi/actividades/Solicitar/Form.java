package com.YozziBeens.rivostaxi.actividades.Solicitar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.controlador.ClientController;
import com.YozziBeens.rivostaxi.controlador.Favorite_PlaceController;
import com.YozziBeens.rivostaxi.controlador.TarjetaController;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.modelo.Client;
import com.YozziBeens.rivostaxi.modelo.Favorite_Place;
import com.YozziBeens.rivostaxi.modelo.Tarjeta;
import com.YozziBeens.rivostaxi.modelosApp.AgregarHistorialCliente;
import com.YozziBeens.rivostaxi.modelosApp.TaxistasCercanos;
import com.YozziBeens.rivostaxi.respuesta.ResultadoAgregarHistorialCliente;
import com.YozziBeens.rivostaxi.respuesta.ResultadoLugaresFavoritos;
import com.YozziBeens.rivostaxi.respuesta.ResultadoTaxistasCercanos;
import com.YozziBeens.rivostaxi.respuesta.ResultadoToken;
import com.YozziBeens.rivostaxi.respuesta.ResultadoTokenError;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudAgregarHistorialCliente;
import com.YozziBeens.rivostaxi.solicitud.SolicitudObtenerPrecio;
import com.YozziBeens.rivostaxi.solicitud.SolicitudObtenerTaxistasCercanos;
import com.YozziBeens.rivostaxi.solicitud.SolicitudToken;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.YozziBeens.rivostaxi.utilerias.Servicio;
import com.conekta.conektasdk.Card;
import com.conekta.conektasdk.Conekta;
import com.conekta.conektasdk.Token;
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


    private static String KEY_SUCCESS = "Success";
    private Button btnTokenize;
    private TextView outputView;
    private TextView uuidDevice;
    private MaterialEditText numberText;
    private MaterialEditText vigenciaYear;
    private MaterialEditText vigenciaMonth;
    private EditText monthText;
    private EditText yearText;
    private MaterialEditText cvcText;
    private MaterialEditText nameText;
    private Activity activity = this;
    private ImageButton btnVerTarjetas;
    private TextView dividerDate;
    private LinearLayout PagoConTarjeta;
    private LinearLayout PagoAlTaxista;
    private Button btnFinalizar;
    private Switch sw_deAcuerdo;
    String month, year;
    String name;
    String number;
    String cvc;

    String id_token;
    String id_cabbie;
    String name_cabbie;
    String gcm_id_cabbie;
    String latitude_cabbie;
    String longitude_cabbie;
    String inicio, destino;

    SweetAlertDialog pDialog;

    int price, price_Id;
    //private ProgressDialog progressdialog;
    double latautc_inicio, lngautc_inicio, latautc_final, lngautc_final;
    String direccion;
    private Gson gson;

    private ResultadoTaxistasCercanos resultadoTaxistasCercanos;
    private ResultadoAgregarHistorialCliente resultadoAgregarHistorialCliente;

    String[] titulo, titulo2;

    private ResultadoToken resultadoToken;
    private ResultadoTokenError resultadoTokenError;
    String tipo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_form);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.gson = new Gson();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PagoConTarjeta = (LinearLayout) findViewById(R.id.PagoConTarjeta);
        PagoAlTaxista = (LinearLayout) findViewById(R.id.PagoAlTaxista);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tipo = bundle.getString("tipo");
            latautc_inicio = bundle.getDouble("latautc_inicio");
            lngautc_inicio = bundle.getDouble("lngautc_inicio");
            latautc_final = bundle.getDouble("latautc_final");
            lngautc_final = bundle.getDouble("lngautc_final");
            price = bundle.getInt("Price");
            price_Id = bundle.getInt("price_Id");
            inicio = bundle.getString("inicio");
            destino = bundle.getString("destino");

            if (tipo.equals("T")){

                PagoConTarjeta.setVisibility(View.VISIBLE);
                PagoAlTaxista.setVisibility(View.GONE);

                btnTokenize = (Button) findViewById(R.id.btnTokenize);
                outputView = (TextView) findViewById(R.id.outputView);
                uuidDevice = (TextView) findViewById(R.id.uuidDevice);
                numberText = (MaterialEditText) findViewById(R.id.numberText);
                nameText = (MaterialEditText) findViewById(R.id.nameText);
                cvcText = (MaterialEditText) findViewById(R.id.cvcText);
                vigenciaMonth = (MaterialEditText) findViewById(R.id.vigenciaMonth);
                vigenciaYear = (MaterialEditText) findViewById(R.id.vigenciaYear);
                btnVerTarjetas = (ImageButton) findViewById(R.id.btnVerTarjetas);
                dividerDate = (TextView) findViewById(R.id.dividerDate);

                btnVerTarjetas.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        CargarTarjetas(view);
                    }
                });



                numberText.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() == 16) {
                            numberText.clearFocus();
                            vigenciaMonth.requestFocus();
                        }
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                });
                vigenciaMonth.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() == 2){
                            vigenciaMonth.clearFocus();
                            vigenciaYear.requestFocus();
                        }
                    }
                });
                vigenciaYear.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() == 4){
                            vigenciaYear.clearFocus();
                            cvcText.requestFocus();
                        }
                    }
                });
                cvcText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() == 3){
                            cvcText.clearFocus();
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(cvcText.getWindowToken(), 0);
                        }
                    }
                });



                btnTokenize.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RealizarPago(view);
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
                        if (sw_deAcuerdo.isChecked()){
                            RealizarPago(v);
                        }
                        else{
                            new SweetAlertDialog(Form.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Primero debes aceptar el acuerdo!")
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


    private void RealizarPago(View view){

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Realizando pago, espere");
        pDialog.setCancelable(false);
        pDialog.show();


        /*progressdialog = new ProgressDialog(Form.this);
        progressdialog.setMessage("Realizando pago, espere");
        progressdialog.setCancelable(true);
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressdialog.dismiss();
            }
        });
        progressdialog.show();*/

        Boolean haveInternet = isOnline();

        if (tipo.equals("T")){
            name = nameText.getText().toString();
            number = numberText.getText().toString();
            cvc = cvcText.getText().toString();
            month = vigenciaMonth.getText().toString();
            year = vigenciaYear.getText().toString();

            int monthNumber = Integer.valueOf(month);
            int yearNumber = Integer.valueOf(year);

            if (number.length() == 16)
            {

                if (cvc.length() == 3)
                {
                    if (name.length() > 0)
                    {
                        if ((month.length() == 2) && ((monthNumber >0) &&(monthNumber < 13)))
                        {
                            if (year.length() == 4 &&((yearNumber > 2015)&&(yearNumber < 2020)))
                            {
                                SolicitudObtenerTaxistasCercanos oData = new SolicitudObtenerTaxistasCercanos();
                                oData.setLatitude(String.valueOf(latautc_inicio));
                                oData.setLongitude(String.valueOf(lngautc_inicio));
                                GetCloseCabbieWebService(gson.toJson(oData));
                            }
                            else
                            {
                                pDialog.dismiss();
                                Snackbar.make(view, "El año no es valido", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            pDialog.dismiss();
                            Snackbar.make(view, "El mes no es valido", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        pDialog.dismiss();
                        Snackbar.make(view, "El nombre es requerido", Snackbar.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    pDialog.dismiss();
                    Snackbar.make(view, "Faltan numeros en el codigo", Snackbar.LENGTH_SHORT).show();
                }
            }
            else {
                pDialog.dismiss();
                Snackbar.make(view, "Faltan numeros en la tarjeta", Snackbar.LENGTH_SHORT).show();
            }
        }
        else if (tipo.equals("P")){
            SolicitudObtenerTaxistasCercanos oData = new SolicitudObtenerTaxistasCercanos();
            oData.setLatitude(String.valueOf(latautc_inicio));
            oData.setLongitude(String.valueOf(lngautc_inicio));
            GetCloseCabbieWebService(gson.toJson(oData));
        }
    }

    private void CargarTarjetas(View view){
        TarjetaController tarjetaController = new TarjetaController(getApplicationContext());
        final List<Tarjeta> listfp = tarjetaController.obtenerTarjeta();
        titulo = new String[listfp.size()];
        titulo2 = new String[listfp.size()];

        for (int i = 0; i < listfp.size(); i++) {
            String card_id = listfp.get(i).getCard_Id();
            String card = listfp.get(i).getNumber_Card();

            titulo[i] = card_id;
            titulo2[i] = card;

        }
        final CharSequence[] items = titulo2;
        AlertDialog.Builder builder = new AlertDialog.Builder(Form.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Mis Tarjetas");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                numberText.setText(listfp.get(item).getNumber_Card());
                nameText.setText(listfp.get(item).getName_Card());
                vigenciaMonth.setText(listfp.get(item).getMonth());
                vigenciaYear.setText(listfp.get(item).getYear());
                cvcText.setText("");
            }

        });
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    private void GetCloseCabbieWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.GetCloseCabbieWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
            }

            @Override
            public void onTaskDownloadedFinished(HashMap<String, Object> result) {
                try {
                    int statusCode = Integer.parseInt(result.get("StatusCode").toString());
                    if (statusCode == 0) {
                        resultadoTaxistasCercanos = gson.fromJson(result.get("Resultado").toString(), ResultadoTaxistasCercanos.class);

                    }
                } catch (Exception error) {

                }
            }

            @Override
            public void onTaskUpdate(String result) {
            }

            @Override
            public void onTaskComplete(HashMap<String, Object> result) {
                if ((!resultadoTaxistasCercanos.isError()) && resultadoTaxistasCercanos.getData() != null) {
                    ArrayList<TaxistasCercanos> taxistasCercanos = resultadoTaxistasCercanos.getData();

                    for (int i=0; i < taxistasCercanos.size(); i++)
                    {
                        id_cabbie = taxistasCercanos.get(i).getCabbie_Id();
                        name_cabbie = taxistasCercanos.get(i).getName();
                        gcm_id_cabbie = taxistasCercanos.get(i).getGcm_Id();
                        latitude_cabbie = taxistasCercanos.get(i).getLatitude();
                        longitude_cabbie = taxistasCercanos.get(i).getLongitude();
                    }

                    if (tipo.equals("T")){
                        //live
                        //Conekta.setPublicKey("key_TsVXhSTCa66D29qjfwc4eZQ");
                        //prueba
                        //Conekta.setPublicKey("key_Ms2JtV99zBb9ozAsz16yLTQ");
                        Conekta.setPublicKey("key_H9xwdHFLt9Vy9vYMh1DP3zw");
                        Conekta.setApiVersion("0.3.0");
                        Conekta.collectDevice(activity);

                        Card card = new Card(name, number, cvc, month, year);
                        Token token = new Token(activity);
                        token.onCreateTokenListener(new Token.CreateToken() {

                            @Override
                            public void onCreateTokenReady(JSONObject data) {

                                try {
                                    Log.d("The token::::", data.getString("id"));
                                    outputView.setText("Token id: " + data.getString("id"));

                                    id_token = data.getString("id");

                                    Preferencias preferencias = new Preferencias(getApplicationContext());
                                    String Client_Id = preferencias.getClient_Id();
                                    ClientController clientController = new ClientController(getApplicationContext());
                                    Client client = clientController.obtenerClientPorClientId(Client_Id);

                                    SolicitudToken oData = new SolicitudToken();
                                    oData.setEmail(client.getEmail());
                                    oData.setName(client.getName());
                                    oData.setPhone(client.getPhone());
                                    oData.setToken(id_token);
                                    oData.setPrice(String.valueOf(price) + "00");
                                    ProcesPayWebService(gson.toJson(oData));

                                } catch (Exception err) {
                                    outputView.setText("Error: " + err.toString());
                                    pDialog.dismiss();
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(Form.this, R.style.AppCompatAlertDialogStyle);
                                    dialog.setMessage("Tarjeta Invalida");
                                    dialog.setCancelable(true);
                                    dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    dialog.show();
                                }
                                uuidDevice.setText("Uuid device: " + Conekta.deviceFingerPrint(activity));
                            }
                        });
                        token.create(card);
                    }
                    else if (tipo.equals("P")){

                        Preferencias preferencias = new Preferencias(getApplicationContext());
                        String Client_Id = preferencias.getClient_Id();

                        SolicitudAgregarHistorialCliente oData = new SolicitudAgregarHistorialCliente();
                        oData.setClient_Id(Client_Id);
                        oData.setCabbie_Id(id_cabbie);
                        oData.setLatitude_In(String.valueOf(latautc_inicio));
                        oData.setLongitude_In(String.valueOf(lngautc_inicio));
                        oData.setLatitude_Fn(String.valueOf(latautc_final));
                        oData.setLongitude_Fn(String.valueOf(lngautc_final));
                        oData.setPrice_Id(String.valueOf(price_Id));
                        oData.setInicio(inicio);
                        oData.setDestino(destino);
                        oData.setTipo("P");
                        SetClientHistoryWebService(gson.toJson(oData));

                    }




                }
                else
                {
                    pDialog.dismiss();
                    String messageError = resultadoTaxistasCercanos.getMessage();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Form.this, R.style.AppCompatAlertDialogStyle);
                    dialog.setMessage(messageError);
                    dialog.setCancelable(true);
                    dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
            }
        });
        servicioAsyncService.execute();
    }

    private void ProcesPayWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.ProcesPayWebService, rawJson);
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
                pDialog.dismiss();
                try {
                    int statusCode = Integer.parseInt(result.get("StatusCode").toString());
                    if (statusCode == 0) {

                        resultadoToken = gson.fromJson(result.get("Resultado").toString(), ResultadoToken.class);

                        if ((!resultadoToken.isError()) && resultadoToken.isData() != false) {

                            Preferencias preferencias = new Preferencias(getApplicationContext());
                            String Client_Id = preferencias.getClient_Id();

                            SolicitudAgregarHistorialCliente oData = new SolicitudAgregarHistorialCliente();
                            oData.setClient_Id(Client_Id);
                            oData.setCabbie_Id(id_cabbie);
                            oData.setLatitude_In(String.valueOf(latautc_inicio));
                            oData.setLongitude_In(String.valueOf(lngautc_inicio));
                            oData.setLatitude_Fn(String.valueOf(latautc_final));
                            oData.setLongitude_Fn(String.valueOf(lngautc_final));
                            oData.setPrice_Id(String.valueOf(price_Id));
                            oData.setInicio(inicio);
                            oData.setDestino(destino);
                            oData.setTipo("T");
                            SetClientHistoryWebService(gson.toJson(oData));

                        }
                    }
                } catch (Exception error) {

                    resultadoTokenError = gson.fromJson(result.get("Resultado").toString(), ResultadoTokenError.class);

                    String mensaje = resultadoTokenError.getMessage().getMessage_to_purchaser();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Form.this, R.style.AppCompatAlertDialogStyle);
                    dialog.setTitle("Tarjeta Incorrecta");
                    dialog.setMessage(mensaje);
                    dialog.setCancelable(true);
                    dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
            }
        });
        servicioAsyncService.execute();
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
                    ArrayList<AgregarHistorialCliente> agregarHistorialClientes = resultadoAgregarHistorialCliente.getData();

                    String date = null;
                    String ref = null;

                    for (int i = 0; i < agregarHistorialClientes.size(); i++) {
                        date = agregarHistorialClientes.get(i).getDate();
                        ref = agregarHistorialClientes.get(i).getRef();
                    }

                    Intent i = new Intent(Form.this, Compra_Final.class);
                    i.putExtra("latautc_inicio", latautc_inicio);
                    i.putExtra("lngautc_inicio", lngautc_inicio);
                    i.putExtra("latautc_final", latautc_final);
                    i.putExtra("lngautc_final", lngautc_final);
                    i.putExtra("direccion", direccion);
                    i.putExtra("Price", price);
                    i.putExtra("price_Id", price_Id);
                    i.putExtra("id_cabbie", id_cabbie);
                    i.putExtra("name_cabbie", name_cabbie);
                    i.putExtra("gcm_id_cabbie", gcm_id_cabbie);
                    i.putExtra("latitude_cabbie", latitude_cabbie);
                    i.putExtra("longitude_cabbie", longitude_cabbie);
                    i.putExtra("date", date);
                    i.putExtra("ref", ref);
                    pDialog.dismiss();
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

    public boolean isOnline () {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
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
