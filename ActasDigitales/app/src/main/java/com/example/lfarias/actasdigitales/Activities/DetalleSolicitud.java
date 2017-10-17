package com.example.lfarias.actasdigitales.Activities;

import android.content.Intent;
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

        Intent i = getIntent();
        mNumero.setText(" o) Numero de solicitud: " + i.getStringExtra("userNroSoli"));
        mNombre.setText(" o) Nombre del Propietario: " + i.getStringExtra("userName"));
        mEstado.setText(" o) Estado de la solicitud: " + i.getStringExtra("userEstado"));
        mCupon.setText(" o) Cupon de pago asociado: " + i.getStringExtra("userCuponPago"));
        mTipoLibro.setText(" o) Tipo de Libro: " + i.getStringExtra("userTipoLibro"));
        mFecha.setText(" o) Fecha de creación de solicitud: " + i.getStringExtra("userFecha"));
        mParentesco.setText(" o) Parentesco: " + i.getStringExtra("userParentesco"));
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
