package com.YozziBeens.rivostaxi.actividades.Proceso;


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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.YozziBeens.rivostaxi.actividades.Ayuda.Nav_Ayuda;
import com.YozziBeens.rivostaxi.adaptadores.AdaptadorHistorial;
import com.YozziBeens.rivostaxi.adaptadores.AdaptadorHistorialPendiente;
import com.YozziBeens.rivostaxi.controlador.HistorialController;
import com.YozziBeens.rivostaxi.controlador.HistorialPendienteController;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.modelo.Historial;
import com.YozziBeens.rivostaxi.modelo.HistorialPendiente;
import com.YozziBeens.rivostaxi.respuesta.ResultadoHistorialCliente;
import com.YozziBeens.rivostaxi.respuesta.ResultadoHistorialPendienteCliente;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudHistorialCliente;
import com.YozziBeens.rivostaxi.solicitud.SolicitudHistorialPendienteCliente;
import com.YozziBeens.rivostaxi.solicitud.SolicitudLoginFacebook;
import com.facebook.CallbackManager;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.adaptadores.PendingHistory;
import com.YozziBeens.rivostaxi.utilerias.FechasBD;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.YozziBeens.rivostaxi.utilerias.Servicio;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by danixsanc on 18/01/2016.
 */
public class Nav_Proceso extends AppCompatActivity {


    private Gson gson;
    private ResultadoHistorialPendienteCliente resultadoHistorialPendienteCliente;
    private HistorialPendienteController historialPendienteController;

    TextView txt_no_data_detected;
    int val;
    String Client_Id;
    private ProgressDialog progressdialog;


    ListView pendinghistoryList;
    PendingHistoryCustomAdapter pendinghistoryAdapter;
    ArrayList<PendingHistory> pendinghistoryArray = new ArrayList<PendingHistory>();
    int request_id[] = new int[0];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_proceso);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.gson = new Gson();

        Preferencias preferencias = new Preferencias(getApplicationContext());
        Client_Id = preferencias.getClient_Id();

        SolicitudHistorialPendienteCliente oData = new SolicitudHistorialPendienteCliente();
        oData.setClient_Id(Client_Id);
        HistorialPendienteClienteWebService(gson.toJson(oData));

        historialPendienteController = new HistorialPendienteController(this);

        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");

        txt_no_data_detected = (TextView) findViewById(R.id.txt_no_data_detected);
        txt_no_data_detected.setTypeface(RobotoCondensed_Regular);
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



    private void HistorialPendienteClienteWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(this, WebService.GetClientHistoryPendingWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
                progressdialog = new ProgressDialog(Nav_Proceso.this);
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
                        resultadoHistorialPendienteCliente = gson.fromJson(result.get("Resultado").toString(), ResultadoHistorialPendienteCliente.class);
                        if ((!resultadoHistorialPendienteCliente.isError()) && resultadoHistorialPendienteCliente.getData() != null) {
                            historialPendienteController.eliminarTodo();
                            historialPendienteController.guardarOActualizarHistorialPendiente(resultadoHistorialPendienteCliente.getData());
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



                HistorialPendienteController historialPendienteController = new HistorialPendienteController(getApplicationContext());
                List<HistorialPendiente> historialList = historialPendienteController.obtenerHistorialPendiente();

                request_id = new int[historialList.size()];
                for (int i=0; i < historialList.size(); i++)
                {
                    String id = historialList.get(i).getRequest_Id();
                    String fecha = historialList.get(i).getDate();
                    pendinghistoryArray.add(new PendingHistory(id, fecha));
                    request_id[i] = Integer.valueOf(historialList.get(i).getRequest_Id());
                }



                pendinghistoryList = (ListView) findViewById(R.id.list_pending_history);
                pendinghistoryAdapter = new PendingHistoryCustomAdapter(getApplicationContext(), R.layout.row_pending_history, pendinghistoryArray);
                pendinghistoryList.setItemsCanFocus(false);
                pendinghistoryList.setAdapter(pendinghistoryAdapter);
                progressdialog.dismiss();


            }

            @Override
            public void onTaskCancelled(HashMap<String, Object> result) {
                progressdialog.dismiss();
            }
        });
        servicioAsyncService.execute();
    }







    public class PendingHistoryCustomAdapter extends ArrayAdapter<PendingHistory> {
        Context context;
        int layoutResourceId;
        ArrayList<PendingHistory> data = new ArrayList<PendingHistory>();

        public PendingHistoryCustomAdapter(Context context, int layoutResourceId, ArrayList<PendingHistory> data) {
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
                //holder.btnEdit = (ImageButton) row.findViewById(R.id.button1);


                //holder.btnDelete = (ImageButton) row.findViewById(R.id.button2);
                row.setTag(holder);
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(),Pending_History_Details.class);
                        i.putExtra("Request_Id", String.valueOf(request_id[position]));
                        startActivity(i);
                    }
                });

            } else {
                holder = (UserHolder) row.getTag();
            }
            final PendingHistory pendingHistory = data.get(position);
            String date = pendingHistory.getPending_History();
            FechasBD fechasBD = new FechasBD();
            String fecha = fechasBD.ObtenerFecha(date);

            holder.textName.setText(fecha);
           /* holder.btnEdit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {



                }
            });*/
            return row;

        }

        class UserHolder {
            TextView textName;
        }
    }


}
