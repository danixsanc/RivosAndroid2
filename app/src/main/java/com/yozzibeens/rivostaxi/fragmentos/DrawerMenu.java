package com.YozziBeens.rivostaxi.fragmentos;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.YozziBeens.rivostaxi.actividades.Tarjetas.Nav_Tarjetas;
import com.facebook.login.LoginManager;
import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.actividades.Ayuda.Nav_Ayuda;
import com.YozziBeens.rivostaxi.actividades.Favoritos.Nav_Favoritos;
import com.YozziBeens.rivostaxi.actividades.Historial.Nav_Historial;
import com.YozziBeens.rivostaxi.actividades.Perfil.Nav_Perfil;
import com.YozziBeens.rivostaxi.actividades.Proceso.Nav_Proceso;
import com.YozziBeens.rivostaxi.app.Main;
import com.YozziBeens.rivostaxi.controlador.ClientController;
import com.YozziBeens.rivostaxi.controlador.Favorite_CabbieController;
import com.YozziBeens.rivostaxi.controlador.Favorite_PlaceController;
import com.YozziBeens.rivostaxi.controlador.HistorialController;
import com.YozziBeens.rivostaxi.modelo.Client;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.google.android.gms.maps.model.LatLng;

public class DrawerMenu extends Fragment {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View mContainer;
    public boolean menuOpen;
    private Context context;

    TextView txtCorreo;
    TextView txtNombre;
    LinearLayout CerrarSesion;
    View rootview;
    String correo;
    String nombre;
    TextView txt_solicitar,txt_proceso,txt_favoritos,txt_historial,txt_perfil,txt_ayuda,txt_CerrarSesion;

    private LatLng mOrigen;


    public void setmOrigen(LatLng mOrigen) {
        this.mOrigen = mOrigen;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //----Tipo de fuentes---------------------------------------------------------------------------------

       /* Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getActivity().getAssets(), "RobotoCondensed-Regular.ttf");
        txtNombre = (TextView) getActivity().findViewById(R.id.txtNombre);

        txtCorreo = (TextView) getActivity().findViewById(R.id.txtCorreo);
        txt_solicitar  = (TextView) getActivity().findViewById(R.id.txt_solicitar);
        txt_solicitar.setTypeface(RobotoCondensed_Regular);
        txt_proceso = (TextView) getActivity().findViewById(R.id.txt_proceso);
        txt_proceso.setTypeface(RobotoCondensed_Regular);
        txt_favoritos = (TextView) getActivity().findViewById(R.id.txt_favoritos);
        txt_favoritos.setTypeface(RobotoCondensed_Regular);
        txt_historial = (TextView) getActivity().findViewById(R.id.txt_historial);
        txt_historial.setTypeface(RobotoCondensed_Regular);
        txt_perfil = (TextView) getActivity().findViewById(R.id.txt_perfil);
        txt_perfil.setTypeface(RobotoCondensed_Regular);
        txt_ayuda = (TextView) getActivity().findViewById(R.id.txt_ayuda);
        txt_ayuda.setTypeface(RobotoCondensed_Regular);
        txt_CerrarSesion = (TextView) getActivity().findViewById(R.id.txt_CerrarSesion);
        txt_CerrarSesion.setTypeface(RobotoCondensed_Regular);*/
        //-----------------------------------------------------------------------------------------------------
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar, final ActionBar actionBar, Context prContext) {
        mContainer = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        context = prContext;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d("VIVZ", "onDrawerOpened");
                getActivity().supportInvalidateOptionsMenu();
                menuOpen = true;
            }


            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d("VIVZ", "onDrawerClosed");
                setHasOptionsMenu(true);
                getActivity().supportInvalidateOptionsMenu();
                menuOpen = false;
            }


            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                //toolbar_wizzard.setAlpha(1 - slideOffset / 2);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public void cerrarDrawer(){
        mDrawerLayout.closeDrawers();
    }

    public DrawerMenu() {
        // Required empty public constructor
    }

    public void invalidarMenu(android.view.Menu menu, int idMenu){
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mContainer);
        menu.findItem(idMenu).setVisible(!drawerOpen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.drawer_menu, container, false);


        final Preferencias preferencias = new Preferencias(getActivity().getApplicationContext());

        boolean check = preferencias.getSesion();
        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getActivity().getAssets(), "RobotoCondensed-Regular.ttf");
        if (!check)
        {
            String Client_Id = preferencias.getClient_Id();
            ClientController clientController = new ClientController(getActivity().getApplicationContext());
            Client client;
            client = clientController.obtenerClientPorClientId(Client_Id);
            correo = client.getEmail();
            nombre = client.getName();
            txtCorreo = (TextView) view.findViewById(R.id.txtCorreo);

            txtNombre = (TextView) view.findViewById(R.id.txtNombre);
            txtNombre.setTypeface(RobotoCondensed_Regular);
            txtCorreo.setText(correo);
            txtCorreo.setTypeface(RobotoCondensed_Regular);
            txtNombre.setText(nombre);

        }


        CerrarSesion = (LinearLayout) view.findViewById(R.id.CerrarSesion);
        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder dialog1 = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                dialog1.setMessage("¿Cerrar Sesion?");
                dialog1.setCancelable(false);
                dialog1.setPositiveButton("Si", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ClientController clientController = new ClientController(getActivity().getApplicationContext());
                        clientController.eliminarTodo();
                        Intent intent = new Intent(getActivity(), Main.class);
                        startActivity(intent);

                        HistorialController historialController = new HistorialController(getActivity().getApplicationContext());
                        Favorite_CabbieController favorite_cabbieController = new Favorite_CabbieController(getActivity().getApplicationContext());
                        Favorite_PlaceController favorite_placeController = new Favorite_PlaceController(getActivity().getApplicationContext());

                        historialController.eliminarTodo();
                        favorite_cabbieController.eliminarTodo();
                        favorite_placeController.eliminarTodo();
                        LoginManager.getInstance().logOut();
                        preferencias.setSesion(true);
                        preferencias.setClient_Id(null);
                        getActivity().finish();
                    }
                });
                dialog1.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog1.show();




            }
        });


        LinearLayout solicitar;
        solicitar = (LinearLayout) view.findViewById(R.id.nav_solicitar);
        solicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
            }
        });

        LinearLayout proceso;
        proceso = (LinearLayout) view.findViewById(R.id.nav_onProcess);
        proceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Nav_Proceso.class));
            }
        });

        LinearLayout tarjetas;
        tarjetas = (LinearLayout) view.findViewById(R.id.nav_tarjetas);
        tarjetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Nav_Tarjetas.class));
            }
        });

        /*LinearLayout reservaciones;
        reservaciones = (LinearLayout) view.findViewById(R.id.nav_reservaciones);
        reservaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Nav_Reservaciones.class));
            }
        });*/

        LinearLayout favoritos;
        favoritos = (LinearLayout) view.findViewById(R.id.nav_favoritos);
        favoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Nav_Favoritos.class));
            }
        });

        LinearLayout perfil;
        perfil = (LinearLayout) view.findViewById(R.id.nav_perfil);
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Nav_Perfil.class));
            }
        });

        LinearLayout historial;
        historial = (LinearLayout) view.findViewById(R.id.nav_historial);
        historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Nav_Historial.class));
            }
        });

        LinearLayout ayuda;
        ayuda = (LinearLayout) view.findViewById(R.id.nav_ayuda);
        ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Nav_Ayuda.class));
            }
        });









        return view;
    }


}
