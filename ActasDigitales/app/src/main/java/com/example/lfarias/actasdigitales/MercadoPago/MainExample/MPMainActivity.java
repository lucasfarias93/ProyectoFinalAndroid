package com.example.lfarias.actasdigitales.MercadoPago.MainExample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.lfarias.actasdigitales.Activities.LandingPageActivity;
import com.example.lfarias.actasdigitales.R;

/**
 * Created by lfarias on 10/3/17.
 */

public class MPMainActivity extends AppCompatActivity {

    private LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mp_activity_main);
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog()
                .build());

        layout = (LinearLayout) findViewById(R.id.volver_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MPMainActivity.this, LandingPageActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void runCheckoutExample(View view) {

        runStep(new CheckoutExampleActivity());
    }

    private void runStep(Activity activity) {

        Intent exampleIntent = new Intent(this, activity.getClass());
        startActivity(exampleIntent);
    }
}