package com.yozzibeens.rivostaxi.actividades.Historial;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.yozzibeens.rivostaxi.R;
import com.yozzibeens.rivostaxi.adaptadores.AdaptadorHistorial;
import com.yozzibeens.rivostaxi.controlador.HistorialController;
import com.yozzibeens.rivostaxi.modelo.Client;
import com.yozzibeens.rivostaxi.modelo.Historial;
import com.yozzibeens.rivostaxi.utilerias.FechasBD;
import com.yozzibeens.rivostaxi.utilerias.Preferencias;
import com.yozzibeens.rivostaxi.utilerias.Servicio;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_historial);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Preferencias preferencias = new Preferencias(getApplicationContext());
        String Client_Id = preferencias.getClient_Id();
        int val;

        txt_no_data_detected = (TextView) findViewById(R.id.txt_no_data_detected2);



        HistorialController historialController = new HistorialController(getApplicationContext());
        List<Historial> historialList = historialController.obtenerHistorial();

        for (int i=0; i < historialList.size(); i++)
        {
            String id = historialList.get(i).getRequest_Id();
            String fecha = historialList.get(i).getDate();
            historyArray.add(new AdaptadorHistorial(id, fecha));
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

                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new UserHolder();
                holder.textName = (TextView) row.findViewById(R.id.textView1);
                holder.btnOptions = (ImageButton) row.findViewById(R.id.btnOptions);
                holder.txtIdHistorial = (TextView) row.findViewById(R.id.txtIdHistorial);
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

                                String request_id = finalHolder1.txtIdHistorial.getText().toString();
                                HistorialController historialController = new HistorialController(getApplicationContext());
                                Historial historial = historialController.obtenerHistorialPorRequestId(request_id);

                                historialController.eliminarHistorial(historial);
                                historyArray.remove(position);
                                historyAdapter.notifyDataSetChanged();
                                Preferencias preferencias = new Preferencias(getApplicationContext());
                                String Client_Id = preferencias.getClient_Id();
                                Servicio servicio = new Servicio();
                                servicio.Delete_History_Client(request_id, Client_Id);

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

}
