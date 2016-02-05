package com.YozziBeens.rivostaxi.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

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
import com.YozziBeens.rivostaxi.controlador.Favorite_CabbieController;
import com.YozziBeens.rivostaxi.controlador.Favorite_PlaceController;
import com.YozziBeens.rivostaxi.controlador.HistorialController;
import com.YozziBeens.rivostaxi.modelo.Client;
import com.YozziBeens.rivostaxi.modelo.Favorite_Cabbie;
import com.YozziBeens.rivostaxi.modelo.Favorite_Place;
import com.YozziBeens.rivostaxi.modelo.Historial;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.YozziBeens.rivostaxi.utilerias.Servicio;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;


public class Login extends AppCompatActivity {

    Context thisContext;
    /*LoginButton loginButton;
    CallbackManager callbackManager;*/

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

    private static String KEY_SUCCESS = "Success";
    private static String KEY_ERROR = "Error";
    private static String KEY_ERROR_MSG = "Error_msg";
    private static String KEY_CLIENT_ID = "Client_Id";
    private static String KEY_NAME = "Name";
    private static String KEY_PHONE = "Phone";
    private static String KEY_EMAIL = "Email";
    private static String KEY_CREATED_AT = "Created_At";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.layout_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        thisContext = this;
        callbackManager = CallbackManager.Factory.create();
        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(this.getAssets(), "RobotoCondensed-Regular.ttf");

        /*txt_iniciarsesion = (TextView) findViewById(R.id.txt_iniciarsesion);
        txt_iniciarsesion.setTypeface(RobotoCondensed_Regular);*/

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
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, final GraphResponse response)
                {
                    Log.v("Login", response.toString());
                    final ProgressDialog dialog = ProgressDialog.show(Login.this, "Iniciando Sesion", "Espere...", true);
                    dialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable()
                    {
                        public void run()
                        {
                            Servicio servicio = new Servicio();
                            Log.d("Button", "Login");
                            try
                            {
                                Log.v("Name:", response.getJSONObject().get("name").toString());

                                final String email = response.getJSONObject().get("email").toString();
                                final String name = response.getJSONObject().get("name").toString();

                                JSONObject json = servicio.loginUserFB(email);
                                if (json.getString(KEY_SUCCESS) != null)
                                {
                                    String res = json.getString(KEY_SUCCESS);
                                    if (Integer.parseInt(res) == 1)
                                    {
                                        JSONObject json_user = json.getJSONObject("User");
                                        Client cliente = new Client(null, json_user.getString(KEY_CLIENT_ID),
                                                json_user.getString(KEY_NAME),
                                                json_user.getString(KEY_EMAIL),
                                                json_user.getString(KEY_PHONE));



                                        ClientController clientController = new ClientController(getApplicationContext());
                                        clientController.guardarClient(cliente);

                                        Preferencias preferencias = new Preferencias(getApplicationContext());
                                        preferencias.setClient_Id(cliente.getClient_Id());
                                        preferencias.setSesion(false);

                                        String Client_Id = preferencias.getClient_Id();

                                        int val = 0;
                                        final JSONObject json1 = servicio.getClientHistory(Client_Id);
                                        try {
                                            if (json1.getString("Success") != null) {
                                                String res1 = json1.getString("Success");
                                                if (Integer.parseInt(res1) == 1)
                                                {
                                                    val = json1.getInt("num");

                                                    HistorialController historialController2 = new HistorialController(getApplicationContext());
                                                    historialController2.eliminarTodo();
                                                    for (int i = 0; i < val; i++)
                                                    {
                                                        JSONObject json_user1 = json1.getJSONObject("Request"+(i+1));
                                                        Historial historial = new Historial(null, json_user1.getString("Request_Id"), json_user1.getString("Latitude_In"),
                                                                json_user1.getString("Latitude_Fn"),json_user1.getString("Date"),json_user1.getString("Name"));

                                                        HistorialController historialController = new HistorialController(getApplicationContext());
                                                        historialController.guardarHistorial(historial);
                                                    }
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }






                                        final JSONObject json2 = servicio.getFavoriteCabbie(Client_Id);
                                        try {
                                            if (json2.getString("Success") != null) {
                                                String res3 = json2.getString("Success");
                                                if (Integer.parseInt(res3) == 1)
                                                {

                                                    int val2 = json2.getInt("num");
                                                    Favorite_CabbieController favorite_cabbieController = new Favorite_CabbieController(getApplicationContext());
                                                    favorite_cabbieController.eliminarTodo();
                                                    for (int i = 0; i < val2; i++)
                                                    {

                                                        JSONObject json_user2 = json2.getJSONObject("Cabbie_Id"+(i+1));

                                                        Favorite_Cabbie favorite_cabbie = new Favorite_Cabbie(null, json_user2.getString("Cabbie_Id"),
                                                                json_user2.getString("Name"),json_user2.getString("Company"));

                                                        favorite_cabbieController = new Favorite_CabbieController(getApplicationContext());
                                                        favorite_cabbieController.guardarFavorite_Cabbie(favorite_cabbie);

                                                    }
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }




                                        final JSONObject json3 = servicio.getFavoritePlace(Client_Id);
                                        try {
                                            if (json3.getString("Success") != null) {
                                                String res5 = json3.getString("Success");
                                                if (Integer.parseInt(res5) == 1)
                                                {

                                                    int val3 = json3.getInt("num");
                                                    Favorite_PlaceController favorite_placeController = new Favorite_PlaceController(getApplicationContext());
                                                    favorite_placeController.eliminarTodo();
                                                    for (int i = 0; i < val3; i++)
                                                    {

                                                        JSONObject json_user3 = json3.getJSONObject("Place"+(i+1));

                                                        Favorite_Place favorite_place = new Favorite_Place(null, json_user3.getString("Place_Favorite_Id"),
                                                                json_user3.getString("Place_Name"),json_user3.getString("Desc_Place"),
                                                                json_user3.getString("Latitude"), json_user3.getString("Longitude"));

                                                        favorite_placeController = new Favorite_PlaceController(getApplicationContext());
                                                        favorite_placeController.guardarFavorite_Place(favorite_place);

                                                    }
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Intent main = new Intent(getApplicationContext(), Main.class);
                                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(main);
                                        finish();
                                    }
                                    else
                                    {
                                        String password = "123456";

                                        String phone = "";
                                        JSONObject json2 = servicio.registerUserFb(name, phone, email, password);
                                        try
                                        {
                                            if (json2.getString(KEY_SUCCESS) != null)
                                            {
                                                String res2 = json2.getString(KEY_SUCCESS);
                                                if (Integer.parseInt(res2) == 1)
                                                {
                                                    JSONObject json_user = json2.getJSONObject("User");
                                                    servicio.logoutUser(getApplicationContext());
                                                    Client cliente = new Client(null, json_user.getString(KEY_CLIENT_ID),
                                                            json_user.getString(KEY_NAME),
                                                            json_user.getString(KEY_EMAIL),
                                                            json_user.getString(KEY_PHONE));

                                                    ClientController clientController = new ClientController(getApplicationContext());
                                                    clientController.guardarClient(cliente);

                                                    Preferencias preferencias = new Preferencias(getApplicationContext());
                                                    preferencias.setClient_Id(cliente.getClient_Id());
                                                    preferencias.setSesion(false);

                                                    Intent main = new Intent(getApplicationContext(), Main.class);
                                                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(main);
                                                    finish();
                                                }
                                            }
                                        }
                                        catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                            catch (JSONException e)
                            {

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
                            dialog.dismiss();
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
















        /*loginButton.setReadPermissions(Arrays.asList("public_profile, email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (exiteConexionInternet()) {
                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, final GraphResponse response) {
                                    Log.v("Login", response.toString());

                                    final ProgressDialog dialog = ProgressDialog.show(Login.this, "Iniciando Sesion", "Espere...", true);
                                    dialog.show();
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            Servicio userFunction = new Servicio();
                                            Log.d("Button", "Login");
                                            try {
                                                Log.v("Name:", response.getJSONObject().get("name").toString());

                                                final String email = response.getJSONObject().get("email").toString();
                                                final String name = response.getJSONObject().get("name").toString();

                                                JSONObject json = userFunction.loginUserFB(email);
                                                if (json.getString(KEY_SUCCESS) != null) {
                                                    String res = json.getString(KEY_SUCCESS);
                                                    if (Integer.parseInt(res) == 1) {


                                                        //DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                                        JSONObject json_user = json.getJSONObject("User");
                                                        userFunction.logoutUser(getApplicationContext());

                                                        Client cliente = new Client(null, json_user.getString(KEY_CLIENT_ID),
                                                                json_user.getString(KEY_NAME),
                                                                json_user.getString(KEY_EMAIL),
                                                                json_user.getString(KEY_PHONE));

                                                        ClientController clientController = new ClientController(getApplicationContext());
                                                        clientController.guardarClient(cliente);



                                                        Intent main = new Intent(getApplicationContext(), MainActivity.class);
                                                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(main);
                                                        finish();
                                                    } else {
                                                        String password = "123456";

                                                        String phone = "";
                                                        JSONObject json2 = userFunction.registerUserFb(name, phone, email, password);
                                                        try {
                                                            if (json2.getString(KEY_SUCCESS) != null) {
                                                                String res2 = json2.getString(KEY_SUCCESS);

                                                                if (Integer.parseInt(res2) == 1) {

                                                                    JSONObject json_user = json2.getJSONObject("User");
                                                                    userFunction.logoutUser(getApplicationContext());

                                                                    Client cliente = new Client(null, json_user.getString(KEY_CLIENT_ID),
                                                                            json_user.getString(KEY_NAME),
                                                                            json_user.getString(KEY_EMAIL),
                                                                            json_user.getString(KEY_PHONE));

                                                                    ClientController clientController = new ClientController(getApplicationContext());
                                                                    clientController.guardarClient(cliente);

                                                                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                                                                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                    startActivity(main);
                                                                    finish();
                                                                }
                                                            }

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                }

                                            } catch (JSONException e) {
                                                //e.printStackTrace();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                                        Login.this, R.style.AppCompatAlertDialogStyle);
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
                                            dialog.dismiss();
                                        }
                                    }, 5000);  // 3000 milliseconds


                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email");
                    request.setParameters(parameters);
                    request.executeAsync();
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Login.this, R.style.AppCompatAlertDialogStyle);
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

            @Override
            public void onCancel() {
                Toast.makeText(thisContext, "Cancel!", Toast.LENGTH_LONG).show();
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
*/
        btn_go_to_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Registro.class);
                startActivity(i);
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                final ProgressDialog dialog = ProgressDialog.show(Login.this, "Iniciando", "Espere...", true);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        String email = edtxt_email.getText().toString();
                        String password = edtxt_password.getText().toString();

                        Servicio servicio = new Servicio();
                        try

                        {
                            JSONObject json = servicio.loginUser(email, password);

                            if (json.getString(KEY_SUCCESS) != null) {
                                String res = json.getString(KEY_SUCCESS);
                                if (Integer.parseInt(res) == 1) {
                                    JSONObject json_user = json.getJSONObject("User");
                                    servicio.logoutUser(getApplicationContext());

                                    Client client = new Client(null, json_user.getString("Client_Id"), json_user.getString("Name"),
                                            json_user.getString("Email"), json_user.getString("Phone"));
                                    ClientController clientController = new ClientController(getApplicationContext());
                                    clientController.guardarOActualizarClient(client);

                                    Preferencias preferencias = new Preferencias(getApplicationContext());
                                    preferencias.setSesion(false);
                                    preferencias.setClient_Id(json_user.getString("Client_Id"));

                                    String Client_Id = preferencias.getClient_Id();

                                    int val = 0;
                                    final JSONObject json1 = servicio.getClientHistory(Client_Id);
                                    try {
                                        if (json1.getString("Success") != null) {
                                            String res1 = json1.getString("Success");
                                            if (Integer.parseInt(res1) == 1)
                                            {
                                                val = json1.getInt("num");

                                                HistorialController historialController2 = new HistorialController(getApplicationContext());
                                                historialController2.eliminarTodo();
                                                for (int i = 0; i < val; i++)
                                                {
                                                    JSONObject json_user1 = json1.getJSONObject("Request"+(i+1));
                                                    Historial historial = new Historial(null, json_user1.getString("Request_Id"), json_user1.getString("Latitude_In"),
                                                            json_user1.getString("Latitude_Fn"),json_user1.getString("Date"),json_user1.getString("Name"));

                                                    HistorialController historialController = new HistorialController(getApplicationContext());
                                                    historialController.guardarHistorial(historial);
                                                }
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }






                                    final JSONObject json2 = servicio.getFavoriteCabbie(Client_Id);
                                    try {
                                        if (json2.getString("Success") != null) {
                                            String res3 = json2.getString("Success");
                                            if (Integer.parseInt(res3) == 1)
                                            {

                                                int val2 = json2.getInt("num");
                                                Favorite_CabbieController favorite_cabbieController = new Favorite_CabbieController(getApplicationContext());
                                                favorite_cabbieController.eliminarTodo();
                                                for (int i = 0; i < val2; i++)
                                                {

                                                    JSONObject json_user2 = json2.getJSONObject("Cabbie_Id"+(i+1));

                                                    Favorite_Cabbie favorite_cabbie = new Favorite_Cabbie(null, json_user2.getString("Cabbie_Id"),
                                                            json_user2.getString("Name"),json_user2.getString("Company"));

                                                    favorite_cabbieController = new Favorite_CabbieController(getApplicationContext());
                                                    favorite_cabbieController.guardarFavorite_Cabbie(favorite_cabbie);

                                                }
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }




                                    final JSONObject json3 = servicio.getFavoritePlace(Client_Id);
                                    try {
                                        if (json3.getString("Success") != null) {
                                            String res5 = json3.getString("Success");
                                            if (Integer.parseInt(res5) == 1)
                                            {

                                                int val3 = json3.getInt("num");
                                                Favorite_PlaceController favorite_placeController = new Favorite_PlaceController(getApplicationContext());
                                                favorite_placeController.eliminarTodo();
                                                for (int i = 0; i < val3; i++)
                                                {

                                                    JSONObject json_user3 = json3.getJSONObject("Place"+(i+1));

                                                    Favorite_Place favorite_place = new Favorite_Place(null, json_user3.getString("Place_Favorite_Id"),
                                                            json_user3.getString("Place_Name"),json_user3.getString("Desc_Place"),
                                                            json_user3.getString("Latitude"), json_user3.getString("Longitude"));

                                                    favorite_placeController = new Favorite_PlaceController(getApplicationContext());
                                                    favorite_placeController.guardarFavorite_Place(favorite_place);

                                                }
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Intent main = new Intent(getApplicationContext(), Main.class);
                                    startActivity(main);
                                    finish();
                                } else {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(Login.this, R.style.AppCompatAlertDialogStyle);
                                    dialog.setMessage("Usuario/Contraseña Incorrecto(a)");
                                    dialog.setCancelable(false);
                                    dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    dialog.show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();
                    }
                }, 3000);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void resetpass (View view) {goToUrl ("http://appm.rivosservices.com/reset_pass.php");}
    //public void PreguntasFrecuentes (View view) { goToUrl("http://appm.rivosservices.com/faqs.html");}

    public void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

}
