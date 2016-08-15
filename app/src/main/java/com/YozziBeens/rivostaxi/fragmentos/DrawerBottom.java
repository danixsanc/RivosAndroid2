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
import com.YozziBeens.rivostaxi.R;

public class DrawerBottom extends Fragment {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View mContainer;
    public boolean menuOpen;
    private Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    public DrawerBottom() {
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
        View view  = inflater.inflate(R.layout.drawer_bottom, container, false);

        return view;
    }


}
