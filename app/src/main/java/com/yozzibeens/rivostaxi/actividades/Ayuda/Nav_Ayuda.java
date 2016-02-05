package com.YozziBeens.rivostaxi.actividades.Ayuda;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
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
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.YozziBeens.rivostaxi.utilerias.Servicio;


/**
 * Created by danixsanc on 12/01/2016.
 */
public class Nav_Ayuda extends AppCompatActivity {

    Button btnAsistenciaTelefonica;
    Button btnPreguntasFrecuentes;
    Button btnEnviarMensaje;
    EditText etxAsunto;
    EditText etxMensaje;
    ProgressDialog dialog;
    String asunto;
    String mensaje;
    Servicio userFunctions = new Servicio();
    TextView Txt_Contact_Us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_ayuda);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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
                if (existeConexionDeInternet())
                {
                    dialog = ProgressDialog.show(Nav_Ayuda.this, "Enviando...", "Porfavor Espere", true);
                    dialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {

                            asunto = etxAsunto.getText().toString();
                            mensaje = etxMensaje.getText().toString();

                            if ((asunto.length() > 0) && (asunto.length() <= 30) && (mensaje.length() > 0) && (mensaje.length() <= 180)){
                                Preferencias preferencias = new Preferencias(getApplicationContext());
                                String Client_id = preferencias.getClient_Id();
                                userFunctions.send_message(asunto, mensaje, Client_id);
                                etxAsunto.setText("");
                                etxMensaje.setText("");

                                AlertDialog.Builder builder = new AlertDialog.Builder(Nav_Ayuda.this, R.style.MyAlertDialogStyle);
                                builder.setTitle("Mensaje enviado");
                                builder.setMessage("Tu mensaje ha sido enviado correctamente.");
                                builder.setPositiveButton("OK", null);
                                builder.show();

                            }
                            else if ((asunto.length() < 1) || (mensaje.length() < 1)){
                                AlertDialog.Builder builder = new AlertDialog.Builder(Nav_Ayuda.this, R.style.MyAlertDialogStyle);
                                builder.setTitle("Campos en blanco");
                                builder.setMessage("Parece que has dejado campos en blanco.");
                                builder.setPositiveButton("OK", null);
                                builder.show();
                            }
                            else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(Nav_Ayuda.this, R.style.MyAlertDialogStyle);
                                builder.setTitle("Exceso de letras");
                                builder.setMessage("Parece que has excedido el limite de caracteres.");
                                builder.setPositiveButton("OK", null);
                                //builder.setNegativeButton("Cancel", null);
                                builder.show();

                            }
                            dialog.dismiss();
                        }
                    }, 2000);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext(), R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Error de conexion");
                    builder.setMessage("Parece que no estas conectado a internet, revisa tu conexion e intentalo de nuevo.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.e("info", "OK");
                        }
                    });
                    builder.show();
                }
            }
        });

    }

    public boolean existeConexionDeInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
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

}
