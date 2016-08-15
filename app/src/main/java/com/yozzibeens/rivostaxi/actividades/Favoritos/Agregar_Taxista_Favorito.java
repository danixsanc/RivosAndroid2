package com.YozziBeens.rivostaxi.actividades.Favoritos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import android.app.AlertDialog;

import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.adaptadores.AddFavoriteCabbie;
import com.YozziBeens.rivostaxi.adaptadores.PendingHistory;
import com.YozziBeens.rivostaxi.controlador.Favorite_CabbieController;
import com.YozziBeens.rivostaxi.controlador.Favorite_PlaceController;
import com.YozziBeens.rivostaxi.controlador.HistorialPendienteController;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.modelo.Favorite_Cabbie;
import com.YozziBeens.rivostaxi.modelo.Favorite_Place;
import com.YozziBeens.rivostaxi.modelo.HistorialPendiente;
import com.YozziBeens.rivostaxi.modelosApp.TaxistasQueAtendieron;
import com.YozziBeens.rivostaxi.respuesta.ResultadoAgregarLugarFavorito;
import com.YozziBeens.rivostaxi.respuesta.ResultadoAgregarTaxistaFavorito;
import com.YozziBeens.rivostaxi.respuesta.ResultadoHistorialPendienteCliente;
import com.YozziBeens.rivostaxi.respuesta.ResultadoTaxistasQueTeAtendieron;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudAgregarTaxistaFavorito;
import com.YozziBeens.rivostaxi.solicitud.SolicitudHistorialPendienteCliente;
import com.YozziBeens.rivostaxi.solicitud.SolicitudTaxistasQueTeAtendieron;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.YozziBeens.rivostaxi.utilerias.Servicio;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by danixsanc on 29/10/2015.
 */
public class Agregar_Taxista_Favorito extends AppCompatActivity {


    private static String KEY_SUCCESS = "Success";
    private static String KEY_ERROR = "Error";
    private static String KEY_ERROR_MSG = "Error_Msg";

    TextView txt_no_data_detected, txt_taxistas;

    private Gson gson;
    private ProgressDialog progressdialog;
    private ResultadoTaxistasQueTeAtendieron resultadoTaxistasQueTeAtendieron;
    private ResultadoAgregarTaxistaFavorito resultadoAgregarTaxistaFavorito;
    private Favorite_CabbieController favorite_cabbieController;


    Servicio servicio = new Servicio();

    ListView addfavoritecabbieList;
    AddFavoriteCabbieCustomAdapter addfavoritecabbieAdapter;
    ArrayList<AddFavoriteCabbie> addfavoritecabbieArray = new ArrayList<AddFavoriteCabbie>();
    int cabbie_id[] = new int[0];

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_taxista_favorito);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        favorite_cabbieController = new Favorite_CabbieController(this);
        //----------TIPO DE FUENTE-----------------------------------------------------------------------------
        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");

        txt_no_data_detected = (TextView) findViewById(R.id.txt_no_data_detected);
        txt_no_data_detected.setTypeface(RobotoCondensed_Regular);
        txt_taxistas = (TextView) findViewById(R.id.txt_taxistas);
        txt_taxistas.setTypeface(RobotoCondensed_Regular);


        /*addfavoritecabbieList = (ListView) findViewById(R.id.list_cabbie_history);
        addfavoritecabbieAdapter = new AddFavoriteCabbieCustomAdapter(getApplicationContext(), R.layout.row_agregar_taxista_favorito, addfavoritecabbieArray);
        addfavoritecabbieList.setItemsCanFocus(false);
        addfavoritecabbieList.setAdapter(addfavoritecabbieAdapter);*/


        //Preferencias preferencias = new Preferencias(getApplicationContext());
        //String Client_Id = preferencias.getClient_Id();

        //cargarDatosCabbiesRequest(Client_Id);

        this.gson = new Gson();

        Preferencias preferencias = new Preferencias(getApplicationContext());
        String Client_Id = preferencias.getClient_Id();

        SolicitudTaxistasQueTeAtendieron oData = new SolicitudTaxistasQueTeAtendieron();
        oData.setClient_Id(Client_Id);
        TaxistasQueTeAtendieronWebService(gson.toJson(oData));




        /*thisContext = this;
        callbackManager = CallbackManager.Factory.create();
        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(this.getAssets(), "RobotoCondensed-Regular.ttf");

        add_favorite_cabbie = (MaterialEditText) findViewById(R.id.edtxt_add_favorite_cabbie);
        add_favorite_cabbie.setTypeface(RobotoCondensed_Regular);
*/


        //final ListView list_cabbie_history = (ListView) findViewById(R.id.list_cabbie_history);
        //adapter = new ListViewAdapter(this, titulo, imagenes);
        //list_cabbie_history.setAdapter(adapter);
        //final String[] finalCodes = codes;
/*
        list_cabbie_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity().getApplicationContext(), "Manten presionado para elimiar.", Toast.LENGTH_SHORT).show();
                add_favorite_cabbie.setText("");
                add_favorite_cabbie.setText(finalCodes[i]);
            }
        });*//*

        btn_add_favorite_cabbie = (Button) findViewById(R.id.btn_add_favorite_cabbie);
        btn_add_favorite_cabbie.setTypeface(RobotoCondensed_Regular);
        btn_add_favorite_cabbie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {

                    String Cabbie_Id = add_favorite_cabbie.getText().toString();
                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    Contact c = db.getContact(1);
                    String Client_Id = c.getClientId();

                    UserFunctions userFunctions = new UserFunctions();
                    JSONObject json = userFunctions.setFavoriteCabbie(Client_Id, Cabbie_Id);

                    if (json.getString(KEY_SUCCESS) != null) {
                        String res = json.getString(KEY_SUCCESS);
                        if (Integer.parseInt(res) == 1) {

                            Toast.makeText(getApplicationContext(), "Taxista agregado correctamente", Toast.LENGTH_LONG).show();
                            Bundle bundle=new Bundle();
                            bundle.putString("value", "0");
                            //set Fragmentclass Arguments
                            Favorite_Cabbie fragobj=new Favorite_Cabbie();
                            fragobj.setArguments(bundle);
                        }
                        else if (json.getString(KEY_ERROR) != null){
                            res = json.getString(KEY_ERROR);
                            if (Integer.parseInt(res) == 7){
                                Toast.makeText(getApplicationContext(), "Ese taxista ya esta registrado.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/
    }



    private void TaxistasQueTeAtendieronWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.GetCabbieHistoryWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
                progressdialog = new ProgressDialog(Agregar_Taxista_Favorito.this);
                progressdialog.setMessage("Obteniendo Datos, espere");
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
                        resultadoTaxistasQueTeAtendieron = gson.fromJson(result.get("Resultado").toString(), ResultadoTaxistasQueTeAtendieron.class);
                        if (!resultadoAgregarTaxistaFavorito.isError())
                        {
                            ArrayList<TaxistasQueAtendieron> taxistasQueAtendieron = resultadoTaxistasQueTeAtendieron.getData();


                            cabbie_id = new int[taxistasQueAtendieron.size()];
                            for (int i=0; i < taxistasQueAtendieron.size(); i++)
                            {
                                String id = taxistasQueAtendieron.get(i).getCabbie_Id();
                                String name = taxistasQueAtendieron.get(i).getName();

                                addfavoritecabbieArray.add(new AddFavoriteCabbie(name));
                                cabbie_id[i] = Integer.valueOf(taxistasQueAtendieron.get(i).getCabbie_Id());
                            }


                            addfavoritecabbieList = (ListView) findViewById(R.id.list_cabbie_history);
                            addfavoritecabbieAdapter = new AddFavoriteCabbieCustomAdapter(getApplicationContext(), R.layout.row_agregar_taxista_favorito, addfavoritecabbieArray);
                            addfavoritecabbieList.setItemsCanFocus(false);
                            addfavoritecabbieList.setAdapter(addfavoritecabbieAdapter);

                            txt_no_data_detected.setVisibility(View.GONE);
                            addfavoritecabbieList.setVisibility(View.VISIBLE);

                            progressdialog.dismiss();
                        }
                        else
                        {
                            txt_no_data_detected.setVisibility(View.VISIBLE);
                            addfavoritecabbieList.setVisibility(View.GONE);
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
                progressdialog.dismiss();
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
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }




    public class AddFavoriteCabbieCustomAdapter extends ArrayAdapter<AddFavoriteCabbie> {
        Context context;
        int layoutResourceId;
        ArrayList<AddFavoriteCabbie> data = new ArrayList<AddFavoriteCabbie>();

        public AddFavoriteCabbieCustomAdapter(Context context, int layoutResourceId, ArrayList<AddFavoriteCabbie> data) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = convertView;
            UserHolder holder = null;

            if (row == null) {
                Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");
                //LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new UserHolder();
                holder.textName = (TextView) row.findViewById(R.id.textView1);
                holder.textName.setTypeface(RobotoCondensed_Regular);
                holder.btnEdit = (ImageButton) row.findViewById(R.id.button1);


                //holder.btnDelete = (ImageButton) row.findViewById(R.id.button2);
                row.setTag(holder);
            } else {
                holder = (UserHolder) row.getTag();
            }
            final AddFavoriteCabbie addfavoriteCabbie = data.get(position);
            holder.textName.setText(addfavoriteCabbie.getAdd_Favorite_Cabbie());
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub


                        String Cabbie_Id = String.valueOf(cabbie_id[position]);

                        Preferencias preferencias = new Preferencias(getApplicationContext());
                        String Client_Id = preferencias.getClient_Id();

                        SolicitudAgregarTaxistaFavorito oData = new SolicitudAgregarTaxistaFavorito();
                        oData.setClient_Id(Client_Id);
                        oData.setCabbie_Id(Cabbie_Id);
                        AgregarTaxistaFavoritoWebService(gson.toJson(oData));

                }
            });
            return row;

        }

        class UserHolder {
            TextView textName;
            ImageButton btnEdit;

            //ImageButton btnDelete;
        }
    }

    private void AgregarTaxistaFavoritoWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(Agregar_Taxista_Favorito.this, WebService.SetFavoriteCabbieWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
            }

            @Override
            public void onTaskDownloadedFinished(HashMap<String, Object> result) {
                try {
                    int statusCode = Integer.parseInt(result.get("StatusCode").toString());
                    if (statusCode == 0) {
                        resultadoAgregarTaxistaFavorito = gson.fromJson(result.get("Resultado").toString(), ResultadoAgregarTaxistaFavorito.class);
                        if ((!resultadoAgregarTaxistaFavorito.isError()) && resultadoAgregarTaxistaFavorito.getData() != null) {
                            favorite_cabbieController.guardarOActualizarFavorite_Cabbie(resultadoAgregarTaxistaFavorito.getData());
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

                String messageError = resultadoAgregarTaxistaFavorito.getMessage();
                AlertDialog.Builder dialog = new AlertDialog.Builder(Agregar_Taxista_Favorito.this, R.style.AppCompatAlertDialogStyle);
                dialog.setMessage(messageError);
                dialog.setCancelable(true);
                dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();



            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
            }
        });
        servicioAsyncService.execute();
    }

}
