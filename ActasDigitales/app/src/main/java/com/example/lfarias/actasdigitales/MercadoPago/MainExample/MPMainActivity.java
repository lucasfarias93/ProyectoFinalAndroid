package com.example.lfarias.actasdigitales.MercadoPago.MainExample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.lfarias.actasdigitales.Activities.LandingPageActivity;
import com.example.lfarias.actasdigitales.Activities.LoginActivity;
import com.example.lfarias.actasdigitales.Cache.CacheService;
import com.example.lfarias.actasdigitales.R;

/**
 * Created by lfarias on 10/3/17.
 */

public class MPMainActivity extends AppCompatActivity {

    private LinearLayout layout;
    private String idSoli;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mp_activity_main);
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog()
                .build());

        /*ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("Pagar con MercadoPago");
        mActionBar.setDisplayHomeAsUpEnabled(true);*/

        layout = (LinearLayout) findViewById(R.id.volver_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MPMainActivity.this, LandingPageActivity.class);
                startActivity(i);
                finish();
            }
        });

        idSoli = getIntent().getStringExtra("idSolicitud").toString();
    }

    public void runCheckoutExample(View view) {

        runStep(new CheckoutExampleActivity());
    }

    private void runStep(Activity activity) {

        Intent exampleIntent = new Intent(this, activity.getClass());
        exampleIntent.putExtra("idSolicitud", idSoli);
        startActivity(exampleIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_request_act, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                finish();
                CacheService.getInstance().clearUser1MockData();
                Intent i = new Intent(MPMainActivity.this, LoginActivity.class);
                startActivity(i);
                break;

            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}