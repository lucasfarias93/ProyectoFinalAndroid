package com.example.lfarias.actasdigitales.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lfarias.actasdigitales.Cache.CacheService;
import com.example.lfarias.actasdigitales.R;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LandingPageActivity extends AppCompatActivity {

    TextView mSolicitarActa;
    TextView mMisSolicitudes;
    TextView mVerificarVigencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        mSolicitarActa      = (TextView) findViewById(R.id.solicitar);
        mMisSolicitudes     = (TextView) findViewById(R.id.mis_solicitudes);
        mVerificarVigencia  = (TextView) findViewById(R.id.vigencia_acta);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Panel principal</font>"));

        mSolicitarActa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LandingPageActivity.this, RequestActActivity.class);
                startActivity(i);
            }
        });

        mVerificarVigencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LandingPageActivity.this, ValidarActaActivity.class);
                startActivity(i);
            }
        });

        mMisSolicitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LandingPageActivity.this, MisSolicitudesActivity.class);
                startActivity(i);
            }
        });
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
                CacheService.getInstance().clear();
                Intent i = new Intent(LandingPageActivity.this, LoginActivity.class);
                startActivity(i);
                break;

            case android.R.id.home:
                finish();
                break;

        }
        return true;
    }

    @Override
    public void onBackPressed() {
    }
}
