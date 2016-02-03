package com.yozzibeens.rivostaxi.actividades.Favoritos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.yozzibeens.rivostaxi.R;
import com.yozzibeens.rivostaxi.adaptadores.AdaptadorLugarFavorito;
import com.yozzibeens.rivostaxi.adaptadores.AdaptadorTaxistaFavorito;
import com.yozzibeens.rivostaxi.controlador.Favorite_CabbieController;
import com.yozzibeens.rivostaxi.controlador.Favorite_PlaceController;
import com.yozzibeens.rivostaxi.modelo.Favorite_Cabbie;
import com.yozzibeens.rivostaxi.modelo.Favorite_Place;
import com.yozzibeens.rivostaxi.utilerias.Preferencias;
import com.yozzibeens.rivostaxi.utilerias.Servicio;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danixsanc on 16/01/2016.
 */
public class LugarFavorito extends Fragment {

    View rootview;
    ListView favoriteplaceList;
    FavoritePlaceCustomAdapter favoriteplaceAdapter;
    ArrayList<AdaptadorLugarFavorito> favoriteplaceArray = new ArrayList<AdaptadorLugarFavorito>();
    TextView txt_no_data_detected;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.favorite_place, container, false);

        txt_no_data_detected = (TextView) rootview.findViewById(R.id.txt_no_data_detected);


        favoriteplaceList = (ListView) rootview.findViewById(R.id.list_favorite_place);
        favoriteplaceAdapter = new FavoritePlaceCustomAdapter(getActivity(), R.layout.row_favorite_place, favoriteplaceArray);
        favoriteplaceList.setItemsCanFocus(false);
        favoriteplaceList.setAdapter(favoriteplaceAdapter);

        Favorite_PlaceController favorite_placeController = new Favorite_PlaceController(getActivity().getApplicationContext());
        List<Favorite_Place> FPlaceList = favorite_placeController.obtenerFavorite_Place();

        for (int i = 0; i < FPlaceList.size(); i++)
        {
            String CName = FPlaceList.get(i).getName();
            String cId = FPlaceList.get(i).getPlaceFavoriteId();
            favoriteplaceArray.add(new AdaptadorLugarFavorito(cId, CName));
        }

        if (favoriteplaceArray.size() == 0) {
            txt_no_data_detected.setVisibility(View.VISIBLE);
            favoriteplaceList.setVisibility(View.GONE);
        } else {
            txt_no_data_detected.setVisibility(View.GONE);
            favoriteplaceList.setVisibility(View.VISIBLE);
        }

        FloatingActionButton floatingActionButton = (FloatingActionButton) rootview.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent( getActivity(), Agregar_Lugar_Favorito.class);
                intent.putExtra("Status", "agregar");
                startActivityForResult(intent, 1521);

                /*Intent intent = new Intent(getActivity(), Agregar_Lugar_Favorito.class);
                intent.putExtra("Status", "agregar");
                startActivity(intent);*/

            }
        });


        return rootview;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1521:
                if (resultCode == Activity.RESULT_OK) {
                    favoriteplaceArray.clear();
                    Favorite_PlaceController favorite_placeController = new Favorite_PlaceController(getActivity());
                    List<Favorite_Place> fplist = favorite_placeController.obtenerFavorite_Place();
                    for (int i = 0; i<fplist.size();i++){
                        String placeid2 = fplist.get(i).getPlaceFavoriteId();
                        String placename2 = fplist.get(i).getName();

                        favoriteplaceArray.add(new AdaptadorLugarFavorito(placeid2, placename2));
                    }

                    favoriteplaceAdapter.notifyDataSetChanged();

                }
                break;

        }
    }




    public class FavoritePlaceCustomAdapter extends ArrayAdapter<AdaptadorLugarFavorito> {
        Context context;
        int layoutResourceId;
        ArrayList<AdaptadorLugarFavorito> data = new ArrayList<AdaptadorLugarFavorito>();

        public FavoritePlaceCustomAdapter(Context context, int layoutResourceId, ArrayList<AdaptadorLugarFavorito> data) {
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
                holder.txtIdPlace = (TextView) row.findViewById(R.id.txtIdPlace);
                holder.btnOptions = (ImageButton) row.findViewById(R.id.btnOptions);
                row.setTag(holder);
                final UserHolder finalHolder = holder;
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), Agregar_Lugar_Favorito.class);
                        String placeId = finalHolder.txtIdPlace.getText().toString();
                        intent.putExtra("placeId", placeId);
                        intent.putExtra("Status", "detalle");
                        startActivity(intent);
                    }
                });

            } else {
                holder = (UserHolder) row.getTag();
            }

            AdaptadorLugarFavorito favoritePlace = data.get(position);

            holder.textName.setText(favoritePlace.getFavorite_Place());
            holder.txtIdPlace.setText(favoritePlace.getId_Favorite_Place());

            final UserHolder finalHolder1 = holder;
            holder.btnOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final CharSequence[] options = {"Eliminar", "Cancelar"};
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle("Elige una opcion");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int seleccion) {
                             if (options[seleccion] == "Eliminar") {

                                String placeid = finalHolder1.txtIdPlace.getText().toString();
                                Preferencias preferencias = new Preferencias(getActivity().getApplicationContext());
                                String clienteid = preferencias.getClient_Id();

                                Servicio servicio = new Servicio();
                                servicio.deleteFavoritePlace(clienteid, placeid);

                                Favorite_PlaceController favorite_placeController = new Favorite_PlaceController(getActivity().getApplicationContext());
                                Favorite_Place favorite_place;
                                favorite_place = favorite_placeController.obtenerFavorite_PlacePorPlaceId(placeid);
                                favorite_placeController.eliminarFavorite_Place(favorite_place);
                                favoriteplaceArray.remove(position);
                                favoriteplaceAdapter.notifyDataSetChanged();
                                if (favoriteplaceArray.size() == 0) {
                                    txt_no_data_detected.setVisibility(View.VISIBLE);
                                    favoriteplaceList.setVisibility(View.GONE);
                                } else {
                                    txt_no_data_detected.setVisibility(View.GONE);
                                    favoriteplaceList.setVisibility(View.VISIBLE);
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
            TextView txtIdPlace;
            ImageButton btnOptions;
        }
    }
}