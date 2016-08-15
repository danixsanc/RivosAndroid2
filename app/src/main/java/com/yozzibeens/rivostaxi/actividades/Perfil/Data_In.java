/*
package com.YozziBeens.rivostaxi.actividades.Perfil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.controlador.ClientController;
import com.YozziBeens.rivostaxi.modelo.Client;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.YozziBeens.rivostaxi.utilerias.Servicio;

import org.json.JSONException;
import org.json.JSONObject;

*/
/**
 * Created by danixsanc on 16/01/2016.
 *//*

public class Data_In extends AppCompatActivity {

    TextView txt_ingresar_codigo;
    TextView txt_h1, txt_h2, txt_correo;
    Button btn_send_code;
    EditText codigo;
    ImageButton back_button;
    private static String KEY_SUCCESS = "Success";
    String Client_Id;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_in_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Preferencias preferencias = new Preferencias(getApplicationContext());
        Client_Id = preferencias.getClient_Id();
        ClientController clientController = new ClientController(getApplicationContext());
        Client client = clientController.obtenerClientPorClientId(Client_Id);
        String email = client.getEmail();



        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(this.getAssets(), "RobotoCondensed-Regular.ttf");


        txt_h1 = (TextView) findViewById(R.id.txt_h1);
        txt_h1.setTypeface(RobotoCondensed_Regular);

        txt_correo = (TextView) findViewById(R.id.txt_correo);
        txt_correo.setTypeface(RobotoCondensed_Regular);

        txt_h2 = (TextView)findViewById(R.id.txt_h2);
        txt_h2.setTypeface(RobotoCondensed_Regular);

        codigo = (EditText) findViewById(R.id.edt_codigo);
        codigo.setTypeface(RobotoCondensed_Regular);

        btn_send_code = (Button) findViewById(R.id.btn_send_code);
        btn_send_code.setTypeface(RobotoCondensed_Regular);

        txt_correo.setText(email);



        btn_send_code.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String Code = codigo.getText().toString();

                if (exiteConexionInternet())
                {
                    if ((codigo.getText() != null) || (codigo.length() != 0))
                    {
                        try {
                            Servicio servicio = new Servicio();
                            JSONObject json = servicio.verifyCode(Client_Id, Code);
                            String res = json.getString(KEY_SUCCESS);
                            if (Integer.parseInt(res) == 1) {

                                Intent i = new Intent(getApplicationContext(),DataInActivity.class);
                                startActivity(i);
                                finish();

                            }
                            else {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(Data_In.this, R.style.AppCompatAlertDialogStyle);
                                dialog.setMessage("Porfavor escriba el codigo correctamente para poder continuar.");
                                dialog.setCancelable(true);
                                dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        if (codigo.length() != 5)
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Data_In.this, R.style.AppCompatAlertDialogStyle);
                            dialog.setMessage("En estos momentos no se pueden modificar los datos, porfavor intentelo de nuevo mas tarde.");
                            dialog.setCancelable(true);
                            dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            dialog.show();
                        }

                    }
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Data_In.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Error de conexion");
                    builder.setMessage("Parece que no estas conectado a internet, revisa tu conexion e intentalo de nuevo.");
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int which) {
                                    Log.e("info", "OK");
                                }
                            });
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
*/
