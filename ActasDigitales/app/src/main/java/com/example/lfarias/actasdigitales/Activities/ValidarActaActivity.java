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

public class ValidarActaActivity extends AppCompatActivity implements UserIdAsynctask.Callback, VerificarValidezAsynctask.Callback{

    @Bind(R.id.verificar)Button mVerificar;
    @Bind(R.id.ir_inicio)Button mIrInicio;
    @Bind(R.id.code)EditText mCodeActa;
    @Bind(R.id.nro_doc)EditText mDni;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validar_acta);
        ButterKnife.bind(this);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("Verificar Acta");
        mActionBar.setDisplayHomeAsUpEnabled(true);

        dialog = Utils.createLoadingIndicator(this);

        mVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* UserIdAsynctask asynctask = new UserIdAsynctask(ValidarActaActivity.this, ValidarActaActivity.this, dialog);

                ConnectionParams conectParams = new ConnectionParams();
                conectParams.setmControllerId(ServiceUtils.Controllers.CIUDADANO_CONTROLLER + "/" + ServiceUtils.Controllers.COMMON_INDEX_METHOD);
                conectParams.setmActionId(ServiceUtils.Actions.CIUDADANO_ID);
                conectParams.setmSearchType(ServiceUtils.SearchType.USER_ID_SEARCH_TYPE);
                dialog.show();
                asynctask.execute(conectParams);*/
                if (mDni.getText().toString().isEmpty()) {
                    mDni.setError("Este campo es obligatorio");
                    if (mCodeActa.getText().toString().isEmpty()) {
                        mCodeActa.setError("Este campo es obligatorio");
                    }
                } else {
                    final ProgressDialog dialog = Utils.createLoadingIndicator(ValidarActaActivity.this);

                    final Handler handler = new Handler();
                    dialog.setMessage("Aguarde mientras procesamos la información...");
                    dialog.show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            checkActExpire(mCodeActa.getText().toString(), dialog);
                        }
                    }, 4000);
                }

                mIrInicio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(ValidarActaActivity.this, LandingPageActivity.class);
                        startActivity(i);
                        finish();
                    }
                });

            }
        });
    }

    @Override
    public void getUserId(Object success) {
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

    @Override
    public void verificarValidez(Object success) {
        if(success.toString().contains("No existen actas")){
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
            builder.setTitle("Vigencia de Acta")
                    .setMessage(success.toString())
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(R.drawable.information)
                    .show();
        }
    }

    public void checkActExpire(String code, ProgressDialog dialog) {
        AlertDialog.Builder builder;
        switch (code) {
            case "12345":
                dialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ContextThemeWrapper ctw = new ContextThemeWrapper(ValidarActaActivity.this, R.style.AppTheme_PopupOverlay);
                    builder = new AlertDialog.Builder(ctw);
                } else {
                    builder = new AlertDialog.Builder(ValidarActaActivity.this);
                }
                builder.setTitle("Acta Vigente")
                        .setMessage("- Su acta se encuentra en estado vigente.\n" +
                                " - Tiempo restante de vigencia: 256 días")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mDni.setText("");
                                mCodeActa.setText("");
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.drawable.information)
                        .show();
                break;

            case "00000":
                dialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ContextThemeWrapper ctw = new ContextThemeWrapper(ValidarActaActivity.this, R.style.AppTheme_PopupOverlay);
                    builder = new AlertDialog.Builder(ctw);
                } else {
                    builder = new AlertDialog.Builder(ValidarActaActivity.this);
                }
                builder.setTitle("Acta Vigente")
                        .setMessage("\" - Su acta se encuentra en estado vigente.\n" +
                                "- Tiempo restante de vigencia: 15 días")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mDni.setText("");
                                mCodeActa.setText("");
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.drawable.information)
                        .show();
                break;

            case "15963":
                dialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ContextThemeWrapper ctw = new ContextThemeWrapper(ValidarActaActivity.this, R.style.AppTheme_PopupOverlay);
                    builder = new AlertDialog.Builder(ctw);
                } else {
                    builder = new AlertDialog.Builder(ValidarActaActivity.this);
                }
                builder.setTitle("Acta caducada")
                        .setMessage("Su acta ha caducado. Si neccesita hacer uso de un acta digital, por favor inicie el proceso de solicitud nuevamente.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mDni.setText("");
                                mCodeActa.setText("");
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.drawable.information)
                        .show();
                break;

            default:
                dialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ContextThemeWrapper ctw = new ContextThemeWrapper(ValidarActaActivity.this, R.style.AppTheme_PopupOverlay);
                    builder = new AlertDialog.Builder(ctw);
                } else {
                    builder = new AlertDialog.Builder(ValidarActaActivity.this);
                }
                builder.setTitle("Error")
                        .setMessage("No se ha encontrado un acta con el numero ingresado. Revise el numero que figura en el acta digital e intente nuevamente")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mDni.setText("");
                                mCodeActa.setText("");
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.drawable.information)
                        .show();
                break;
        }
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
