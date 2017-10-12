package com.example.lfarias.actasdigitales.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_error);
        ButterKnife.bind(this);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("Reporte de Error");
        mActionBar.setDisplayHomeAsUpEnabled(true);

        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Seleccione tipo de Error");
        spinnerArray.add("Enlazar acta digital");
        spinnerArray.add("Rectificar datos erroneos");
        spinnerArray.add("Realizar digitalización de acta");

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
                final AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ContextThemeWrapper ctw = new ContextThemeWrapper(ReportErrorActivity.this, R.style.AppTheme_PopupOverlay);
                    builder = new AlertDialog.Builder(ctw);
                } else {
                    builder = new AlertDialog.Builder(ReportErrorActivity.this);
                }
                builder.setTitle("Error Reportado")
                        .setMessage("El reporte fue enviado al Archivo General del Registro Civil con éxito. Tenga en cuenta que su resolución puede demorar de 2-5 dias hábiles.")
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(ReportErrorActivity.this, RequestActActivity.class);
                                startActivity(i);
                            }
                        })
                        .setIcon(R.drawable.information)
                        .show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
