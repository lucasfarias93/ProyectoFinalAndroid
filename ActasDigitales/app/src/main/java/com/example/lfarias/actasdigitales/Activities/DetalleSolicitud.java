package com.example.lfarias.actasdigitales.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.lfarias.actasdigitales.Cache.CacheService;
import com.example.lfarias.actasdigitales.Entities.SolicitudActa;
import com.example.lfarias.actasdigitales.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetalleSolicitud extends AppCompatActivity {

    @Bind(R.id.numero)TextView mNumero;
    @Bind(R.id.nombre)TextView mNombre;
    @Bind(R.id.estado)TextView mEstado;
    @Bind(R.id.cupon_pago)TextView mCupon;
    @Bind(R.id.tipoLibro)TextView mTipoLibro;
    @Bind(R.id.fecha)TextView mFecha;
    @Bind(R.id.parentesco)TextView mParentesco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_solicitud);
        ButterKnife.bind(this);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        /*final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);*/

        Intent i = getIntent();
        mNumero.setText(getResources().getString(R.string.numero_soli) + " " + i.getStringExtra("userNroSoli"));
        mNombre.setText(getResources().getString(R.string.nombre) + " " + i.getStringExtra("userName"));
        mEstado.setText(getResources().getString(R.string.estado) + " " + i.getStringExtra("userEstado"));
        mCupon.setText(getResources().getString(R.string.cupon) + " " + i.getStringExtra("userCuponPago"));
        mTipoLibro.setText(getResources().getString(R.string.tipo_libro) + " " + i.getStringExtra("userTipoLibro"));
        mFecha.setText(getResources().getString(R.string.fecha) + " " + i.getStringExtra("userFecha"));
        mParentesco.setText(getResources().getString(R.string.parentesco) + " " + i.getStringExtra("userParentesco"));
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
                Intent i = new Intent(DetalleSolicitud.this, LoginActivity.class);
                startActivity(i);
                break;

            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
