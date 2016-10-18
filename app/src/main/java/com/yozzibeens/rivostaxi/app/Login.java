package com.YozziBeens.rivostaxi.app;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.YozziBeens.rivostaxi.controlador.Favorite_PlaceController;
import com.YozziBeens.rivostaxi.controlador.HistorialController;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.respuesta.ResultadoLogin;
import com.YozziBeens.rivostaxi.respuesta.ResultadoLugaresFavoritos;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudLogin;
import com.YozziBeens.rivostaxi.solicitud.SolicitudLugaresFavoritos;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.controlador.ClientController;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.Transition;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;


public class Login extends AppCompatActivity {

    Context thisContext;

    Button btn_login;
    Button btn_forgot_password;
    Button btn_go_to_register;
    MaterialEditText edtxt_email;
    MaterialEditText edtxt_password;
    LoginButton loginButton;
    CallbackManager callbackManager;

    private String fb_email = "";
    private String fb_firstName = "";
    private String fb_lastName = "";


    private ProgressDialog progressdialog;
    private Gson gson;
    private ClientController clientController;
    private HistorialController historialController;
    private Favorite_PlaceController favorite_placeController;
    private ResultadoLogin resultadoLogin;
    private ResultadoLugaresFavoritos resultadoLugaresFavoritos;
    private Preferencias preferencias;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.layout_login);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        this.gson = new Gson();
        clientController = new ClientController(this);
        historialController = new HistorialController(this);
        favorite_placeController = new Favorite_PlaceController(this);
        preferencias = new Preferencias(getApplicationContext());
        this.resultadoLogin = new ResultadoLogin();
        this.resultadoLugaresFavoritos = new ResultadoLugaresFavoritos();



        KenBurnsView kbv = (KenBurnsView) findViewById(R.id.image2);
        kbv.setTransitionListener(new KenBurnsView.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }
            @Override
            public void onTransitionEnd(Transition transition) {

            }
        });

        if (ContextCompat.checkSelfPermission(Login.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(Login.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            }
        }

        thisContext = this;
        callbackManager = CallbackManager.Factory.create();
        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(this.getAssets(), "Roboto-Light.ttf");

        edtxt_email = (MaterialEditText) findViewById(R.id.edtxt_email);
        edtxt_email.setTypeface(RobotoCondensed_Regular);
        edtxt_email.setAccentTypeface(RobotoCondensed_Regular);

        edtxt_password = (MaterialEditText) findViewById(R.id.edtxt_password);
        edtxt_password.setTypeface(RobotoCondensed_Regular);
        edtxt_password.setAccentTypeface(RobotoCondensed_Regular);

        btn_forgot_password = (Button) findViewById(R.id.btn_forgot_password);
        btn_forgot_password.setTypeface(RobotoCondensed_Regular);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setTypeface(RobotoCondensed_Regular);

        btn_go_to_register = (Button) findViewById(R.id.txt_go_to_register);
        btn_go_to_register.setTypeface(RobotoCondensed_Regular);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setTypeface(RobotoCondensed_Regular);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                progressdialog = new ProgressDialog(Login.this);
                progressdialog.setMessage("Iniciando, espere");
                progressdialog.setCancelable(true);
                progressdialog.setCanceledOnTouchOutside(false);
                progressdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        progressdialog.dismiss();
                    }
                });
                progressdialog.show();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    fb_email  = response.getJSONObject().get("email").toString();
                                    fb_firstName  = response.getJSONObject().get("first_name").toString();
                                    fb_lastName  = response.getJSONObject().get("last_name").toString();

                                    SolicitudLogin oUsuario = new SolicitudLogin();
                                    oUsuario.setEmail(fb_email);
                                    oUsuario.setPassword("");
                                    oUsuario.setGcm_Id(preferencias.getGcm_Id().toString());
                                    oUsuario.setLogin_Fb(true);
                                    oUsuario.setUser_Type("1");
                                    LoginWebService(gson.toJson(oUsuario));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();

            }


            @Override
            public void onCancel() {
                Toast.makeText(thisContext, "Cancelado por el usuario", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Error de conexion");
                builder.setMessage("Error al inciar con facebook." + exception);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("info", "OK");
                            }
                        });
                builder.show();
            }
        });

        btn_go_to_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Registro.class);
                startActivity(i);
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String email = edtxt_email.getText().toString();
                String password = edtxt_password.getText().toString();
                SolicitudLogin oUsuario = new SolicitudLogin();
                oUsuario.setEmail(email);
                oUsuario.setPassword(password);
                oUsuario.setGcm_Id(preferencias.getGcm_Id().toString());
                oUsuario.setLogin_Fb(false);
                oUsuario.setUser_Type("1");
                LoginWebService(gson.toJson(oUsuario));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }



    private void LoginWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.LoginWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
                progressdialog = new ProgressDialog(Login.this);
                progressdialog.setMessage("Iniciando, espere");
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
                        resultadoLogin = gson.fromJson(result.get("Resultado").toString(), ResultadoLogin.class);
                    }
                }
                catch (Exception error) {
                    Log.d("Error Login", error.toString());
                }
            }

            @Override
            public void onTaskUpdate(String result) {

            }

            @Override
            public void onTaskComplete(HashMap<String, Object> result) {
                progressdialog.dismiss();
                if (!resultadoLogin.isError()) {
                    if (resultadoLogin.getMessage().equals("OK")) {
                        clientController.eliminarTodo();
                        clientController.guardarOActualizarClient(resultadoLogin.getData());

                        Preferencias preferencias = new Preferencias(getApplicationContext());
                        String clientId = resultadoLogin.getData().getClient_Id().toString();
                        preferencias.setClient_Id(clientId);
                        preferencias.setSesion(false);

                        SolicitudLugaresFavoritos oData = new SolicitudLugaresFavoritos();
                        oData.setClient_Id(clientId);
                        LugaresFavoritosWebService(gson.toJson(oData));

                        Intent main = new Intent(getApplicationContext(), Main.class);
                        startActivity(main);
                        finish();
                    }
                    else{
                        if (resultadoLogin.getMessage().equals("fb")){
                            Intent i = new Intent(getApplicationContext(), Registro.class);
                            i.putExtra("email",fb_email);
                            i.putExtra("firstName",fb_firstName);
                            i.putExtra("lastName",fb_lastName);
                            startActivity(i);
                        }
                        else{
                            String messageError = resultadoLogin.getMessage();
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Login.this, R.style.AppCompatAlertDialogStyle);
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
                }
                else if (resultadoLogin.isError())
                {
                    String messageError = resultadoLogin.getMessage();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Login.this, R.style.AppCompatAlertDialogStyle);
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

    private void LugaresFavoritosWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.GetFavoritePlaceWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
            }

            @Override
            public void onTaskDownloadedFinished(HashMap<String, Object> result) {
                try {
                    int statusCode = Integer.parseInt(result.get("StatusCode").toString());
                    if (statusCode == 0) {
                        resultadoLugaresFavoritos = gson.fromJson(result.get("Resultado").toString(), ResultadoLugaresFavoritos.class);
                    }
                } catch (Exception error) {

                }
            }

            @Override
            public void onTaskUpdate(String result) {
            }

            @Override
            public void onTaskComplete(HashMap<String, Object> result) {
                if ((!resultadoLugaresFavoritos.isError()) && resultadoLugaresFavoritos.getData() != null) {
                    favorite_placeController.eliminarTodo();
                    favorite_placeController.guardarOActualizarFavorite_Place(resultadoLugaresFavoritos.getData());
                }
            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
            }
        });
        servicioAsyncService.execute();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void resetpass (View view) {
        goToUrl("http://appm.rivosservices.com/reset_pass.php");
    }


    public void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

}
