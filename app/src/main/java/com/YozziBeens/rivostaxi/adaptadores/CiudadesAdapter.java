package com.YozziBeens.rivostaxi.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;


import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.modelo.Ciudad;

import java.util.List;

/**
 * Created by Zair on 26/11/2015.
 */
public class CiudadesAdapter extends ArrayAdapter<Ciudad> {

    private List<Ciudad> items;
    private Context context;

    public CiudadesAdapter(Context context, int textViewResourceId, List<Ciudad> prItems) {
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
            view = vi.inflate(R.layout.item_ciudad, parent, false);
            holder = new ViewHolder();
            holder.txtSettings = (CheckedTextView) view.findViewById(R.id.txtSettings);
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();
        final Ciudad oItem = items.get(position);
        if (oItem != null) {
            holder.txtSettings.setText(oItem.getNombreCiudad());
        }
        return view;
    }

    static class ViewHolder {
        private CheckedTextView txtSettings;
    }
}
