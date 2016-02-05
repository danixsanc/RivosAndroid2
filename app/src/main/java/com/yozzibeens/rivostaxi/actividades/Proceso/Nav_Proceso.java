package com.YozziBeens.rivostaxi.actividades.Proceso;


import android.content.Context;
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

import com.facebook.CallbackManager;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.adaptadores.PendingHistory;
import com.YozziBeens.rivostaxi.utilerias.FechasBD;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.YozziBeens.rivostaxi.utilerias.Servicio;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by danixsanc on 18/01/2016.
 */
public class Nav_Proceso extends AppCompatActivity {


    MaterialEditText add_favorite_cabbie;
    Button btn_add_favorite_cabbie;
    Context thisContext;
    CallbackManager callbackManager;
    private static String KEY_SUCCESS = "Success";
    private static String KEY_ERROR = "Error";
    private static String KEY_ERROR_MSG = "Error_Msg";
    ImageButton back_button;

    TextView txt_no_data_detected;
    int val;
    String Client_Id;


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


        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");

        txt_no_data_detected = (TextView) findViewById(R.id.txt_no_data_detected);
        txt_no_data_detected.setTypeface(RobotoCondensed_Regular);



        Preferencias preferencias = new Preferencias(getApplicationContext());
        Client_Id = preferencias.getClient_Id();


        Servicio servicio = new Servicio();
        final JSONObject json = servicio.getPendingRequest(Client_Id);

        try {



            if (json.getString(KEY_SUCCESS) != null) {
                String res = json.getString(KEY_SUCCESS);
                if (Integer.parseInt(res) == 1)
                {
                    val = json.getInt("num");
                    request_id = new int[val];
                    for (int i = 0; i < val; i++)
                    {
                        JSONObject json_user = json.getJSONObject("Request"+(i+1));
                        pendinghistoryArray.add(new PendingHistory(json_user.getString("Date")));
                        request_id[i] = json_user.getInt("Request_Id");
                    }
                }
                else {
                    txt_no_data_detected.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        pendinghistoryList = (ListView) findViewById(R.id.list_pending_history);
        pendinghistoryAdapter = new PendingHistoryCustomAdapter(getApplicationContext(), R.layout.row_pending_history, pendinghistoryArray);
        pendinghistoryList.setItemsCanFocus(false);
        pendinghistoryList.setAdapter(pendinghistoryAdapter);

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
