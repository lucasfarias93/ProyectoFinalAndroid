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
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lfarias.actasdigitales.AsyncTask.UserIdAsynctask;
import com.example.lfarias.actasdigitales.AsyncTask.VerificarValidezAsynctask;
import com.example.lfarias.actasdigitales.Cache.CacheService;
import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Helpers.Utils;
import com.example.lfarias.actasdigitales.R;
import com.example.lfarias.actasdigitales.Services.ServiceUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ValidarActaActivity extends AppCompatActivity implements VerificarValidezAsynctask.Callback {

    @Bind(R.id.verificar)
    Button mVerificar;
    @Bind(R.id.code)
    EditText mCodeActa;
    @Bind(R.id.nro_doc)
    EditText mDni;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validar_acta);
        ButterKnife.bind(this);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Verificar Acta</font>"));
        mActionBar.setDisplayHomeAsUpEnabled(true);

        dialog = Utils.createLoadingIndicator(this);

        mVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificarValidezAsynctask asynctask = new VerificarValidezAsynctask(ValidarActaActivity.this, ValidarActaActivity.this, dialog);
                List<String> params = new ArrayList<>();
                params.add("5");
                params.add("1xd32");

                ConnectionParams conectParams = new ConnectionParams();
                conectParams.setmControllerId(ServiceUtils.Controllers.CIUDADANO_CONTROLLER + "/" + ServiceUtils.Controllers.VERIFICAR_CONTROLLER);
                conectParams.setmActionId(ServiceUtils.Actions.VALIDEZ_ACTA);
                conectParams.setmSearchType(ServiceUtils.SearchType.VALIDEZ_ACTA_SEARCH_TYPE);
                conectParams.setParams(params);
                dialog.show();
                asynctask.execute(conectParams);
            }
        });
    }

    @Override
    public void verificarValidez(Object success) {
        if (success.toString().contains("No existen actas")) {
            dialog.dismiss();
            Utils.createGlobalDialog(this, "Error al validar acta", "No se encontraron actas con los datos ingresados").show();
        } else {
            dialog.dismiss();
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ContextThemeWrapper ctw = new ContextThemeWrapper(ValidarActaActivity.this, R.style.AppTheme_PopupOverlay);
                builder = new AlertDialog.Builder(ctw);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            String validez = success.toString().substring(1, success.toString().length() - 1);
            builder.setTitle("Vigencia de Acta")
                    .setMessage(validez)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(R.drawable.success_1)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_request_act, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                finish();
                CacheService.getInstance().clear();
                Intent i = new Intent(ValidarActaActivity.this, LoginActivity.class);
                startActivity(i);
                break;

            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
