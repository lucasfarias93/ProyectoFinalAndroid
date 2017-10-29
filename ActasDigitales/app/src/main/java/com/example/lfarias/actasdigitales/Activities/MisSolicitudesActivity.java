package com.example.lfarias.actasdigitales.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.lfarias.actasdigitales.AsyncTask.CancelRequestAsynctask;
import com.example.lfarias.actasdigitales.AsyncTask.MisSolicitudesAsynctask;
import com.example.lfarias.actasdigitales.Cache.CacheService;
import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Entities.SolicitudActa;
import com.example.lfarias.actasdigitales.Helpers.Utils;
import com.example.lfarias.actasdigitales.MercadoPago.MainExample.MPMainActivity;
import com.example.lfarias.actasdigitales.R;
import com.example.lfarias.actasdigitales.Services.ServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MisSolicitudesActivity extends AppCompatActivity implements MisSolicitudesAsynctask.Callback, CancelRequestAsynctask.Callback {

    ListView view;
    ListAdapter adapter;
    List<Map<String, String>> mapStringListItem;
    List<SolicitudActa> actas_1;
    TextView textView;
    EditText mFilter;
    LinearLayout listItemLayout;

    private static final String TEXT1 = "text1";
    private static final String TEXT2 = "text2";
    private static final String TEXT3 = "text3";
    private String payImage, deleteImage;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_solicitudes);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Mis solicitudes</font>"));
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mFilter = (EditText) findViewById(R.id.list_filter);

        textView = (TextView) findViewById(R.id.text_no_soli);
        textView.setVisibility(View.GONE);
        view = (ListView) findViewById(android.R.id.list);
        dialog = Utils.createLoadingIndicator(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        MisSolicitudesAsynctask asynctask = new MisSolicitudesAsynctask(MisSolicitudesActivity.this, MisSolicitudesActivity.this);

        ConnectionParams conectParams = new ConnectionParams();
        conectParams.setmControllerId(ServiceUtils.Controllers.CIUDADANO_CONTROLLER + "/" + ServiceUtils.Controllers.LISTADO_PATH);
        conectParams.setmActionId(ServiceUtils.Actions.LISTADO_SOLICITUDES);
        conectParams.setmSearchType(ServiceUtils.SearchType.BUSCAR_LISTADO_SOLICITUDES_TYPE);
        dialog.show();
        asynctask.execute(conectParams);

        view.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (SolicitudActa acta : actas_1) {
                    TextView soliView = (TextView) view.findViewById(R.id.nombre_prop);
                    if (acta.getNombrePropietarioActa().equals(soliView.getText().toString())) {
                        Intent i = new Intent(MisSolicitudesActivity.this, DetalleSolicitud.class);
                        i.putExtra("userName", acta.getNombrePropietarioActa());
                        i.putExtra("userNroSoli", acta.getId());
                        i.putExtra("userParentesco", acta.getNombreParentesco());
                        i.putExtra("userCuponPago", acta.getCodigoDePago());
                        i.putExtra("userTipoLibro", acta.getNombrelibro());
                        i.putExtra("userFecha", acta.getFechaCambioEstado());
                        i.putExtra("userEstado", acta.getNombreEstadoSolicitud());
                        startActivity(i);
                        break;
                    }
                }
            }
        });

        mFilter.clearFocus();
        mFilter.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        mFilter.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                ((SimpleAdapter) MisSolicitudesActivity.this.adapter).getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }


    public List<Map<String, String>> getListItems(List<SolicitudActa> userList) {
        final List<Map<String, String>> listItem =
                new ArrayList<>(userList.size());

        for (final SolicitudActa solicitud : userList) {
            final Map<String, String> listItemMap = new HashMap<>();
            listItemMap.put(TEXT1, solicitud.getNombrePropietarioActa());
            listItemMap.put(TEXT2, solicitud.getNombreEstadoSolicitud());
            listItemMap.put(TEXT3, solicitud.getNombreParentesco());
            listItem.add(Collections.unmodifiableMap(listItemMap));

        }

        return Collections.unmodifiableList(listItem);
    }

    private ListAdapter createListAdapter(List<SolicitudActa> userList) {
        final String[] fromMapKey = new String[]{TEXT1, TEXT2, TEXT3};
        final int[] toLayoutId = new int[]{R.id.nombre_prop, R.id.estado_acta, R.id.parentesco};
        final List<Map<String, String>> list = getListItems(userList);
        
        return new SimpleAdapter(MisSolicitudesActivity.this, list,
                R.layout.item_list,
                fromMapKey, toLayoutId);
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
        List<SolicitudActa> actas = new ArrayList<>();
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
            adapter = createListAdapter(actas);
            view.setAdapter(adapter);
            enableExtraButtons(view);
        }
    }
    
    public void navigateToPaymentActivity(View v){
        Intent i = new Intent(MisSolicitudesActivity.this, MPMainActivity.class);
        startActivity(i);
    }
    
    public void cancelSelectedRequest(View v){
        CancelRequestAsynctask asynctask = new CancelRequestAsynctask(MisSolicitudesActivity.this, MisSolicitudesActivity.this);
        List<String> params = new ArrayList<>();

        ConnectionParams conectParams = new ConnectionParams();
        conectParams.setmControllerId(ServiceUtils.Controllers.CIUDADANO_CONTROLLER + "/" + ServiceUtils.Controllers.LISTADO_PATH);
        conectParams.setmActionId(ServiceUtils.Actions.CANCELAR_SOLICITUD);
        conectParams.setmSearchType(ServiceUtils.SearchType.CANCELAR_SOLICITUD_SEARCH_TYPE);
        conectParams.setParams(params);
        asynctask.execute(conectParams);
    }

    public void enableExtraButtons(ListView view){
        ListAdapter adapter = view.getAdapter();
        for(int i = 0; i < adapter.getCount(); i++){
            Object object = adapter.getItem(i);
            LinearLayout v = (LinearLayout) adapter.getView(i, null, view);
            RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.images_id);
            ImageView view2= (ImageView) layout.findViewById(R.id.pagar);
            ImageView view3= (ImageView) layout.findViewById(R.id.borrar);
            layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void getCancelRequestResponse(Boolean success) {

    }
}
