package com.YozziBeens.rivostaxi.actividades.Tarjetas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.controlador.TarjetaController;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.modelo.Tarjeta;
import com.YozziBeens.rivostaxi.modelosApp.TarjetaId;
import com.YozziBeens.rivostaxi.respuesta.ResultadoAgregarTarjeta;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudAgregarTarjeta;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by danixsanc on 23/02/2016.
 */


public class Add_Card  extends AppCompatActivity{

    private Button btnGuardar;
    private MaterialEditText numberText;
    private MaterialEditText vigenciaYear;
    private MaterialEditText vigenciaMonth;
    private MaterialEditText cvcText;
    private MaterialEditText nameText;


    private ProgressDialog progressdialog;

    private ResultadoAgregarTarjeta resultadoAgregarTarjeta;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.agregar_tarjeta);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.gson = new Gson();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        numberText = (MaterialEditText) findViewById(R.id.numberText);
        nameText = (MaterialEditText) findViewById(R.id.nameText);
        cvcText = (MaterialEditText) findViewById(R.id.cvcText);
        vigenciaMonth = (MaterialEditText) findViewById(R.id.vigenciaMonth);
        vigenciaYear = (MaterialEditText) findViewById(R.id.vigenciaYear);

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


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Preferencias preferencias = new Preferencias(getApplicationContext());
                String Client_Id = preferencias.getClient_Id();

                SolicitudAgregarTarjeta oData = new SolicitudAgregarTarjeta();
                oData.setCard(numberText.getText().toString());
                oData.setMonth(vigenciaMonth.getText().toString());
                oData.setYear(vigenciaYear.getText().toString());
                oData.setName_Card(nameText.getText().toString());
                oData.setClient_Id(Client_Id);
                AgregarTarjetaWebService(gson.toJson(oData));

            }
        });
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

    private void AgregarTarjetaWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(Add_Card.this, WebService.SetCardWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
                progressdialog = new ProgressDialog(Add_Card.this);
                progressdialog.setMessage("Guardando, espere");
                progressdialog.setCancelable(true);
                progressdialog.setCanceledOnTouchOutside(false);
                progressdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        progressdialog.dismiss();
                    }
                });
                progressdialog.show();
            }

            @Override
            public void onTaskDownloadedFinished(HashMap<String, Object> result) {
                try {
                    int statusCode = Integer.parseInt(result.get("StatusCode").toString());
                    if (statusCode == 0) {
                        resultadoAgregarTarjeta = gson.fromJson(result.get("Resultado").toString(), ResultadoAgregarTarjeta.class);
                    }
                }
                catch (Exception error) {

                }
            }

            @Override
            public void onTaskUpdate(String result) {
            }

            @Override
            public void onTaskComplete(HashMap<String, Object> result) {
                progressdialog.dismiss();
                if (!resultadoAgregarTarjeta.isError())
                {
                    ArrayList<TarjetaId> tarjetaId = resultadoAgregarTarjeta.getData();

                    TarjetaController tarjetaController  = new TarjetaController(getApplicationContext());
                    Tarjeta tarjeta = new Tarjeta(null, tarjetaId.get(0).getCard_Id(), numberText.getText().toString(),
                            vigenciaMonth.getText().toString(), vigenciaYear.getText().toString(), nameText.getText().toString());

                    tarjetaController.guardarOActualizarTarjeta(tarjeta);

                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }

                String messageError = resultadoAgregarTarjeta.getMessage();
                AlertDialog.Builder dialog = new AlertDialog.Builder(Add_Card.this, R.style.AppCompatAlertDialogStyle);
                dialog.setMessage(messageError);
                dialog.setCancelable(true);
                dialog.setNegativeButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
                progressdialog.dismiss();
            }
        });
        servicioAsyncService.execute();
    }
}
