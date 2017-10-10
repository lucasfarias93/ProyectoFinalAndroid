package com.example.lfarias.actasdigitales.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lfarias.actasdigitales.AsyncTask.UserIdAsynctask;
import com.example.lfarias.actasdigitales.AsyncTask.VerificarValidezAsynctask;
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

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validar_acta);
        ButterKnife.bind(this);

        dialog = Utils.createLoadingIndicator(this);

        mVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserIdAsynctask asynctask = new UserIdAsynctask(ValidarActaActivity.this, ValidarActaActivity.this, dialog);

                ConnectionParams conectParams = new ConnectionParams();
                conectParams.setmControllerId(ServiceUtils.Controllers.CIUDADANO_CONTROLLER + "/" + ServiceUtils.Controllers.COMMON_INDEX_METHOD);
                conectParams.setmActionId(ServiceUtils.Actions.CIUDADANO_ID);
                conectParams.setmSearchType(ServiceUtils.SearchType.USER_ID_SEARCH_TYPE);
                dialog.show();
                asynctask.execute(conectParams);
            }
        });

        mIrInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ValidarActaActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void getUserId(Object success) {
        VerificarValidezAsynctask asynctask = new VerificarValidezAsynctask(ValidarActaActivity.this, ValidarActaActivity.this, dialog);
        List<String> params = new ArrayList<>();
        params.add("5");
        params.add("1x32");

        ConnectionParams conectParams = new ConnectionParams();
        conectParams.setmControllerId(ServiceUtils.Controllers.CIUDADANO_CONTROLLER + "/" + ServiceUtils.Controllers.VERIFICAR_CONTROLLER);
        conectParams.setmActionId(ServiceUtils.Actions.VALIDEZ_ACTA);
        conectParams.setmSearchType(ServiceUtils.SearchType.VALIDEZ_ACTA_SEARCH_TYPE);
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
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Vigencia de Acta")
                    .setMessage(success.toString())
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(R.drawable.alerts)
                    .show();
        }
    }
}
