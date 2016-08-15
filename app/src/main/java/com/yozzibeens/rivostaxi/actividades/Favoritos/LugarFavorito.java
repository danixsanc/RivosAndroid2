package com.YozziBeens.rivostaxi.actividades.Favoritos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.adaptadores.AdaptadorLugarFavorito;
import com.YozziBeens.rivostaxi.controlador.Favorite_PlaceController;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.modelo.Favorite_Place;
import com.YozziBeens.rivostaxi.respuesta.ResultadoEliminarLugarFavorito;
import com.YozziBeens.rivostaxi.respuesta.ResultadoEliminarTaxistaFavorito;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudEliminarLugarFavorito;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.YozziBeens.rivostaxi.utilerias.Servicio;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
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

    private Gson gson;
    private ProgressDialog progressdialog;
    private ResultadoEliminarLugarFavorito resultadoEliminarLugarFavorito;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.favorite_place, container, false);

        this.gson = new Gson();



        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getActivity().getAssets(), "RobotoCondensed-Regular.ttf");

        txt_no_data_detected = (TextView) rootview.findViewById(R.id.txt_no_data_detected);
        txt_no_data_detected.setTypeface(RobotoCondensed_Regular);


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
                Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getActivity().getAssets(), "RobotoCondensed-Regular.ttf");
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new UserHolder();
                holder.textName = (TextView) row.findViewById(R.id.textView1);
                holder.textName.setTypeface(RobotoCondensed_Regular);
                holder.txtIdPlace = (TextView) row.findViewById(R.id.txtIdPlace);
                holder.txtIdPlace.setTypeface(RobotoCondensed_Regular);
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

                                 SolicitudEliminarLugarFavorito oData = new SolicitudEliminarLugarFavorito();
                                 oData.setClient_Id(clienteid);
                                 oData.setPlace_Id(placeid);
                                 DeleteLugarFavoritoWebService(gson.toJson(oData));

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


    private void DeleteLugarFavoritoWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(getActivity(), WebService.DeleteFavoritePlaceWebService, rawJson);
        servicioAsyncService.setOnCompleteListener(new AsyncTaskListener() {
            @Override
            public void onTaskStart() {
                progressdialog = new ProgressDialog(getActivity());
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
                        resultadoEliminarLugarFavorito = gson.fromJson(result.get("Resultado").toString(), ResultadoEliminarLugarFavorito.class);
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
                String messageError = resultadoEliminarLugarFavorito.getMessage();
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
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
