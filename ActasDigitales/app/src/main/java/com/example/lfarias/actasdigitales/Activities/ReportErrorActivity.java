package com.example.lfarias.actasdigitales.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
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
    EditText mNombreProp;
    @Bind(R.id.apellido_prop_acta)
    EditText mApellidoProp;

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
                if (mSpinnerReport.getSelectedItem().toString().equals("Seleccione tipo de Error")) {
                    Toast.makeText(ReportErrorActivity.this, "Debe seleccionar un tipo de error", Toast.LENGTH_SHORT).show();
                } else if (mNombreProp.getText().toString().equals("")) {
                    mNombreProp.setError("Este campo es obligatorio");
                    if (mApellidoProp.getText().toString().equals("")) {
                        mApellidoProp.setError("Este campo es obligatorio");
                    }

                } else if (mApellidoProp.getText().toString().equals("")) {
                    mApellidoProp.setError("Este campo es obligatorio");
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
