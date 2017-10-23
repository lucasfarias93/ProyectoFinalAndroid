package com.example.lfarias.actasdigitales.Activities;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.lfarias.actasdigitales.Cache.CacheService;
import com.example.lfarias.actasdigitales.Entities.SolicitudActa;
import com.example.lfarias.actasdigitales.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MisSolicitudesActivity extends AppCompatActivity {

    ListView view;
    ListAdapter adapter;
    List<Map<String, String>> mapStringListItem;
    List<SolicitudActa> actas;
    TextView textView;

    private static final String TEXT1 = "text1";
    private static final String TEXT2 = "text2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_solicitudes);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("Mis solicitudes");
        mActionBar.setDisplayHomeAsUpEnabled(true);

        textView = (TextView)findViewById(R.id.text_no_soli);
        textView.setVisibility(View.GONE);
        view = (ListView) findViewById(android.R.id.list);
        actas = CacheService.getInstance().getActaUser1();
        if(actas.size() == 0){
            textView.setVisibility(View.VISIBLE);
        }
        adapter = createListAdapter(actas);
        view.setAdapter(adapter);

        view.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(SolicitudActa acta : actas){
                    TextView soliView = (TextView) view.findViewById(android.R.id.text1);
                    if(acta.getNombrePropietario().equals(soliView.getText().toString())){
                        Intent i = new Intent(MisSolicitudesActivity.this, DetalleSolicitud.class);
                        i.putExtra("userName", acta.getNombrePropietario());
                        i.putExtra("userNroSoli", acta.getNroSolicitud());
                        i.putExtra("userParentesco", acta.getParentesco());
                        i.putExtra("userCuponPago", acta.getCuponPagoCodigo());
                        i.putExtra("userTipoLibro", acta.getTipoSolicitud());
                        i.putExtra("userFecha", acta.getFechaSolicitud());
                        i.putExtra("userEstado", acta.getEstadoSolicitud());
                        startActivity(i);
                    }
                }
            }
        });
    }

    public List<Map<String, String>> getListItems(List<SolicitudActa> userList) {
        final List<Map<String, String>> listItem =
                new ArrayList<>(userList.size());

        for (final SolicitudActa solicitud : userList) {
            final Map<String, String> listItemMap = new HashMap<>();
            listItemMap.put(TEXT1, solicitud.getNombrePropietario());
            listItemMap.put(TEXT2, solicitud.getParentesco());
            listItem.add(Collections.unmodifiableMap(listItemMap));
        }

        return Collections.unmodifiableList(listItem);
    }

    private ListAdapter createListAdapter(List<SolicitudActa> userList) {
        final String[] fromMapKey = new String[] {TEXT1, TEXT2};
        final int[] toLayoutId = new int[] {android.R.id.text1, android.R.id.text2};
        final List<Map<String, String>> list = getListItems(userList);

        return new SimpleAdapter(this, list,
                android.R.layout.simple_list_item_2,
                fromMapKey, toLayoutId);
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
}
