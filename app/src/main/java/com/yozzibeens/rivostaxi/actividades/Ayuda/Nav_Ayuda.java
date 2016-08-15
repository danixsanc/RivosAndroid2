package com.YozziBeens.rivostaxi.actividades.Ayuda;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.app.PreguntasFrecuentes;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.respuesta.ResultadoMensajeAyuda;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudMensajeAyuda;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.google.gson.Gson;

import java.util.HashMap;



public class Nav_Ayuda extends AppCompatActivity {

    Button btnAsistenciaTelefonica;
    Button btnPreguntasFrecuentes;
    Button btnEnviarMensaje;
    EditText etxAsunto;
    EditText etxMensaje;
    TextView Txt_Contact_Us;
    private ProgressDialog progressdialog;
    private ResultadoMensajeAyuda resultadoMensajeAyuda;

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_ayuda);
        this.gson = new Gson();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");

        btnAsistenciaTelefonica = (Button) findViewById(R.id.btnAsistenciaTelefonica);
        btnAsistenciaTelefonica.setTypeface(RobotoCondensed_Regular);

        Txt_Contact_Us = (TextView) findViewById(R.id.Txt_Contact_Us);
        Txt_Contact_Us.setTypeface(RobotoCondensed_Regular);

        btnPreguntasFrecuentes = (Button) findViewById(R.id.btnPreguntasFrecuentes);
        btnPreguntasFrecuentes.setTypeface(RobotoCondensed_Regular);

        btnEnviarMensaje = (Button) findViewById(R.id.btnEnviarMensaje);
        btnEnviarMensaje.setTypeface(RobotoCondensed_Regular);

        btnEnviarMensaje = (Button) findViewById(R.id.btnEnviarMensaje);
        btnEnviarMensaje.setTypeface(RobotoCondensed_Regular);

        etxAsunto = (EditText) findViewById(R.id.etxAsunto);
        etxAsunto.setTypeface(RobotoCondensed_Regular);

        etxMensaje = (EditText) findViewById(R.id.etxMensaje);
        etxMensaje.setTypeface(RobotoCondensed_Regular);

        btnAsistenciaTelefonica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AsistenciaTelefonica();
            }
        });

        btnPreguntasFrecuentes.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent i = new Intent(getApplicationContext(), PreguntasFrecuentes.class);
                startActivity(i);
            }
        });

        btnEnviarMensaje.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String asunto = etxAsunto.getText().toString();
                String mensaje = etxMensaje.getText().toString();
                if (checkdata(asunto, mensaje))
                {
                    Preferencias preferencias = new Preferencias(getApplicationContext());
                    String Client_Id = preferencias.getClient_Id();
                    SolicitudMensajeAyuda oData = new SolicitudMensajeAyuda();
                    oData.setSubject(asunto);
                    oData.setMessage(mensaje);
                    oData.setClient_Id(Client_Id);
                    SendMessageWebService(gson.toJson(oData));
                    etxAsunto.setText("");
                    etxMensaje.setText("");
                }
            }
        });

    }


    private void SendMessageWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.MessageWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
                progressdialog = new ProgressDialog(Nav_Ayuda.this);
                progressdialog.setMessage("Enviando mensaje, espere");
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
                        resultadoMensajeAyuda = gson.fromJson(result.get("Resultado").toString(), ResultadoMensajeAyuda.class);
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
                    String messageError = resultadoMensajeAyuda.getMessage();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Nav_Ayuda.this, R.style.AppCompatAlertDialogStyle);
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

    public void AsistenciaTelefonica() {
        Intent callIntent;
        try {
            callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:6673171415"));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);
        }
        catch (ActivityNotFoundException activityException)
        {
            Log.e("dialing-example", "Call failed", activityException);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkdata(String asunto, String mensaje){

        int cont = 0;

        if ((asunto.length()>0) && (mensaje.length()>0))
        {
            if (asunto.length() > 30)
            {
                cont++;
            }
            if (mensaje.length() > 180)
            {
                cont++;
            }
            if (cont > 0){
                AlertDialog.Builder dialog = new AlertDialog.Builder(Nav_Ayuda.this, R.style.AppCompatAlertDialogStyle);
                dialog.setMessage("Hay campos mal escritos.");
                dialog.setCancelable(true);
                dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                return false;
            }
            else {
                return true;
            }
        }
        else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(Nav_Ayuda.this, R.style.AppCompatAlertDialogStyle);
            dialog.setMessage("Hay campos vacios.");
            dialog.setCancelable(true);
            dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();

            return false;
        }

    }

}
