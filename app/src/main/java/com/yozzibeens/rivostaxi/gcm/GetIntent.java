package com.yozzibeens.rivostaxi.gcm;

import android.os.Bundle;

import com.yozzibeens.rivostaxi.R;


/**
 * Created by aneh on 8/16/2014.
 */
public class GetIntent extends com.yozzibeens.rivostaxi.app.Main {
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
