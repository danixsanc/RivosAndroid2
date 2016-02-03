package com.yozzibeens.rivostaxi.actividades.Favoritos;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.yozzibeens.rivostaxi.R;
import com.yozzibeens.rivostaxi.adaptadores.AddFavoriteCabbie;
import com.yozzibeens.rivostaxi.controlador.Favorite_CabbieController;
import com.yozzibeens.rivostaxi.modelo.Favorite_Cabbie;
import com.yozzibeens.rivostaxi.utilerias.Preferencias;
import com.yozzibeens.rivostaxi.utilerias.Servicio;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by danixsanc on 29/10/2015.
 */
public class Agregar_Taxista_Favorito extends AppCompatActivity {


    private static String KEY_SUCCESS = "Success";
    private static String KEY_ERROR = "Error";
    private static String KEY_ERROR_MSG = "Error_Msg";

    TextView txt_no_data_detected, txt_taxistas;


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

        //----------TIPO DE FUENTE-----------------------------------------------------------------------------
        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");

        txt_no_data_detected = (TextView) findViewById(R.id.txt_no_data_detected);
        txt_no_data_detected.setTypeface(RobotoCondensed_Regular);
        txt_taxistas = (TextView) findViewById(R.id.txt_taxistas);
        txt_taxistas.setTypeface(RobotoCondensed_Regular);


        addfavoritecabbieList = (ListView) findViewById(R.id.list_cabbie_history);
        addfavoritecabbieAdapter = new AddFavoriteCabbieCustomAdapter(getApplicationContext(), R.layout.row_agregar_taxista_favorito, addfavoritecabbieArray);
        addfavoritecabbieList.setItemsCanFocus(false);
        addfavoritecabbieList.setAdapter(addfavoritecabbieAdapter);


        Preferencias preferencias = new Preferencias(getApplicationContext());
        String Client_Id = preferencias.getClient_Id();

        cargarDatosCabbiesRequest(Client_Id);





/*
        thisContext = this;
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

    public void cargarDatosCabbiesRequest(String Client_Id)
    {


        final JSONObject json = servicio.getCabbieHistory(Client_Id);

        try {

            if (json.getString(KEY_SUCCESS) != null) {
                String res = json.getString(KEY_SUCCESS);
                if (Integer.parseInt(res) == 1)
                {
                    cabbie_id = new int[Integer.valueOf(json.getInt("num"))];
                    for (int i = 0; i < cabbie_id.length; i++)
                    {
                        JSONObject json_user = json.getJSONObject("Cabbie_Id"+(i+1));
                        addfavoritecabbieArray.add(new AddFavoriteCabbie("Codigo: "+json_user.getString("Cabbie_Id")
                                + "\nNombre: " + json_user.getString("Name")));

                        cabbie_id[i] = Integer.parseInt(json_user.getString("Cabbie_Id"));
                    }
                }
                else {
                    txt_no_data_detected.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                //LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new UserHolder();
                holder.textName = (TextView) row.findViewById(R.id.textView1);
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
                    try {

                        //String Cabbie_Id = add_favorite_cabbie.getText().toString();

                        Preferencias preferencias = new Preferencias(getApplicationContext());
                        String Client_Id = preferencias.getClient_Id();

                        JSONObject json = servicio.setFavoriteCabbie(Client_Id, String.valueOf(cabbie_id[position]));

                        if (json.getString(KEY_SUCCESS) != null) {
                            String res = json.getString(KEY_SUCCESS);
                            if (Integer.parseInt(res) == 1) {
                                Snackbar.make(v, "Taxista agregado correctamente", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                Favorite_Cabbie favorite_cabbie = new Favorite_Cabbie(null, json.getString("Cabbie_Id"),
                                        json.getString("Name"), json.getString("Company"));
                                Favorite_CabbieController favorite_cabbieController = new Favorite_CabbieController(getApplicationContext());
                                favorite_cabbieController.guardarFavorite_Cabbie(favorite_cabbie);
                            }
                            else if (json.getString(KEY_ERROR) != null){
                                res = json.getString(KEY_ERROR);
                                if (Integer.parseInt(res) == 7){
                                    Snackbar.make(v, "Ese taxista ya esta registrado", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            /*holder.btnDelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // TODO Auto-generated method stub
                    Log.i("Delete Button Clicked", "**********");
                    Toast.makeText(context, "Delete button Clicked " + position, Toast.LENGTH_LONG).show();
                }
            });*/
            return row;

        }

        class UserHolder {
            TextView textName;
            ImageButton btnEdit;

            //ImageButton btnDelete;
        }
    }

}
