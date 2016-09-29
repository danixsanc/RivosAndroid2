package com.YozziBeens.rivostaxi.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.controlador.ClientController;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.respuesta.ResultadoRegistro;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudRegistro;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.regex.Pattern;


public class Registro extends AppCompatActivity
{
    private CheckBox check_terminos;
    private ResultadoRegistro resultadoRegistro;
    private MaterialEditText inputFirstName;
    private MaterialEditText inputLastName;
    private MaterialEditText inputPhone;
    private MaterialEditText inputEmail;
    private MaterialEditText inputPassword;
    private MaterialEditText inputPasswordRepeat;
    private ProgressDialog progressdialog;
    private Gson gson;
    private ClientController clientController;
    private Preferencias preferencias;


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_registro);
        this.gson = new Gson();
        clientController = new ClientController(this);
        this.preferencias = new Preferencias(getApplicationContext());
        this.resultadoRegistro = new ResultadoRegistro();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(this.getAssets(), "RobotoCondensed-Regular.ttf");

        inputFirstName = (MaterialEditText) findViewById(R.id.registerFirstName);
        inputFirstName.setTypeface(RobotoCondensed_Regular);
        inputFirstName.setAccentTypeface(RobotoCondensed_Regular);

        inputLastName = (MaterialEditText) findViewById(R.id.registerLastName);
        inputLastName.setTypeface(RobotoCondensed_Regular);
        inputLastName.setAccentTypeface(RobotoCondensed_Regular);

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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String email = extras.getString("email");
            String firstName = extras.getString("firstName");
            String lastName = extras.getString("lastName");

            inputFirstName.setText(firstName);
            inputLastName.setText(lastName);
            inputEmail.setText(email);
        }

        AccountManager accountManager = AccountManager.get(getApplicationContext());
        Account account = getAccount(accountManager);

        if (account == null) {

        } else {
            String accountName = account.name;
            inputEmail.setText(accountName);
        }

        Button btnTerminos = (Button) findViewById(R.id.txt_lee_terminos);
        btnTerminos.setTypeface(RobotoCondensed_Regular);

        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setTypeface(RobotoCondensed_Regular);

        btnTerminos.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent i = new Intent(getApplicationContext(),TerminosCondiciones.class);
                startActivity(i);
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if (check_terminos.isChecked())
                {
                    String firstName = inputFirstName.getText().toString();
                    String lastName = inputLastName.getText().toString();
                    String phone = inputPhone.getText().toString();
                    String email = inputEmail.getText().toString();
                    String password = inputPassword.getText().toString();
                    String passwordrepeat = inputPasswordRepeat.getText().toString();
                    if (checkdata(firstName,lastName, phone, email, password, passwordrepeat))
                    {
                        SolicitudRegistro oUsuario = new SolicitudRegistro();
                        oUsuario.setFirstName(firstName);
                        oUsuario.setLastName(lastName);
                        oUsuario.setPhone(phone);
                        oUsuario.setEmail(email);
                        oUsuario.setPassword(password);
                        oUsuario.setGcm_Id(preferencias.getGcm_Id());
                        oUsuario.setUser_Type("1");
                        RegistroWebService(gson.toJson(oUsuario));
                    }
                }
                else
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Registro.this, R.style.AppCompatAlertDialogStyle);
                    dialog.setMessage("Acepte los TerminosCondiciones para poder continuar.");
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
        });
    }

    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void RegistroWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.RegisterWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
                progressdialog = new ProgressDialog(Registro.this);
                progressdialog.setMessage("Registrando, espere");
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
                        resultadoRegistro = gson.fromJson(result.get("Resultado").toString(), ResultadoRegistro.class);
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

                if (!resultadoRegistro.isError()) {
                    if (resultadoRegistro.getMessage().equals("OK"))
                    {
                        clientController.eliminarTodo();
                        clientController.guardarOActualizarClient(resultadoRegistro.getData());

                        Preferencias preferencias = new Preferencias(getApplicationContext());
                        String clientId = resultadoRegistro.getData().getClient_Id().toString();
                        preferencias.setClient_Id(clientId);
                        preferencias.setSesion(false);

                        Intent main = new Intent(getApplicationContext(), Main.class);
                        startActivity(main);
                        finish();
                    }
                    else
                    {
                        String messageError = resultadoRegistro.getMessage();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Registro.this, R.style.AppCompatAlertDialogStyle);
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
                else if (resultadoRegistro.isError())
                {
                    String messageError = resultadoRegistro.getMessage();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Registro.this, R.style.AppCompatAlertDialogStyle);
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

    private boolean checkdata(String firstName, String lastName, String phone, String email, String password, String passwordrepeat){

        int cont = 0;

        if ((firstName.length()>0) && (lastName.length()>0) && (phone.length()>0) && (email.length()>0) && (password.length()>0) && (passwordrepeat.length()>0)){

            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            String namePattern = "[a-zA-Z ]+";
            String phonePattern = "[0-9]{10}";

            if (firstName.length() < 3) {
                inputFirstName.setErrorColor(Color.parseColor("#cd1500"));
                inputFirstName.validate("","El nombre es muy corto...");
                cont++;
            }
            if (firstName.length() > 30) {
                inputFirstName.setErrorColor(Color.parseColor("#cd1500"));
                inputFirstName.validate("","El nombre es muy largo...");
                cont++;
            }
            if (lastName.length() < 3) {
                inputLastName.setErrorColor(Color.parseColor("#cd1500"));
                inputLastName.validate("","El nombre es muy corto...");
                cont++;
            }
            if (lastName.length() > 30) {
                inputLastName.setErrorColor(Color.parseColor("#cd1500"));
                inputLastName.validate("","El nombre es muy largo...");
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
            if (email.contains("..")){
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
            if (firstName.charAt(0) == 32){
                inputFirstName.setErrorColor(Color.parseColor("#cd1500"));
                inputFirstName.validate("\\d+", "El nombre no puede comenzar con espacio!");
                cont++;
            }
            if (lastName.charAt(0) == 32){
                inputLastName.setErrorColor(Color.parseColor("#cd1500"));
                inputLastName.validate("\\d+", "El nombre no puede comenzar con espacio!");
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
            if (!firstName.matches(namePattern))
            {
                inputFirstName.setErrorColor(Color.parseColor("#cd1500"));
                inputFirstName.validate("","El nombre solo debe contener letras...");
                cont++;
            }
            if (!lastName.matches(namePattern))
            {
                inputLastName.setErrorColor(Color.parseColor("#cd1500"));
                inputLastName.validate("","El nombre solo debe contener letras...");
                cont++;
            }
            if (!email.matches(emailPattern))
            {
                Toast.makeText(getApplicationContext(),"valid email address", Toast.LENGTH_SHORT).show();
                inputEmail.setErrorColor(Color.parseColor("#cd1500"));
                inputEmail.validate("\\d+", "El email no es valido!");
                cont++;
            }

        }
        else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(Registro.this, R.style.AppCompatAlertDialogStyle);
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
            AlertDialog.Builder dialog = new AlertDialog.Builder(Registro.this, R.style.AppCompatAlertDialogStyle);
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
