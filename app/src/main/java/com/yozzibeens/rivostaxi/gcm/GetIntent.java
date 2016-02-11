package com.YozziBeens.rivostaxi.gcm;

import android.os.Bundle;

import com.YozziBeens.rivostaxi.R;


/**
 * Created by aneh on 8/16/2014.
 */
public class GetIntent extends com.YozziBeens.rivostaxi.app.Main {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        /*String get = getIntent().getStringExtra("Notif");

        Log.e("Msg", "---------------------------"+get);

        TextView txt = (TextView)findViewById(R.id.get);
        txt.setText(get);*/

    }
}
