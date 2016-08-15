package com.YozziBeens.rivostaxi.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.modelo.Favorite_Place;
import com.YozziBeens.rivostaxi.modelo.Lugar;

import java.util.List;

/**
 * Created by Zair on 26/11/2015.
 */
public class LugarAdapter extends ArrayAdapter<Favorite_Place> {

    private List<Favorite_Place> items;
    private Context context;

    public LugarAdapter(Context context, int textViewResourceId, List<Favorite_Place> prItems) {
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
            view = vi.inflate(R.layout.item_lugar, parent, false);
            holder = new ViewHolder();
            holder.txtNombreLugar = (TextView) view.findViewById(R.id.txtNombreLugar);
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();
        final Favorite_Place oItem = items.get(position);
        if (oItem != null) {
            holder.txtNombreLugar.setText(oItem.getName());
        }
        return view;
    }

    static class ViewHolder {
        private TextView txtNombreLugar;
    }
}
