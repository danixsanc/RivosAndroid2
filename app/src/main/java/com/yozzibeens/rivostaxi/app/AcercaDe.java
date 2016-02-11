package com.YozziBeens.rivostaxi.app;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.YozziBeens.rivostaxi.R;


public class AcercaDe extends AppCompatActivity {

    ImageButton back_button;
    TextView textView3,textView4,textView5,textView7,textView8,textView10;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acerca_de);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");

        textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setTypeface(RobotoCondensed_Regular);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView4.setTypeface(RobotoCondensed_Regular);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView5.setTypeface(RobotoCondensed_Regular);
        textView7 = (TextView) findViewById(R.id.textView7);
        textView7.setTypeface(RobotoCondensed_Regular);
        textView8 = (TextView) findViewById(R.id.textView8);
        textView8.setTypeface(RobotoCondensed_Regular);
        textView10 = (TextView) findViewById(R.id.textView10);
        textView10.setTypeface(RobotoCondensed_Regular);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}









