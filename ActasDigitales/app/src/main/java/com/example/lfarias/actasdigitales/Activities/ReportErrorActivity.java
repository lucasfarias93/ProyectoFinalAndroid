package com.example.lfarias.actasdigitales.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lfarias.actasdigitales.Helpers.Utils;
import com.example.lfarias.actasdigitales.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReportErrorActivity extends AppCompatActivity {

    @Bind(R.id.spinner_report)
    Spinner mSpinnerReport;
    @Bind(R.id.button_enviar_reporte)
    Button mButtonReport;
    @Bind(R.id.nombre_prop_acta)
    TextInputEditText mNombreProp;
    @Bind(R.id.apellido_prop_acta)
    TextInputEditText mApellidoProp;
    @Bind(R.id.año_acta) TextInputEditText mAñoActa;
    @Bind(R.id.nro_acta) TextInputEditText mNroActa;
    @Bind(R.id.nro_libro) TextInputEditText mNroLibro;

    @Bind(R.id.nombre_prop_acta_layout)
    TextInputLayout nombreLayout;
    @Bind(R.id.apellido_prop_acta_layout)
    TextInputLayout apellitoLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_error);
        ButterKnife.bind(this);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Reporte de Error</font>"));
        mActionBar.setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Seleccione tipo de Error");
        spinnerArray.add("Enlazar acta digital");
        spinnerArray.add("Rectificar datos erroneos");
        spinnerArray.add("Realizar digitalización de acta");

        if(getIntent().getStringExtra("año_acta") != null){
            mAñoActa.setText(getIntent().getStringExtra("año_acta"));
        }
        if(getIntent().getStringExtra("nro_acta") != null){
            mNroActa.setText(getIntent().getStringExtra("nro_acta"));
        }
        if(getIntent().getStringExtra("nro_libro") != null){
            mNroLibro.setText(getIntent().getStringExtra("nro_libro"));
        }
        if(getIntent().getStringExtra("nombre") != null){
            mNombreProp.setText((getIntent().getStringExtra("nombre")));
        }
        if(getIntent().getStringExtra("apellido") != null){
            mApellidoProp.setText((getIntent().getStringExtra("apellido")));
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerReport.setAdapter(spinnerArrayAdapter);
        mSpinnerReport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mButtonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpinnerReport.getSelectedItem().toString().equals("Seleccione tipo de Error")) {
                    Toast.makeText(ReportErrorActivity.this, "Debe seleccionar un tipo de error", Toast.LENGTH_SHORT).show();
                } else if (mNombreProp.getText().toString().equals("")) {
                    nombreLayout.setError("Este campo es obligatorio");
                    mNombreProp.getBackground().setColorFilter(getResources().getColor(R.color.color_error), PorterDuff.Mode.SRC_ATOP);
                    nombreLayout.setErrorTextAppearance(R.style.error_orange);
                    if (mApellidoProp.getText().toString().equals("")) {
                        apellitoLayout.setError("Este campo es obligatorio");
                        mApellidoProp.getBackground().setColorFilter(getResources().getColor(R.color.color_error), PorterDuff.Mode.SRC_ATOP);
                        apellitoLayout.setErrorTextAppearance(R.style.error_orange);
                    }
                } else if (mApellidoProp.getText().toString().equals("")) {
                    apellitoLayout.setError("Este campo es obligatorio");
                    mApellidoProp.getBackground().setColorFilter(getResources().getColor(R.color.color_error), PorterDuff.Mode.SRC_ATOP);
                    apellitoLayout.setErrorTextAppearance(R.style.error_orange);
                } else {
                    final ProgressDialog dialog = Utils.createLoadingIndicator(ReportErrorActivity.this);

                    final Handler handler = new Handler();
                    dialog.setMessage("Aguarde mientras procesamos la información...");
                    dialog.show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            submitError(dialog);
                        }
                    }, 2000);

                }
            }
        });
    }


    public void submitError(ProgressDialog dialog) {
        dialog.dismiss();
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ContextThemeWrapper ctw = new ContextThemeWrapper(ReportErrorActivity.this, R.style.AppTheme_PopupOverlay);
            builder = new AlertDialog.Builder(ctw);
        } else {
            builder = new AlertDialog.Builder(ReportErrorActivity.this);
        }
        builder.setTitle("Error Reportado")
                .setMessage("El reporte fue enviado al Archivo General del Registro Civil con éxito.\n Tenga en cuenta que su resolución puede demorar de 2-5 dias hábiles.")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent i = new Intent(ReportErrorActivity.this, LandingPageActivity.class);
                        startActivity(i);
                    }
                })
                .setIcon(R.drawable.success_1)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
