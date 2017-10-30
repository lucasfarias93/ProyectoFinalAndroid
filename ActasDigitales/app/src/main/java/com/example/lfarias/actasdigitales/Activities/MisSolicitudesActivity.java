package com.example.lfarias.actasdigitales.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lfarias.actasdigitales.AsyncTask.CancelRequestAsynctask;
import com.example.lfarias.actasdigitales.AsyncTask.MisSolicitudesAsynctask;
import com.example.lfarias.actasdigitales.Cache.CacheService;
import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Entities.SolicitudActa;
import com.example.lfarias.actasdigitales.Helpers.CustomAdapter;
import com.example.lfarias.actasdigitales.Helpers.Utils;
import com.example.lfarias.actasdigitales.R;
import com.example.lfarias.actasdigitales.Services.ServiceUtils;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MisSolicitudesActivity extends AppCompatActivity implements MisSolicitudesAsynctask.Callback, CancelRequestAsynctask.Callback, SearchView.OnQueryTextListener {

    ListView view;
    CustomAdapter adapter;
    List<Map<String, String>> mapStringListItem;
    List<SolicitudActa> actas_1;
    TextView textView;
    SearchView mFilter;
    LinearLayout listItemLayout;

    private static final String TEXT1 = "text1";
    private static final String TEXT2 = "text2";
    private static final String TEXT3 = "text3";
    private String payImage, deleteImage;
    ProgressDialog dialog;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_solicitudes);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Mis solicitudes</font>"));
        mActionBar.setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        mFilter = (SearchView) findViewById(R.id.list_filter);

        textView = (TextView) findViewById(R.id.text_no_soli);
        textView.setVisibility(View.GONE);
        view = (ListView) findViewById(android.R.id.list);
        view.setTextFilterEnabled(true);
        dialog = Utils.createLoadingIndicator(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        MisSolicitudesAsynctask asynctask = new MisSolicitudesAsynctask(MisSolicitudesActivity.this, MisSolicitudesActivity.this);

        ConnectionParams conectParams = new ConnectionParams();
        conectParams.setmControllerId(ServiceUtils.Controllers.CIUDADANO_CONTROLLER + "/" + ServiceUtils.Controllers.LISTADO_PATH);
        conectParams.setmActionId(ServiceUtils.Actions.LISTADO_SOLICITUDES);
        conectParams.setmSearchType(ServiceUtils.SearchType.BUSCAR_LISTADO_SOLICITUDES_TYPE);
        dialog.show();
        asynctask.execute(conectParams);
        setupSearchView();
    }

    private void setupSearchView() {
        mFilter.setIconifiedByDefault(false);
        mFilter.setOnQueryTextListener(this);
        mFilter.setSubmitButtonEnabled(false);
        mFilter.setQueryHint("Filtre su búsqueda aqui...");

        mFilter.clearFocus();
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (TextUtils.isEmpty(newText)) {
            view.clearTextFilter();
        } else {
            view.setFilterText(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
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
                Intent i = new Intent(MisSolicitudesActivity.this, LoginActivity.class);
                startActivity(i);
                break;

            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void getSolicitudesList(List<JSONObject> success) {
        dialog.dismiss();
        ArrayList<SolicitudActa> actas = new ArrayList<>();
        for (JSONObject object : success) {
            SolicitudActa acta = new SolicitudActa();
            try {
                acta.setId(object.get("id").toString());
                acta.setNombrePropietarioActa(object.get("nombrepropietarioacta").toString());
                acta.setIdUsuario(object.get("idusuario").toString());
                acta.setIdImagenActa(object.get("idimagenacta").toString());
                acta.setIdCuponDePago(object.get("idcupondepago").toString());
                acta.setIdParentesco(object.get("idparentesco").toString());
                acta.setIdTipoLibro(object.get("idtipolibro").toString());
                acta.setUltimoSolicitudEstado(object.get("ultimosolicitudestado").toString());
                acta.setNombreParentesco(object.get("nombreparentesco").toString());
                acta.setCodigoDePago(object.get("codigodepago").toString());
                acta.setNombrelibro(object.get("nombrelibro").toString());
                acta.setFechaCambioEstado(object.get("fechacambioestado").toString());
                acta.setNombreEstadoSolicitud(object.get("nombreestadosolicitud").toString());
                actas.add(acta);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (actas.size() == 0) {
            textView.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);
        } else {
            actas_1 = actas;
            view.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            adapter = new CustomAdapter(MisSolicitudesActivity.this, actas, MisSolicitudesActivity.this);
            view.setAdapter(adapter);

            view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    SolicitudActa acta = adapter.getItem(position);
                    Intent intent = new Intent(MisSolicitudesActivity.this, DetalleSolicitud.class);
                    intent.putExtra("userName", acta.getNombrePropietarioActa());
                    intent.putExtra("userNroSoli", acta.getId());
                    intent.putExtra("userParentesco", acta.getNombreParentesco());
                    intent.putExtra("userCuponPago", acta.getCodigoDePago());
                    intent.putExtra("userTipoLibro", acta.getNombrelibro());
                    intent.putExtra("userFecha", acta.getFechaCambioEstado());
                    intent.putExtra("userEstado", acta.getNombreEstadoSolicitud());
                    MisSolicitudesActivity.this.startActivity(intent);
                }
            });
        }
    }

    @Override
    public void getCancelRequestResponse(Boolean success) {
        if (success) {
            dialog.dismiss();
            final AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ContextThemeWrapper ctw = new ContextThemeWrapper(MisSolicitudesActivity.this, R.style.AppTheme_PopupOverlay);
                builder = new AlertDialog.Builder(ctw);
            } else {
                builder = new AlertDialog.Builder(MisSolicitudesActivity.this);
            }
            builder.setTitle("Solicitud de acta cancelada")
                    .setMessage("La solicitud seleccionada fue cancelada satisfactoriamente.")
                    .setNegativeButton("Ir a MIS SOLICITUDES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            MisSolicitudesAsynctask asynctask = new MisSolicitudesAsynctask(MisSolicitudesActivity.this, MisSolicitudesActivity.this);
                            ProgressDialog dialog1 = Utils.createLoadingIndicator(MisSolicitudesActivity.this);
                            ConnectionParams conectParams = new ConnectionParams();
                            conectParams.setmControllerId(ServiceUtils.Controllers.CIUDADANO_CONTROLLER + "/" + ServiceUtils.Controllers.LISTADO_PATH);
                            conectParams.setmActionId(ServiceUtils.Actions.LISTADO_SOLICITUDES);
                            conectParams.setmSearchType(ServiceUtils.SearchType.BUSCAR_LISTADO_SOLICITUDES_TYPE);
                            //dialog1.show();
                            asynctask.execute(conectParams);
                        }
                    })
                    .setIcon(R.drawable.success_1)
                    .show();
        } else {
            dialog.dismiss();
            final AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ContextThemeWrapper ctw = new ContextThemeWrapper(MisSolicitudesActivity.this, R.style.AppTheme_PopupOverlay);
                builder = new AlertDialog.Builder(ctw);
            } else {
                builder = new AlertDialog.Builder(MisSolicitudesActivity.this);
            }
            builder.setTitle("Error al cancelar la solicitud")
                    .setMessage("No se pudo cancelar la solicitud seleccionada")
                    .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(R.drawable.error_1)
                    .show();
        }
        dialog.dismiss();
    }
}
