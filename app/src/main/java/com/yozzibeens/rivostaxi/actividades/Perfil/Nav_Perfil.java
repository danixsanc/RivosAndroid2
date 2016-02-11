package com.YozziBeens.rivostaxi.actividades.Perfil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.controlador.ClientController;
import com.YozziBeens.rivostaxi.modelo.Client;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.YozziBeens.rivostaxi.utilerias.Servicio;

/**
 * Created by danixsanc on 12/01/2016.
 */
public class Nav_Perfil extends AppCompatActivity {

    TextView txt_phone_user;
    TextView txt_email_user;
    TextView txt_nombre_user;
    TextView txt_datos_personales, txt_nombre, txt_email, txt_phone; //********

    Button btn_modifydata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_perfil);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");

        txt_datos_personales = (TextView) findViewById(R.id.txt_datos_personales);
        txt_datos_personales.setTypeface(RobotoCondensed_Regular);
        txt_nombre = (TextView) findViewById(R.id.txt_nombre);
        txt_nombre.setTypeface(RobotoCondensed_Regular);
        txt_email = (TextView) findViewById(R.id.txt_email);
        txt_email.setTypeface(RobotoCondensed_Regular);
        txt_phone = (TextView) findViewById(R.id.txt_phone);
        txt_phone.setTypeface(RobotoCondensed_Regular);
        txt_phone_user = (TextView) findViewById(R.id.txt_phone_user);
        txt_phone_user.setTypeface(RobotoCondensed_Regular);
        txt_email_user = (TextView) findViewById(R.id.txt_email_user);
        txt_email_user.setTypeface(RobotoCondensed_Regular);
        txt_nombre_user = (TextView) findViewById(R.id.txt_nombre_user);
        txt_nombre_user.setTypeface(RobotoCondensed_Regular);

        final Preferencias preferencias = new Preferencias(getApplicationContext());
        String ClientId = preferencias.getClient_Id();
        ClientController clientController =  new ClientController(getApplicationContext());
        Client client = new Client();
        client = clientController.obtenerClientPorClientId(ClientId);
        String Nombre = client.getName();
        final String Correo = client.getEmail();
        String Telefono = client.getPhone();

        txt_email_user.setText(Correo);
        txt_nombre_user.setText(Nombre);
        txt_phone_user.setText(Telefono);

        btn_modifydata = (Button) findViewById(R.id.modify_data);
        btn_modifydata.setTypeface(RobotoCondensed_Regular);
        btn_modifydata.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (exiteConexionInternet()) {

                    Servicio servicio = new Servicio();
                    servicio.modify_data(Correo);
                    Intent i = new Intent(Nav_Perfil.this , Data_In.class);
                    startActivity(i);
                    finish();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Nav_Perfil.this, R.style.MyAlertDialogStyle);
                    builder.setTitle("Error de conexion");
                    builder.setMessage("Parece que no esta conectado a Internet. Porfavor revise su conexion e intentelo de nuevo.");
                    builder.setPositiveButton("OK", null);
                    //builder.setNegativeButton("Cancel", null);
                    builder.show();
                }

            }
        });

    }

    public boolean exiteConexionInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
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
