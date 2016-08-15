package com.YozziBeens.rivostaxi.adaptadores;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.YozziBeens.rivostaxi.R;

import java.util.List;

/**
 * Created by Zair on 26/11/2015.
 */
public class DireccionesAdapter extends ArrayAdapter<Address> {

    private List<Address> items;
    private Context context;

    public DireccionesAdapter(Context context, int textViewResourceId, List<Address> prItems) {
        super(context, textViewResourceId, prItems);
        this.items = prItems;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.item_direcciones, parent, false);
            holder = new ViewHolder();
            holder.txtNombreLugar = (TextView) view.findViewById(R.id.txtNombreLugar);
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();
        final Address oItem = items.get(position);
        if (oItem != null) {
            holder.txtNombreLugar.setText(obtenerDireccionLimpia(oItem));
        }
        return view;
    }

    private String obtenerDireccionLimpia(Address address)
    {
        String Thoroughfare = address.getThoroughfare() == null ? "" : address.getThoroughfare();
        String FeatureName = address.getFeatureName() == null ? "" : address.getFeatureName();
        String Locality = address.getLocality() == null ? "" : address.getLocality();
        String AdminArea = address.getAdminArea() == null ? "" : address.getAdminArea();
        return Thoroughfare + " " + FeatureName  + " " + Locality  + " " + AdminArea;
    }

    static class ViewHolder {
        private TextView txtNombreLugar;
    }
}
