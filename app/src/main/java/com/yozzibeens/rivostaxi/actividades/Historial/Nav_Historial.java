package com.YozziBeens.rivostaxi.actividades.Historial;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
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
import com.YozziBeens.rivostaxi.actividades.Ayuda.Nav_Ayuda;
import com.YozziBeens.rivostaxi.adaptadores.AdaptadorHistorial;
import com.YozziBeens.rivostaxi.controlador.HistorialController;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.modelo.Historial;
import com.YozziBeens.rivostaxi.respuesta.ResultadoEliminarHistorial;
import com.YozziBeens.rivostaxi.respuesta.ResultadoMensajeAyuda;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudEliminarHistorial;
import com.YozziBeens.rivostaxi.solicitud.SolicitudMensajeAyuda;
import com.YozziBeens.rivostaxi.utilerias.FechasBD;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.YozziBeens.rivostaxi.utilerias.Servicio;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by danixsanc on 12/01/2016.
 */
public class Nav_Historial extends AppCompatActivity {

    private static String KEY_SUCCESS = "Success";

    TextView txt_no_data_detected;

    ListView historyList;
    HistoryCustomAdapter historyAdapter;
    ArrayList<AdaptadorHistorial> historyArray = new ArrayList<AdaptadorHistorial>();

    int request_id[] = new int[0];

    private Gson gson;
    private ProgressDialog progressdialog;
    private ResultadoEliminarHistorial resultadoEliminarHistorial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_historial);

        this.gson = new Gson();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Preferencias preferencias = new Preferencias(getApplicationContext());
        String Client_Id = preferencias.getClient_Id();
        int val;

        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");

        txt_no_data_detected = (TextView) findViewById(R.id.txt_no_data_detected2);
        txt_no_data_detected.setTypeface(RobotoCondensed_Regular);



        HistorialController historialController = new HistorialController(getApplicationContext());
        List<Historial> historialList = historialController.obtenerHistorial();

        if (historialList != null)
        {
            for (int i=0; i < historialList.size(); i++)
            {
                String id = historialList.get(i).getRequest_Id();
                String fecha = historialList.get(i).getDate();
                historyArray.add(new AdaptadorHistorial(id, fecha));
            }
        }




        historyAdapter = new HistoryCustomAdapter(getApplicationContext(), R.layout.row_history, historyArray);
        historyList = (ListView) findViewById(R.id.listView);
        historyList.setItemsCanFocus(false);
        historyList.setAdapter(historyAdapter);


        if (historyArray.size() == 0 ){
            txt_no_data_detected.setVisibility(View.VISIBLE);
            historyList.setVisibility(View.GONE);
        }
        else
        {
            txt_no_data_detected.setVisibility(View.GONE);
            historyList.setVisibility(View.VISIBLE);
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


    public class HistoryCustomAdapter extends ArrayAdapter<AdaptadorHistorial> {
        Context context;
        int layoutResourceId;
        ArrayList<AdaptadorHistorial> data = new ArrayList<AdaptadorHistorial>();

        public HistoryCustomAdapter(Context context, int layoutResourceId,
                                    ArrayList<AdaptadorHistorial> data) {
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

                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new UserHolder();
                holder.textName = (TextView) row.findViewById(R.id.textView1);
                holder.textName.setTypeface(RobotoCondensed_Regular);
                holder.btnOptions = (ImageButton) row.findViewById(R.id.btnOptions);
                holder.txtIdHistorial = (TextView) row.findViewById(R.id.txtIdHistorial);
                holder.txtIdHistorial.setTypeface(RobotoCondensed_Regular);
                //holder.btnDelete = (ImageButton) row.findViewById(R.id.button2);
                row.setTag(holder);

                final UserHolder finalHolder = holder;
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent login = new Intent(Nav_Historial.this, Historial_Detalle.class);
                        String request_id=finalHolder.txtIdHistorial.getText().toString();
                        login.putExtra("request_id", request_id);
                        startActivity(login);
                    }
                });

            } else {
                holder = (UserHolder) row.getTag();
            }


            AdaptadorHistorial history = data.get(position);


            String date = history.getHistory();
            FechasBD fechasBD = new FechasBD();
            String fecha = fechasBD.ObtenerFecha(date);
            holder.textName.setText(fecha);
            holder.txtIdHistorial.setText(history.getHistorialId());
            final UserHolder finalHolder1 = holder;
            holder.btnOptions.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final CharSequence[] options = {"Eliminar", "Cancelar"};
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Nav_Historial.this);

                    builder.setTitle("Elige una opcion");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int seleccion) {
                            if  (options[seleccion] == "Eliminar") {

                                Preferencias preferencias = new Preferencias(getApplicationContext());
                                String Client_Id = preferencias.getClient_Id();
                                String request_id = finalHolder1.txtIdHistorial.getText().toString();

                                SolicitudEliminarHistorial oData = new SolicitudEliminarHistorial();
                                oData.setClient_Id(Client_Id);
                                oData.setRequest_Id(request_id);
                                DeleteHistorialWebService(gson.toJson(oData));

                                HistorialController historialController = new HistorialController(getApplicationContext());
                                Historial historial = historialController.obtenerHistorialPorRequestId(request_id);

                                historialController.eliminarHistorial(historial);
                                historyArray.remove(position);
                                historyAdapter.notifyDataSetChanged();

                                if (historyArray.size() == 0) {
                                    txt_no_data_detected.setVisibility(View.VISIBLE);
                                    historyList.setVisibility(View.GONE);
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
            TextView textName;
            TextView txtIdHistorial;
            ImageButton btnOptions;
        }
    }

    private void DeleteHistorialWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.DeleteHistoryClientWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
                progressdialog = new ProgressDialog(Nav_Historial.this);
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
                        resultadoEliminarHistorial = gson.fromJson(result.get("Resultado").toString(), ResultadoEliminarHistorial.class);
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
                String messageError = resultadoEliminarHistorial.getMessage();
                AlertDialog.Builder dialog = new AlertDialog.Builder(Nav_Historial.this, R.style.AppCompatAlertDialogStyle);
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
