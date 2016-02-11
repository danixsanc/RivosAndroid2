package com.YozziBeens.rivostaxi.adaptadores;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Yozzi Been's S.A. de C.V.
 */
public class FavoritosPaginador extends Fragment {

    ViewPager pager;
    //ViewPagerAdpaterFavorite adapter;
    //SlidingTabLayout tabs;
    CharSequence Titles[]={"Taxista","Lugares"};
    int Numboftabs = 2;

    View rootview;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //rootview = inflater.inflate(R.layout.favorites_layout, container, false);





        //adapter =  new ViewPagerAdpaterFavorite(getActivity().getSupportFragmentManager(),Titles,Numboftabs);
        //pager = (ViewPager) rootview.findViewById(R.id.pager3);
        //pager.setAdapter(adapter);
        /*tabs = (SlidingTabLayout) rootview.findViewById(R.id.tabs3);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(pager);


*/


        return rootview;
    }
}