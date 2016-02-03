package com.yozzibeens.rivostaxi.actividades.Perfil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.yozzibeens.rivostaxi.R;
import com.yozzibeens.rivostaxi.controlador.ClientController;
import com.yozzibeens.rivostaxi.modelo.Client;
import com.yozzibeens.rivostaxi.utilerias.Preferencias;
import com.yozzibeens.rivostaxi.utilerias.Servicio;

import org.json.JSONException;
import org.json.JSONObject;

public class DataInActivity extends Activity {

    private static String KEY_SUCCESS = "Success";
    private static String KEY_CLIENT_ID = "Client_Id";
    private static String KEY_NAME = "Name";
    private static String KEY_PHONE = "Phone";
    private static String KEY_EMAIL = "Email";
    private static String KEY_CREATED_AT = "Created_At";
    Servicio servicio = new Servicio();


    TextView txtdataerror;
    CheckBox check_terminos;
    TextView txt_modificar_datos;
    MaterialEditText inputFullName;
    MaterialEditText inputPhone;
    MaterialEditText inputEmail;
    MaterialEditText inputPassword;
    MaterialEditText inputPasswordRepeat;
    Button btnTerminos;
    String Client_Id;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_in_layout);

        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(this.getAssets(), "RobotoCondensed-Regular.ttf");

        txt_modificar_datos = (TextView) findViewById(R.id.txt_modificar_datos);
        txt_modificar_datos.setTypeface(RobotoCondensed_Regular);

        inputFullName = (MaterialEditText) findViewById(R.id.registerName);
        inputFullName.setTypeface(RobotoCondensed_Regular);
        inputFullName.setAccentTypeface(RobotoCondensed_Regular);

        inputPhone = (MaterialEditText) findViewById(R.id.registerPhone);
        inputPhone.setTypeface(RobotoCondensed_Regular);
        inputPhone.setAccentTypeface(RobotoCondensed_Regular);

        inputEmail = (MaterialEditText) findViewById(R.id.registerEmail);
        inputEmail.setTypeface(RobotoCondensed_Regular);
        inputEmail.setAccentTypeface(RobotoCondensed_Regular);

        inputPassword = (MaterialEditText) findViewById(R.id.registerPassword);
        inputPassword.setTypeface(RobotoCondensed_Regular);
        inputPassword.setAccentTypeface(RobotoCondensed_Regular);

        inputPasswordRepeat = (MaterialEditText) findViewById(R.id.registerPasswordRepeat);
        inputPasswordRepeat.setTypeface(RobotoCondensed_Regular);
        inputPasswordRepeat.setAccentTypeface(RobotoCondensed_Regular);

        check_terminos  = (CheckBox) findViewById(R.id.check_terminos);
        check_terminos.setTypeface(RobotoCondensed_Regular);

        btnTerminos = (Button) findViewById(R.id.btn_lee_terminos);
        btnTerminos.setTypeface(RobotoCondensed_Regular);






        final Preferencias preferencias = new Preferencias(getApplicationContext());
        Client_Id = preferencias.getClient_Id();



        try {
            JSONObject json = servicio.getUser(Client_Id);

            if (json.getString(KEY_SUCCESS) != null) {
                String res = json.getString(KEY_SUCCESS);
                if (Integer.parseInt(res) == 1) {
                    JSONObject json_user = json.getJSONObject("User");

                    String name = json_user.getString(KEY_NAME);
                    String email = json_user.getString(KEY_EMAIL);
                    String phone = json_user.getString(KEY_PHONE);

                    if ((name.contains("null")))
                    {
                        name = "";
                    }
                    if ((email.contains("null")))
                    {
                        email = "";
                    }
                    if ((phone.contains("null")))
                    {
                        phone = "";
                    }

                    inputFullName.setText(name);
                    inputEmail.setText(email);
                    inputPhone.setText(phone);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //txtdataerror=(TextView)findViewById(R.id.txt_dataerror);
        check_terminos  = (CheckBox) findViewById(R.id.check_terminos);

        Button btn_terminos = (Button) findViewById(R.id.btn_lee_terminos);
        btn_terminos.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                /*Intent i = new Intent(getApplicationContext(),terminos.class);
                startActivity(i);*/
            }
        });



        Button btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {

                if (exiteConexionInternet())
                {
                    final ProgressDialog dialog = ProgressDialog.show(DataInActivity.this, "Guardando datos","Espere..." , true);
                    dialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {

                            String name2 = inputFullName.getText().toString();
                            String email2 = inputEmail.getText().toString();
                            String phone2 = inputPhone.getText().toString();
                            String pass2 = inputPassword.getText().toString();
                            String passr2 = inputPasswordRepeat.getText().toString();

                            if (check_terminos.isChecked())
                            {
                                if (checkdata(name2, phone2, email2, pass2, passr2))
                                {
                                    //UserFunctions userFunction = new UserFunctions();
                                    JSONObject json = servicio.updateUser(Client_Id, name2, phone2, email2, pass2);
                                    try {
                                        if (json.getString(KEY_SUCCESS) != null) {
                                            String res = json.getString(KEY_SUCCESS);

                                            if (Integer.parseInt(res) == 1) {

                                                JSONObject json_user = json.getJSONObject("User");
                                                String Client_Id = preferencias.getClient_Id();
                                                ClientController clientController = new ClientController(getApplicationContext());

                                                Client client = clientController.obtenerClientPorClientId(Client_Id);
                                                long id_db = client.getId();

                                                Client clien2 = new Client(id_db, Client_Id, json_user.getString(KEY_NAME),
                                                        json_user.getString(KEY_EMAIL),
                                                        json_user.getString(KEY_PHONE));


                                                clientController.guardarOActualizarClient(clien2);


                                                Intent back = new Intent(getApplicationContext(), Nav_Perfil.class);
                                                //back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(back);

                                                finish();

                                            }
                                            else if (Integer.parseInt(res) == 3)
                                            {
                                                txtdataerror.setText("El nombre de usuario ya esta registrado.");
                                            }
                                            else if (Integer.parseInt(res) == 4)
                                            {
                                                txtdataerror.setText("El telefono ya esta registrado.");
                                            }
                                            else
                                            {
                                                txtdataerror.setText("Error al actualizar datos.");
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }



                            } else {
                                txtdataerror.setText("Acepte los terminos para poder continuar.");
                            }
                            dialog.dismiss();
                        }
                    }, 3000);
                }
                else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(DataInActivity.this, R.style.AppCompatAlertDialogStyle);
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

    private boolean checkdata(String name, String phone, String email, String password, String passwordrepeat){
        int cont = 0;
        //registerErrorMsg.setText("");

        if ((name.length()>0) && (phone.length()>0)
                && (email.length()>0) && (password.length()>0) && (passwordrepeat.length()>0)){

            if (name.length() < 3) {
                inputFullName.setErrorColor(Color.parseColor("#cd1500"));
                inputFullName.validate("","El nombre es muy corto...");
                cont++;
            }
            if ((phone.length() < 10) || (phone.length() > 10)){
                inputPhone.setErrorColor(Color.parseColor("#cd1500"));
                inputPhone.validate("\\d+", "El telefono debe ser de 10 digitos!");
                cont++;
            }
            if (email.length() < 5){
                inputEmail.setErrorColor(Color.parseColor("#cd1500"));
                inputEmail.validate("\\d+", "El email no es valido!");
                cont++;
            }
            if (password.length() < 6){
                inputPassword.setErrorColor(Color.parseColor("#cd1500"));
                inputPassword.validate("\\d+", "La contraseña es muy corta!");
                cont++;
            }
            if (name.charAt(0) == 32){
                inputFullName.setErrorColor(Color.parseColor("#cd1500"));
                inputFullName.validate("\\d+", "El nombre no puede comenzar con espacio!");
                cont++;
            }

            if (email.charAt(0) == 32){
                inputEmail.setErrorColor(Color.parseColor("#cd1500"));
                inputEmail.validate("\\d+", "El email no debe comenzar con espacio!");
                cont++;
            }
            if (password.contains(" ")){
                inputPassword.setErrorColor(Color.parseColor("#cd1500"));
                inputPassword.validate("\\d+", "La contraseña no debe contener espacios!");
            }
            if (!((phone.contains("1")) || (phone.contains("2")) || (phone.contains("3")) || (phone.contains("4")) || (phone.contains("5"))
                    || (phone.contains("6")) || (phone.contains("7")) || (phone.contains("8")) || (phone.contains("9")) || (phone.contains("0")))){
                inputPhone.setErrorColor(Color.parseColor("#cd1500"));
                inputPhone.validate("\\d+", "Debe contener solo numeros!");
                cont++;
            }

            if (!((email.contains("@")) && (email.charAt(0) != 32) && (email.charAt(0) != 64) && ((email.contains(".com"))
                    || (email.contains(".com.mx")) || (email.contains(".mx")) || (email.contains(".org")) || (email.contains(".es"))
                    || (email.contains(".es")))))
            {
                inputEmail.setErrorColor(Color.parseColor("#cd1500"));
                inputEmail.validate("\\d+", "El email no es valido!");

                cont++;
            }
            if (!(password.equals(passwordrepeat)))
            {
                inputPassword.setErrorColor(Color.parseColor("#cd1500"));
                inputPassword.validate("\\d+", "Las contraseñas no coinciden!");

                inputPasswordRepeat.setErrorColor(Color.parseColor("#cd1500"));
                inputPasswordRepeat.validate("\\d+", "Las contraseñas no coinciden!");

                cont++;
            }
        }
        else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(DataInActivity.this, R.style.AppCompatAlertDialogStyle);
            dialog.setMessage("Hay campos vacios.");
            dialog.setCancelable(true);
            dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();
            cont++;
        }

        if (cont > 0){
            AlertDialog.Builder dialog = new AlertDialog.Builder(DataInActivity.this, R.style.AppCompatAlertDialogStyle);
            dialog.setMessage("Hay campos mal escritos o incompletos.");
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
}
