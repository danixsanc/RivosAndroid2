package com.YozziBeens.rivostaxi.actividades.Tarjetas;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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

import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.adaptadores.AdaptadorTarjetas;
import com.YozziBeens.rivostaxi.controlador.HistorialPendienteController;
import com.YozziBeens.rivostaxi.controlador.TarjetaController;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.modelo.Tarjeta;
import com.YozziBeens.rivostaxi.respuesta.ResultadoEliminarTarjeta;
import com.YozziBeens.rivostaxi.respuesta.ResultadoTarjetas;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudEliminarTarjeta;
import com.YozziBeens.rivostaxi.solicitud.SolicitudTarjetas;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.YozziBeens.rivostaxi.utilerias.TarjetasBD;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by danixsanc on 18/01/2016.
 */
public class Nav_Tarjetas extends AppCompatActivity {

    private Gson gson;
    private ResultadoTarjetas resultadoTarjetas;
    private HistorialPendienteController historialPendienteController;

    TextView txt_no_data_detected;
    int val;
    String Client_Id;
    private ProgressDialog progressdialog;

    ImageButton fab_add_card;

    ResultadoEliminarTarjeta resultadoEliminarTarjeta;


    ListView tarjetasList;
    TarjetasCustomAdapter tarjetasAdapter;
    ArrayList<AdaptadorTarjetas> tarjetasArray = new ArrayList<AdaptadorTarjetas>();
    int request_id[] = new int[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_tarjetas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.gson = new Gson();

        fab_add_card = (ImageButton) findViewById(R.id.fab_add_card);
        fab_add_card.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Nav_Tarjetas.this, Add_Card.class);
                startActivityForResult(intent, 1003);
            }
        });


        Preferencias preferencias = new Preferencias(getApplicationContext());
        Client_Id = preferencias.getClient_Id();

        SolicitudTarjetas oData = new SolicitudTarjetas();
        oData.setClient_Id(Client_Id);
        ObtenerTarjetasWebService(gson.toJson(oData));


        historialPendienteController = new HistorialPendienteController(this);


        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");

        txt_no_data_detected = (TextView) findViewById(R.id.txt_no_data_detected);
        txt_no_data_detected.setTypeface(RobotoCondensed_Regular);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1003:
                if (resultCode == Activity.RESULT_OK) {
                    tarjetasArray.clear();
                    TarjetaController tarjetaController = new TarjetaController(this);
                    List<Tarjeta> tarjetaslista2 = tarjetaController.obtenerTarjeta();


                    for (int i=0; i < tarjetaslista2.size(); i++)
                    {
                        String id = String.valueOf(tarjetaslista2.get(i).getCard_Id());
                        String tarjeta = tarjetaslista2.get(i).getNumber_Card();

                        int cont = tarjeta.length();
                        int num = cont - 4;
                        String tarjetaSeg = tarjeta.substring(num, tarjeta.length());
                        String tarjF;
                        if (tarjeta.length() == 16)
                        {
                            tarjF = "**** **** **** " + tarjetaSeg;
                        }
                        else
                        {
                            tarjF = "**** **** " + tarjetaSeg;
                        }

                        tarjetasArray.add(new AdaptadorTarjetas(tarjF, id));
                        //request_id[i] = Integer.valueOf(String.valueOf(tarjetaslista2.get(i).getCard_Id()));
                    }

                    tarjetasAdapter.notifyDataSetChanged();
                }
                break;
        }
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


    private void ObtenerTarjetasWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.GetCardWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
                progressdialog = new ProgressDialog(Nav_Tarjetas.this);
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
                        resultadoTarjetas = gson.fromJson(result.get("Resultado").toString(), ResultadoTarjetas.class);
                        /*if ((!resultadoTarjetas.isError()) && resultadoTarjetas.getData() != null) {
                            *//*historialPendienteController.eliminarTodo();
                            historialPendienteController.guardarOActualizarHistorialPendiente(resultadoTarjetas.getData());*//*
                        }*/
                    }
                }
                catch (Exception error) {
                }
            }

            @Override
            public void onTaskUpdate(String result) {}

            @Override
            public void onTaskComplete(HashMap<String, Object> result) {



                //HistorialPendienteController historialPendienteController = new HistorialPendienteController(getApplicationContext());
                //List<HistorialPendiente> historialList = historialPendienteController.obtenerHistorialPendiente();

                TarjetaController tarjetaController = new TarjetaController(getApplicationContext());
                tarjetaController.eliminarTodo();
                tarjetaController.guardarOActualizarTarjeta(resultadoTarjetas.getData());

                List<Tarjeta> tarjetasLista = tarjetaController.obtenerTarjeta();

                request_id = new int[tarjetasLista.size()];
                for (int i=0; i < tarjetasLista.size(); i++)
                {
                    String id = String.valueOf(tarjetasLista.get(i).getCard_Id());
                    String tarjeta = tarjetasLista.get(i).getNumber_Card();

                    TarjetasBD tarjetasBD = new TarjetasBD();
                    String tarjF = tarjetasBD.ocultarTarjeta(tarjetasLista.get(i).getNumber_Card());
                    tarjetasArray.add(new AdaptadorTarjetas(tarjF, id));
                    request_id[i] = Integer.valueOf(String.valueOf(tarjetasLista.get(i).getCard_Id()));
                }



                tarjetasList = (ListView) findViewById(R.id.list_pending_history);
                tarjetasAdapter = new TarjetasCustomAdapter(getApplicationContext(), R.layout.row_tarjetas, tarjetasArray);
                tarjetasList.setItemsCanFocus(false);
                tarjetasList.setAdapter(tarjetasAdapter);
                progressdialog.dismiss();


            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
                progressdialog.dismiss();
            }
        });
        servicioAsyncService.execute();
    }





    public class TarjetasCustomAdapter extends ArrayAdapter<AdaptadorTarjetas> {
        Context context;
        int layoutResourceId;
        ArrayList<AdaptadorTarjetas> data = new ArrayList<AdaptadorTarjetas>();

        public TarjetasCustomAdapter(Context context, int layoutResourceId, ArrayList<AdaptadorTarjetas> data) {
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
                holder.textTarjeta = (TextView) row.findViewById(R.id.txtTarjeta);
                holder.textTarjeta.setTypeface(RobotoCondensed_Regular);
                holder.textIdTarjeta = (TextView) row.findViewById(R.id.txtIdTarjeta);
                holder.textIdTarjeta.setTypeface(RobotoCondensed_Regular);

                holder.btnOptions = (ImageButton) row.findViewById(R.id.btnOptions);

                row.setTag(holder);
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), Card_Details.class);
                        i.putExtra("Card_Id", String.valueOf(request_id[position]));
                        startActivity(i);
                    }
                });

            } else {
                holder = (UserHolder) row.getTag();
            }
            final AdaptadorTarjetas adaptadorTarjetas = data.get(position);
            holder.textTarjeta.setText(adaptadorTarjetas.getTarjeta());

            final UserHolder finalHolder = holder;
            holder.btnOptions.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final CharSequence[] options = {"Eliminar", "Cancelar"};
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Nav_Tarjetas.this);

                    builder.setTitle("Elige una opcion");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int seleccion) {
                            if  (options[seleccion] == "Eliminar") {

                                Preferencias preferencias = new Preferencias(getApplicationContext());
                                String Client_Id = preferencias.getClient_Id();
                                String request = String.valueOf(request_id[position]);

                                SolicitudEliminarTarjeta oData = new SolicitudEliminarTarjeta();
                                oData.setClient_Id(Client_Id);
                                oData.setCard(request);
                                DeleteCardWebService(gson.toJson(oData));

                                TarjetaController tarjetaController = new TarjetaController(getApplicationContext());
                                Tarjeta tarjeta = tarjetaController.obtenerTarjetaPorTarjetaId(Long.valueOf(request));

                                tarjetaController.eliminarTarjeta(tarjeta);

                                tarjetasArray.remove(position);
                                tarjetasAdapter.notifyDataSetChanged();

                                if (tarjetasArray.size() == 0) {
                                    txt_no_data_detected.setVisibility(View.VISIBLE);
                                    tarjetasList.setVisibility(View.GONE);
                                }

                            } else if (options[seleccion] == "Cancelar") {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();

                }
            });
            return row;

        }

        class UserHolder {
            TextView textTarjeta;
            TextView textIdTarjeta;
            ImageButton btnOptions;
        }
    }

    private void DeleteCardWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.DeleteCardWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
                progressdialog = new ProgressDialog(Nav_Tarjetas.this);
                progressdialog.setMessage("Eliminando, espere");
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
                        resultadoEliminarTarjeta = gson.fromJson(result.get("Resultado").toString(), ResultadoEliminarTarjeta.class);
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
                String messageError = resultadoEliminarTarjeta.getMessage();
                AlertDialog.Builder dialog = new AlertDialog.Builder(Nav_Tarjetas.this, R.style.AppCompatAlertDialogStyle);
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

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
                progressdialog.dismiss();
            }
        });
        servicioAsyncService.execute();
    }

}
