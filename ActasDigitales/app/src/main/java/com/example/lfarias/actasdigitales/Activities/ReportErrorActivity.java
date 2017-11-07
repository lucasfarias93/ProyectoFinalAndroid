package com.example.lfarias.actasdigitales.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.constraint.solver.Cache;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lfarias.actasdigitales.AsyncTask.ReportErrorAsynctask;
import com.example.lfarias.actasdigitales.AsyncTask.SearchParentBookTypeAsynctask;
import com.example.lfarias.actasdigitales.Cache.CacheService;
import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Helpers.Utils;
import com.example.lfarias.actasdigitales.R;
import com.example.lfarias.actasdigitales.Services.ServiceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReportErrorActivity extends AppCompatActivity implements ReportErrorAsynctask.Callback{

    @Bind(R.id.spinner_report)
    Spinner mSpinnerReport;
    @Bind(R.id.button_enviar_reporte)
    Button mButtonReport;
    @Bind(R.id.nombre_prop_acta)
    TextInputEditText mNombreProp;
    @Bind(R.id.apellido_prop_acta)
    TextInputEditText mApellidoProp;
    @Bind(R.id.observaciones) TextInputEditText mObservaciones;
    @Bind(R.id.año_acta) TextInputEditText mAñoActa;
    @Bind(R.id.nro_acta) TextInputEditText mNroActa;
    @Bind(R.id.nro_libro) TextInputEditText mNroLibro;

    @Bind(R.id.nombre_prop_acta_layout)
    TextInputLayout nombreLayout;
    @Bind(R.id.apellido_prop_acta_layout)
    TextInputLayout apellitoLayout;
    ProgressDialog dialog1;

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

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mAñoActa.clearFocus();
        mNroActa.clearFocus();
        mNroLibro.clearFocus();
        mNombreProp.clearFocus();
        mApellidoProp.clearFocus();

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
                    mNombreProp.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                    nombreLayout.setErrorTextAppearance(R.style.error_red);
                    if (mApellidoProp.getText().toString().equals("")) {
                        apellitoLayout.setError("Este campo es obligatorio");
                        mApellidoProp.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                        apellitoLayout.setErrorTextAppearance(R.style.error_red);
                    }
                } else if (mApellidoProp.getText().toString().equals("")) {
                    apellitoLayout.setError("Este campo es obligatorio");
                    mApellidoProp.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                    apellitoLayout.setErrorTextAppearance(R.style.error_red);
                } else {
                    dialog1 = Utils.createLoadingIndicator(ReportErrorActivity.this);

                    ReportErrorAsynctask provincesDataRetrieveAsynctask = new ReportErrorAsynctask(ReportErrorActivity.this, ReportErrorActivity.this, dialog1);
                    List<String> params = new ArrayList<>();
                    params.add(CacheService.getInstance().getApellido());
                    params.add(CacheService.getInstance().getNombre());
                    if(CacheService.getInstance().getNroActa() != null){
                        params.add(CacheService.getInstance().getNroActa());
                    } else {
                        params.add("0");
                    }
                    if(!mObservaciones.getText().toString().isEmpty()){
                        params.add(mObservaciones.getText().toString());
                    } else {
                        params.add(null);
                    }
                    if(mSpinnerReport.getSelectedItem().toString().equals("Rectificar datos erroneos")){
                        params.add("8");
                    }
                    if(mSpinnerReport.getSelectedItem().toString().equals("Enlazar acta digital")){
                        params.add("7");
                    }
                    if(mSpinnerReport.getSelectedItem().toString().equals("Realizar digitalización de acta")){
                        params.add("6");
                    }
                    params.add(String.valueOf(CacheService.getInstance().getIdUser()));
                    if(CacheService.getInstance().getNroLibro() != null){
                        params.add(CacheService.getInstance().getNroLibro());
                    } else {
                        params.add("0");
                    }

                    ConnectionParams conectParams = new ConnectionParams();
                    conectParams.setmControllerId(ServiceUtils.Controllers.CIUDADANO_CONTROLLER + "/" + ServiceUtils.Controllers.REPORT_ERROR_PATH);
                    conectParams.setmActionId(ServiceUtils.Actions.REPORT_ERROR);
                    conectParams.setmSearchType(ServiceUtils.SearchType.REPORTAR_ERROR_SEARCH_TYPE);
                    conectParams.setParams(params);
                    dialog1.show();
                    provincesDataRetrieveAsynctask.execute(conectParams);

                }
            }
        });
    }


    public void submitError() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        Snackbar snackbar = Snackbar
                .make(getWindow().getDecorView().findViewById(R.id.activity_report_error), "Reporte enviado correctamente", Snackbar.LENGTH_LONG)
                .setAction("IR AL INICIO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(ReportErrorActivity.this, LandingPageActivity.class);
                        startActivity(i);
                    }
                });
        // Changing message text color
        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));

        // Changing action button text color
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(ReportErrorActivity.this, R.color.black));
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void successReportError(Boolean success) {
        dialog1.dismiss();
        if(success){
            submitError();
        } else {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ContextThemeWrapper ctw = new ContextThemeWrapper(ReportErrorActivity.this, R.style.AppTheme_PopupOverlay);
                builder = new AlertDialog.Builder(ctw);
            } else {
                builder = new AlertDialog.Builder(ReportErrorActivity.this);
            }
            builder.setTitle("Error")
                    .setMessage("Ocurrio un error al enviar el reporte al Archivo general. Intente nuevamente más tarde.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(R.drawable.error_1)
                    .show();
        }
    }
}
