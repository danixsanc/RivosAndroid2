package com.YozziBeens.rivostaxi.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.YozziBeens.rivostaxi.controlador.Favorite_CabbieController;
import com.YozziBeens.rivostaxi.controlador.Favorite_PlaceController;
import com.YozziBeens.rivostaxi.controlador.HistorialController;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.modelo.Client;
import com.YozziBeens.rivostaxi.respuesta.ResultadoHistorialCliente;
import com.YozziBeens.rivostaxi.respuesta.ResultadoLogin;
import com.YozziBeens.rivostaxi.respuesta.ResultadoLoginFacebook;
import com.YozziBeens.rivostaxi.respuesta.ResultadoLugaresFavoritos;
import com.YozziBeens.rivostaxi.respuesta.ResultadoTaxistasFavoritos;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudHistorialCliente;
import com.YozziBeens.rivostaxi.solicitud.SolicitudLogin;
import com.YozziBeens.rivostaxi.solicitud.SolicitudLoginFacebook;
import com.YozziBeens.rivostaxi.solicitud.SolicitudLugaresFavoritos;
import com.YozziBeens.rivostaxi.solicitud.SolicitudRegistro;
import com.YozziBeens.rivostaxi.solicitud.SolicitudRegistroFacebook;
import com.YozziBeens.rivostaxi.solicitud.SolicitudTaxistasFavoritos;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
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
    CheckBox check_terminos;
    MaterialEditText edtxt_email;
    MaterialEditText edtxt_password;
    TextView txt_login_networks;
    TextView txt_iniciarsesion;
    LoginButton loginButton;
    CallbackManager callbackManager;
    String FacebookName;
    String FacebookEmail;

    private ProgressDialog progressdialog;
    private Gson gson;
    private ClientController clientController;
    private HistorialController historialController;
    private Favorite_CabbieController favorite_cabbieController;
    private Favorite_PlaceController favorite_placeController;
    private ResultadoLogin resultadoLogin;
    private ResultadoLoginFacebook resultadoLoginFacebook;
    private ResultadoHistorialCliente resultadoHistorialCliente;
    private ResultadoTaxistasFavoritos resultadoTaxistasFavoritos;
    private ResultadoLugaresFavoritos resultadoLugaresFavoritos;



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
        favorite_cabbieController = new Favorite_CabbieController(this);
        favorite_placeController = new Favorite_PlaceController(this);


        KenBurnsView kbv = (KenBurnsView) findViewById(R.id.image2);
        kbv.setTransitionListener(new KenBurnsView.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }
            @Override
            public void onTransitionEnd(Transition transition) {

            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        thisContext = this;
        callbackManager = CallbackManager.Factory.create();
        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(this.getAssets(), "RobotoCondensed-Regular.ttf");

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

        txt_login_networks = (TextView) findViewById(R.id.txt_login_networks);
        txt_login_networks.setTypeface(RobotoCondensed_Regular);

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
                    public void onCompleted(JSONObject object, final GraphResponse response) {
                        Log.v("Login", response.toString());
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                Log.d("Button", "Login");
                                try {
                                    Log.v("Name:", response.getJSONObject().get("name").toString());

                                    FacebookEmail = response.getJSONObject().get("email").toString();
                                    FacebookName = response.getJSONObject().get("name").toString();
                                    SolicitudLoginFacebook oData = new SolicitudLoginFacebook();
                                    oData.setEmail(FacebookEmail);
                                    LoginFacebookWebService(gson.toJson(oData));

                                } catch (JSONException e) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this, R.style.AppCompatAlertDialogStyle);
                                    builder.setTitle("Error");
                                    builder.setMessage("No se pudo registrar con facebook debido a que no se encontro correo " +
                                            "valido. Porfavor cree una cuenta con correo valido  para continuar.");
                                    builder.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    LoginManager.getInstance().logOut();
                                                    Intent i = new Intent(getApplicationContext(), Registro.class);
                                                    startActivity(i);
                                                    finish();
                                                    Log.e("info", "OK");
                                                }
                                            });
                                    builder.show();
                                }
                            }
                        }, 5000);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();

            }


            @Override
            public void onCancel() {
                //Toast.makeText(thisContext, "Cancel!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Error de conexion");
                builder.setMessage("Error al inciar con facebook." + exception);
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
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
                LoginWebService(gson.toJson(oUsuario));
            }
        });
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
                        if ((!resultadoLogin.isError()) && resultadoLogin.getData() != null) {
                            clientController.eliminarTodo();
                            clientController.guardarOActualizarClient(resultadoLogin.getData());

                            Preferencias preferencias = new Preferencias(getApplicationContext());
                            String clientId = resultadoLogin.getData().get(0).getClient_Id();
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
                if (resultadoLogin.isError())
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
                else {
                    Preferencias preferencias = new Preferencias(getApplicationContext());
                    String clientId = preferencias.getClient_Id();
                    SolicitudTaxistasFavoritos oData = new SolicitudTaxistasFavoritos();
                    oData.setClient_Id(clientId);
                    TaxistasFavoritosWebService(gson.toJson(oData));
                }
            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
                progressdialog.dismiss();
            }
        });
        servicioAsyncService.execute();
    }

    private void LoginFacebookWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.LoginFacebookWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {

            }

            @Override
            public void onTaskDownloadedFinished(HashMap<String, Object> result) {
                try {
                    int statusCode = Integer.parseInt(result.get("StatusCode").toString());
                    if (statusCode == 0) {
                        resultadoLoginFacebook = gson.fromJson(result.get("Resultado").toString(), ResultadoLoginFacebook.class);
                        if ((!resultadoLoginFacebook.isError()) && resultadoLoginFacebook.getData() != null) {
                            clientController.eliminarTodo();
                            clientController.guardarOActualizarClient(resultadoLoginFacebook.getData());

                            Preferencias preferencias = new Preferencias(getApplicationContext());
                            String clientId = resultadoLoginFacebook.getData().get(0).getClient_Id();
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
                if (resultadoLoginFacebook.isError())
                {
                    SolicitudRegistroFacebook oData = new SolicitudRegistroFacebook();
                    oData.setName(FacebookName);
                    oData.setEmail(FacebookEmail);
                    oData.setPhone("");
                    oData.setPassword("123456");
                    RegistroFacebookWebService(gson.toJson(oData));
                }
            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
                progressdialog.dismiss();
            }
        });
        servicioAsyncService.execute();
    }

    private void RegistroFacebookWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.RegisterFacebookWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
                progressdialog = new ProgressDialog(Login.this);
                progressdialog.setMessage("Registrando");
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
                        resultadoLoginFacebook = gson.fromJson(result.get("Resultado").toString(), ResultadoLoginFacebook.class);
                        if ((!resultadoLoginFacebook.isError()) && resultadoLoginFacebook.getData() != null) {
                            clientController.eliminarTodo();
                            clientController.guardarOActualizarClient(resultadoLoginFacebook.getData());

                            Preferencias preferencias = new Preferencias(getApplicationContext());
                            String clientId = resultadoLoginFacebook.getData().get(0).getClient_Id();
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
            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
                progressdialog.dismiss();
            }
        });
        servicioAsyncService.execute();
    }

    private void HistorialClienteWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.GetClientHistoryWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {}

            @Override
            public void onTaskDownloadedFinished(HashMap<String, Object> result) {
                try {
                    int statusCode = Integer.parseInt(result.get("StatusCode").toString());
                    if (statusCode == 0) {
                        resultadoHistorialCliente = gson.fromJson(result.get("Resultado").toString(), ResultadoHistorialCliente.class);
                        if ((!resultadoHistorialCliente.isError()) && resultadoHistorialCliente.getData() != null) {
                            historialController.eliminarTodo();
                            historialController.guardarOActualizarHistorial(resultadoHistorialCliente.getData());
                        }
                    }
                }
                catch (Exception error) {
                }
            }

            @Override
            public void onTaskUpdate(String result) {}

            @Override
            public void onTaskComplete(HashMap<String, Object> result) {
                Preferencias preferencias = new Preferencias(getApplicationContext());
                String clientId = preferencias.getClient_Id();
                SolicitudLugaresFavoritos oData = new SolicitudLugaresFavoritos();
                oData.setClient_Id(clientId);
                LugaresFavoritosWebService(gson.toJson(oData));
            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {}
        });
        servicioAsyncService.execute();
    }

    private void TaxistasFavoritosWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.GetFavoriteCabbieWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
            }

            @Override
            public void onTaskDownloadedFinished(HashMap<String, Object> result) {
                try {
                    int statusCode = Integer.parseInt(result.get("StatusCode").toString());
                    if (statusCode == 0) {
                        resultadoTaxistasFavoritos = gson.fromJson(result.get("Resultado").toString(), ResultadoTaxistasFavoritos.class);
                        if ((!resultadoTaxistasFavoritos.isError()) && resultadoTaxistasFavoritos.getData() != null) {
                            favorite_cabbieController.eliminarTodo();
                            favorite_cabbieController.guardarOActualizarFavorite_Cabbie(resultadoTaxistasFavoritos.getData());
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
                if (resultadoLogin.isError())
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
                else {
                    Preferencias preferencias = new Preferencias(getApplicationContext());
                    String clientId = preferencias.getClient_Id();
                    SolicitudHistorialCliente oData = new SolicitudHistorialCliente();
                    oData.setClient_Id(clientId);
                    HistorialClienteWebService(gson.toJson(oData));
                }

            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
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
                        if ((!resultadoLugaresFavoritos.isError()) && resultadoLugaresFavoritos.getData() != null) {
                            favorite_placeController.eliminarTodo();
                            favorite_placeController.guardarOActualizarFavorite_Place(resultadoLugaresFavoritos.getData());
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
