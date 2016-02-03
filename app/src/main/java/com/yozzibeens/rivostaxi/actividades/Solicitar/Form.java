package com.yozzibeens.rivostaxi.actividades.Solicitar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.conekta.conektasdk.Card;
import com.conekta.conektasdk.Conekta;
import com.conekta.conektasdk.Token;
import com.yozzibeens.rivostaxi.R;
import com.yozzibeens.rivostaxi.controlador.ClientController;
import com.yozzibeens.rivostaxi.modelo.Client;
import com.yozzibeens.rivostaxi.utilerias.Preferencias;
import com.yozzibeens.rivostaxi.utilerias.Servicio;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by danixsanc on 30/01/2016.
 */
public class Form extends AppCompatActivity {


    private static String KEY_SUCCESS = "Success";
    private Button btnTokenize;
    private TextView outputView;
    private TextView uuidDevice;
    private EditText numberText;
    private EditText monthText;
    private EditText yearText;
    private EditText cvcText;
    private EditText nameText;
    private Activity activity = this;
    int price, price_Id;
    double latautc_inicio, lngautc_inicio, latautc_final, lngautc_final;
    String direccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_form);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            latautc_inicio = bundle.getDouble("latautc_inicio");
            lngautc_inicio = bundle.getDouble("lngautc_inicio");
            latautc_final = bundle.getDouble("latautc_final");
            lngautc_final = bundle.getDouble("lngautc_final");
            direccion = bundle.getString("direccion");
            direccion = bundle.getString("direccion");
            price = bundle.getInt("Price");
            price_Id = bundle.getInt("price_Id");
        }

        btnTokenize = (Button) findViewById(R.id.btnTokenize);
        outputView = (TextView) findViewById(R.id.outputView);
        uuidDevice = (TextView) findViewById(R.id.uuidDevice);
        numberText = (EditText) findViewById(R.id.numberText);
        nameText = (EditText) findViewById(R.id.nameText);
        monthText = (EditText) findViewById(R.id.monthText);
        yearText = (EditText) findViewById(R.id.yearText);
        cvcText = (EditText) findViewById(R.id.cvcText);

        btnTokenize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean haveInternet = isOnline();
                if (haveInternet) {

                    Conekta.setPublicKey("key_H9xwdHFLt9Vy9vYMh1DP3zw");
                    Conekta.setApiVersion("0.3.0");
                    Conekta.collectDevice(activity);
                    Card card = new Card(nameText.getText().toString(), numberText.getText().toString(), cvcText.getText().toString(), monthText.getText().toString(), yearText.getText().toString());
                    Token token = new Token(activity);

                    //Listen when token is returned
                    token.onCreateTokenListener(new Token.CreateToken() {

                        @Override
                        public void onCreateTokenReady(JSONObject data) {

                            try {
                                Log.d("The token::::", data.getString("id"));
                                outputView.setText("Token id: " + data.getString("id"));

                                Servicio servicio = new Servicio();
                                Preferencias preferencias = new Preferencias(getApplicationContext());
                                String ClientId = preferencias.getClient_Id();
                                ClientController clientController = new ClientController(getApplicationContext());
                                Client client = clientController.obtenerClientPorClientId(ClientId);
                                String nombre = client.getName();
                                String email = client.getEmail();
                                String phone = client.getPhone();


                                JSONObject json = servicio.ProcessPay(String.valueOf(price)+"00", data.getString("id"), nombre, email, phone);

                                try {
                                    if (json.getString(KEY_SUCCESS) != null) {
                                        String res = json.getString(KEY_SUCCESS);
                                        if (Integer.parseInt(res) == 1) {
                                            Intent i = new Intent(Form.this , Compra_Final.class);
                                            i.putExtra("latautc_inicio", latautc_inicio);
                                            i.putExtra("lngautc_inicio", lngautc_inicio);
                                            i.putExtra("latautc_final", latautc_final);
                                            i.putExtra("lngautc_final", lngautc_final);
                                            i.putExtra("direccion", direccion);
                                            i.putExtra("Price", price);
                                            i.putExtra("price_Id", price_Id);
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }







                            } catch (Exception err) {
                                outputView.setText("Error: " + err.toString());
                            }
                            uuidDevice.setText("Uuid device: " + Conekta.deviceFingerPrint(activity));
                        }
                    });

                    //Request for create token
                    token.create(card);
                } else {
                    outputView.setText("You don't have internet");
                }
            }
        });
    }

    public boolean isOnline () {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
