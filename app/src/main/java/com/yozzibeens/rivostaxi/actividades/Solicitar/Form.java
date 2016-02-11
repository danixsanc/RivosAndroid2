package com.YozziBeens.rivostaxi.actividades.Solicitar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.controlador.ClientController;
import com.YozziBeens.rivostaxi.modelo.Client;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.YozziBeens.rivostaxi.utilerias.Servicio;
import com.conekta.conektasdk.Card;
import com.conekta.conektasdk.Conekta;
import com.conekta.conektasdk.Token;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danixsanc on 30/01/2016.
 */
public class Form extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


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
    String month, year;
    int price, price_Id;
    double latautc_inicio, lngautc_inicio, latautc_final, lngautc_final;
    String direccion;

    String tarjetaPattern = "[0-9]{16}";
    String cvcPattern = "[0-9]{3}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_form);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        Spinner spinner2 = (Spinner) findViewById(R.id.spinnerYear);
        spinner2.setOnItemSelectedListener(this);
        List<String> categories2 = new ArrayList<String>();
        categories2.add("16");
        categories2.add("17");
        categories2.add("18");
        categories2.add("19");
        categories2.add("20");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter2);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerMonth);
        spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("01");
        categories.add("02");
        categories.add("03");
        categories.add("04");
        categories.add("05");
        categories.add("06");
        categories.add("07");
        categories.add("08");
        categories.add("09");
        categories.add("10");
        categories.add("11");
        categories.add("12");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        btnTokenize = (Button) findViewById(R.id.btnTokenize);
        outputView = (TextView) findViewById(R.id.outputView);
        uuidDevice = (TextView) findViewById(R.id.uuidDevice);
        numberText = (EditText) findViewById(R.id.numberText);
        nameText = (EditText) findViewById(R.id.nameText);
        cvcText = (EditText) findViewById(R.id.cvcText);

        btnTokenize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean haveInternet = isOnline();

                if (haveInternet) {

                    String name = nameText.getText().toString();
                    String number = numberText.getText().toString();
                    String cvc = cvcText.getText().toString();

                    if (number.matches(tarjetaPattern))
                    {

                        if (cvc.matches(cvcPattern))
                        {
                            if (name.length() > 0)
                            {
                                Conekta.setPublicKey("key_H9xwdHFLt9Vy9vYMh1DP3zw");
                                Conekta.setApiVersion("0.3.0");
                                Conekta.collectDevice(activity);

                                Card card = new Card(name, number, cvc, month, year);
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


                                            JSONObject json = servicio.ProcessPay(String.valueOf(price) + "00", data.getString("id"), nombre, email, phone);

                                            try {
                                                if (json.getString(KEY_SUCCESS) != null) {
                                                    String res = json.getString(KEY_SUCCESS);
                                                    if (Integer.parseInt(res) == 1) {
                                                        Intent i = new Intent(Form.this, Compra_Final.class);
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

                            }
                            else
                            {
                                Snackbar.make(view, "El nombre es requerido", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Snackbar.make(view, "Faltan numeros en el codigo", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Snackbar.make(view, "Faltan numeros en la tarjeta", Snackbar.LENGTH_SHORT).show();
                    }


                } else {
                    Snackbar.make(view, "You don't have internet", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //String item = parent.getItemAtPosition(position).toString();

        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.spinnerMonth)
        {
            month = spinner.getSelectedItem().toString();
        }
        else if(spinner.getId() == R.id.spinnerYear)
        {
            year = spinner.getSelectedItem().toString();
        }
        // Showing selected spinner item
       /* Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();*/
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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
