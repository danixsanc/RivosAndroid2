package com.YozziBeens.rivostaxi.actividades.Perfil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.YozziBeens.rivostaxi.app.Main;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.respuesta.ResultadoModificarDatos;
import com.YozziBeens.rivostaxi.respuesta.ResultadoRegistro;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudModificarDatos;
import com.YozziBeens.rivostaxi.solicitud.SolicitudRegistro;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.controlador.ClientController;
import com.YozziBeens.rivostaxi.modelo.Client;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;

import java.util.HashMap;

public class DataInActivity extends AppCompatActivity {

    MaterialEditText inputFullName;
    MaterialEditText inputPhone;
    MaterialEditText inputEmail;
    MaterialEditText inputPassword;
    MaterialEditText inputPasswordRepeat;
    String Client_Id;
    private Gson gson;
    private ClientController clientController;
    private ResultadoModificarDatos resultadoModificarDatos;
    private ProgressDialog progressdialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_in_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.gson = new Gson();
        clientController = new ClientController(this);

        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(this.getAssets(), "RobotoCondensed-Regular.ttf");

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

        final Preferencias preferencias = new Preferencias(getApplicationContext());
        Client_Id = preferencias.getClient_Id();
        ClientController clientController = new ClientController(getApplicationContext());
        Client client = clientController.obtenerClientPorClientId(Client_Id);
        inputFullName.setText(client.getName());
        inputEmail.setText(client.getEmail());
        inputPhone.setText(client.getPhone());






        Button btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                String name = inputFullName.getText().toString();
                String phone = inputPhone.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                String passwordrepeat = inputPasswordRepeat.getText().toString();

                if (checkdata(name, phone, email, password, passwordrepeat))
                {
                    SolicitudModificarDatos oUsuario = new SolicitudModificarDatos();
                    oUsuario.setClient_id(Client_Id);
                    oUsuario.setName(name);
                    oUsuario.setPhone(phone);
                    oUsuario.setEmail(email);
                    oUsuario.setPassword(password);
                    ModificarDatosWebService(gson.toJson(oUsuario));
                }
            }
        });

    }

    private void ModificarDatosWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.UpdateUserWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
                progressdialog = new ProgressDialog(DataInActivity.this);
                progressdialog.setMessage("Actualizando, espere");
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
                        resultadoModificarDatos = gson.fromJson(result.get("Resultado").toString(), ResultadoModificarDatos.class);
                        if ((!resultadoModificarDatos.isError()) && resultadoModificarDatos.getData() != null) {
                            clientController.eliminarTodo();
                            clientController.guardarOActualizarClient(resultadoModificarDatos.getData());

                            Preferencias preferencias = new Preferencias(getApplicationContext());
                            String clientId = resultadoModificarDatos.getData().get(0).getClient_Id();
                            preferencias.setClient_Id(clientId);
                            preferencias.setSesion(false);

                            Intent main = new Intent(getApplicationContext(), Main.class);
                            startActivity(main);
                            finish();
                        }
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
                if (resultadoModificarDatos.isError())
                {
                    String messageError = resultadoModificarDatos.getMessage();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(DataInActivity.this, R.style.AppCompatAlertDialogStyle);
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
            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
                progressdialog.dismiss();
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

    private boolean checkdata(String name, String phone, String email, String password, String passwordrepeat){

        int cont = 0;

        if ((name.length()>0) && (phone.length()>0)
                && (email.length()>0) && (password.length()>0) && (passwordrepeat.length()>0)){

            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            String namePattern = "[a-zA-Z ]+";
            String phonePattern = "[0-9]{10}";

            if (name.length() < 3) {
                inputFullName.setErrorColor(Color.parseColor("#cd1500"));
                inputFullName.validate("","El nombre es muy corto...");
                cont++;
            }
            if (name.length() > 30) {
                inputFullName.setErrorColor(Color.parseColor("#cd1500"));
                inputFullName.validate("","El nombre es muy largo...");
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
            if (password.length() > 15){
                inputPassword.setErrorColor(Color.parseColor("#cd1500"));
                inputPassword.validate("\\d+", "La contraseña es muy larga!");
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
            if (!(password.equals(passwordrepeat)))
            {
                inputPassword.setErrorColor(Color.parseColor("#cd1500"));
                inputPassword.validate("\\d+", "Las contraseñas no coinciden!");

                inputPasswordRepeat.setErrorColor(Color.parseColor("#cd1500"));
                inputPasswordRepeat.validate("\\d+", "Las contraseñas no coinciden!");

                cont++;
            }
            if (!phone.matches(phonePattern))
            {
                inputPhone.setErrorColor(Color.parseColor("#cd1500"));
                inputPhone.validate("\\d+", "Debe contener solo numeros!");
                cont++;
            }
            if (!name.matches(namePattern))
            {
                inputFullName.setErrorColor(Color.parseColor("#cd1500"));
                inputFullName.validate("","El nombre solo debe contener letras...");
                cont++;
            }
            if (!email.matches(emailPattern))
            {
                Toast.makeText(getApplicationContext(), "valid email address", Toast.LENGTH_SHORT).show();
                inputEmail.setErrorColor(Color.parseColor("#cd1500"));
                inputEmail.validate("\\d+", "El email no es valido!");

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
