package com.YozziBeens.rivostaxi.actividades.Favoritos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.adaptadores.AdaptadorTaxistaFavorito;
import com.YozziBeens.rivostaxi.controlador.Favorite_CabbieController;
import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.listener.ServicioAsyncService;
import com.YozziBeens.rivostaxi.modelo.Favorite_Cabbie;
import com.YozziBeens.rivostaxi.respuesta.ResultadoEliminarHistorial;
import com.YozziBeens.rivostaxi.respuesta.ResultadoEliminarTaxistaFavorito;
import com.YozziBeens.rivostaxi.servicios.WebService;
import com.YozziBeens.rivostaxi.solicitud.SolicitudEliminarHistorial;
import com.YozziBeens.rivostaxi.solicitud.SolicitudEliminarTaxistaFavorito;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.YozziBeens.rivostaxi.utilerias.Servicio;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
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

    private Gson gson;
    private ProgressDialog progressdialog;
    private ResultadoEliminarTaxistaFavorito resultadoEliminarTaxistaFavorito;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.favorite_cabbie, container, false);

        this.gson = new Gson();


        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getActivity().getAssets(), "RobotoCondensed-Regular.ttf");

        txt_no_data_detected = (TextView) rootview.findViewById(R.id.txt_no_data_detected);
        txt_no_data_detected.setTypeface(RobotoCondensed_Regular);


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
                Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getActivity().getAssets(), "RobotoCondensed-Regular.ttf");
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new UserHolder();
                holder.textName = (TextView) row.findViewById(R.id.textView1);
                holder.textName.setTypeface(RobotoCondensed_Regular);
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

                                SolicitudEliminarTaxistaFavorito oData = new SolicitudEliminarTaxistaFavorito();
                                oData.setClient_Id(clienteid);
                                oData.setCabbie_Id(cabbieid);
                                DeleteTaxistaFavoritoWebService(gson.toJson(oData));

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


    private void DeleteTaxistaFavoritoWebService(String rawJson) {
        ServicioAsyncService servicioAsyncService = new ServicioAsyncService(getActivity(), WebService.DeleteFavoriteCabbieWebService, rawJson);
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
                        resultadoEliminarTaxistaFavorito = gson.fromJson(result.get("Resultado").toString(), ResultadoEliminarTaxistaFavorito.class);
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
                String messageError = resultadoEliminarTaxistaFavorito.getMessage();
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
