package com.YozziBeens.rivostaxi.app;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.YozziBeens.rivostaxi.R;

public class TerminosCondiciones extends AppCompatActivity
{
    TextView textView3,textView4,textView2;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_terminos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");

        textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setTypeface(RobotoCondensed_Regular);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setTypeface(RobotoCondensed_Regular);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView4.setTypeface(RobotoCondensed_Regular);
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
