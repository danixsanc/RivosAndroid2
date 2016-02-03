package com.yozzibeens.rivostaxi.actividades.Favoritos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.yozzibeens.rivostaxi.R;
import com.yozzibeens.rivostaxi.adaptadores.AdaptadorTaxistaFavorito;
import com.yozzibeens.rivostaxi.controlador.Favorite_CabbieController;
import com.yozzibeens.rivostaxi.modelo.Favorite_Cabbie;
import com.yozzibeens.rivostaxi.utilerias.Preferencias;
import com.yozzibeens.rivostaxi.utilerias.Servicio;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danixsanc on 16/01/2016.
 */
public class TaxistaFavorito  extends Fragment{
    View rootview;
    ListView favoritecabbieList;
    FavoriteCabbieCustomAdapter favoritecabbieAdapter;
    ArrayList<AdaptadorTaxistaFavorito> favoritecabbieArray = new ArrayList<AdaptadorTaxistaFavorito>();
    int cabbie_id[] = new int[0];
    TextView txt_no_data_detected;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.favorite_cabbie, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        txt_no_data_detected = (TextView) rootview.findViewById(R.id.txt_no_data_detected);


        favoritecabbieList = (ListView) rootview.findViewById(R.id.list_favorite_cabbie);
        favoritecabbieAdapter = new FavoriteCabbieCustomAdapter(getActivity(), R.layout.row_favorite_cabbie, favoritecabbieArray);
        favoritecabbieList.setItemsCanFocus(false);
        favoritecabbieList.setAdapter(favoritecabbieAdapter);


        Favorite_CabbieController favorite_cabbieController = new Favorite_CabbieController(getActivity().getApplicationContext());
        List<Favorite_Cabbie> FCabbieList = favorite_cabbieController.obtenerFavorite_Cabbie();

        for (int i = 0; i < FCabbieList.size(); i++)
        {
            String CName = FCabbieList.get(i).getName();
            String cId = FCabbieList.get(i).getCabbieFavoriteId();
            favoritecabbieArray.add(new AdaptadorTaxistaFavorito(cId, CName));
        }

        FloatingActionButton fab_favorite_cabbie = (FloatingActionButton) rootview.findViewById(R.id.fab_favorite_cabbie);
        fab_favorite_cabbie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Agregar_Taxista_Favorito.class);
                startActivityForResult(intent, 1002);

            }
        });

        if (favoritecabbieArray.size() == 0) {
            txt_no_data_detected.setVisibility(View.VISIBLE);
            favoritecabbieList.setVisibility(View.GONE);
        } else {
            txt_no_data_detected.setVisibility(View.GONE);
            favoritecabbieList.setVisibility(View.VISIBLE);
        }


        return rootview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1002:
                if (resultCode == Activity.RESULT_OK) {
                    favoritecabbieArray.clear();
                    Favorite_CabbieController favorite_cabbieController = new Favorite_CabbieController(getActivity());
                    List<Favorite_Cabbie> fcalist = favorite_cabbieController.obtenerFavorite_Cabbie();
                    for (int i = 0; i<fcalist.size();i++){
                        String cabbieid2 = fcalist.get(i).getCabbieFavoriteId();
                        String cabbiename2 = fcalist.get(i).getName();

                        favoritecabbieArray.add(new AdaptadorTaxistaFavorito(cabbieid2, cabbiename2));
                    }

                    favoritecabbieAdapter.notifyDataSetChanged();
                }
                break;
        }
    }




    public class FavoriteCabbieCustomAdapter extends ArrayAdapter<AdaptadorTaxistaFavorito> {
        Context context;
        int layoutResourceId;
        ArrayList<AdaptadorTaxistaFavorito> data = new ArrayList<AdaptadorTaxistaFavorito>();

        public FavoriteCabbieCustomAdapter(Context context, int layoutResourceId, ArrayList<AdaptadorTaxistaFavorito> data) {
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
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new UserHolder();
                holder.textName = (TextView) row.findViewById(R.id.textView1);
                holder.txtId = (TextView) row.findViewById(R.id.txtIdCabbie);
                //holder.btnEdit = (ImageButton) row.findViewById(R.id.button1);
                holder.btnOptions = (ImageButton) row.findViewById(R.id.btnOptions);
                row.setTag(holder);
            } else {
                holder = (UserHolder) row.getTag();
            }
            AdaptadorTaxistaFavorito favoriteCabbie = data.get(position);
            holder.textName.setText(favoriteCabbie.getFavorite_Cabbie());
            holder.txtId.setText(favoriteCabbie.getFavoriteCabbieId());
            /*holder.btnEdit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Log.i("Edit Button Clicked", "**********");
                    Snackbar.make(rootview, "Edit button Clicked " + cabbie_id[position], Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });*/
            final UserHolder finalHolder = holder;
            holder.btnOptions.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    final CharSequence[] options = { "Eliminar", "Cancelar"};
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle("Elige una opcion");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int seleccion) {
                            if (options[seleccion] == "Eliminar") {

                                String cabbieid = finalHolder.txtId.getText().toString();
                                Preferencias preferencias = new Preferencias(getActivity().getApplicationContext());
                                String clienteid = preferencias.getClient_Id();

                                Servicio servicio = new Servicio();
                                servicio.deleteFavoriteCabbie(clienteid, cabbieid);

                                Favorite_CabbieController favorite_cabbieController = new Favorite_CabbieController(getActivity().getApplicationContext());
                                Favorite_Cabbie favorite_cabbie;
                                favorite_cabbie = favorite_cabbieController.obtenerFavorite_CabbiePorCabbieId(cabbieid);
                                favorite_cabbieController.eliminarFavorite_Cabbie(favorite_cabbie);
                                favoritecabbieArray.remove(position);
                                favoritecabbieAdapter.notifyDataSetChanged();
                                if (favoritecabbieArray.size() == 0) {
                                    txt_no_data_detected.setVisibility(View.VISIBLE);
                                    favoritecabbieList.setVisibility(View.GONE);
                                } else {
                                    txt_no_data_detected.setVisibility(View.GONE);
                                    favoritecabbieList.setVisibility(View.VISIBLE);
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
            TextView txtId;
            ImageButton btnOptions;
        }
    }

}