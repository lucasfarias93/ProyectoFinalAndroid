package com.example.lfarias.actasdigitales.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lfarias.actasdigitales.R;

import butterknife.Bind;

public class LandingPageActivity extends AppCompatActivity {

    @Bind(R.id.ir_a_solicitud_acta)
    Button mSolicitudActa;
    @Bind(R.id.ver_historial_solicitudes) Button mHistorial;
    @Bind(R.id.reportar_error)
    Button mReportarError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        mSolicitudActa = (Button) findViewById(R.id.ir_a_solicitud_acta);
        mSolicitudActa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LandingPageActivity.this, RequestActActivity.class);
                startActivity(i);
            }
        });
    }
}
