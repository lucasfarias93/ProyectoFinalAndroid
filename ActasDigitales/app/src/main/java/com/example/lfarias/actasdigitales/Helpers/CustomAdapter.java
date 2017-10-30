package com.example.lfarias.actasdigitales.Helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lfarias.actasdigitales.Activities.DetalleSolicitud;
import com.example.lfarias.actasdigitales.Activities.MisSolicitudesActivity;
import com.example.lfarias.actasdigitales.AsyncTask.CancelRequestAsynctask;
import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Entities.SolicitudActa;
import com.example.lfarias.actasdigitales.MercadoPago.MainExample.MPMainActivity;
import com.example.lfarias.actasdigitales.R;
import com.example.lfarias.actasdigitales.Services.ServiceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lfarias on 10/17/17.
 */

public class CustomAdapter extends ArrayAdapter<SolicitudActa> {

    private Context context;
    private ArrayList<SolicitudActa> myList;
    private CancelRequestAsynctask.Callback myCallback;

    private LayoutInflater mInflater;
    private boolean mNotifyOnChange = true;

    public CustomAdapter(Context context, ArrayList<SolicitudActa> mPersons, CancelRequestAsynctask.Callback callback) {
        super(context, R.layout.item_list);
        this.context = context;
        this.myList = new ArrayList<SolicitudActa>(mPersons);
        this.mInflater = LayoutInflater.from(context);
        this.myCallback = callback;
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public SolicitudActa getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getPosition(SolicitudActa item) {
        return myList.indexOf(item);
    }

    @Override
    public int getViewTypeCount() {
        return 1; //Number of types + 1 !!!!!!!!
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        int type = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case 1:
                    convertView = mInflater.inflate(R.layout.item_list,parent, false);
                    holder.nombreProp = (TextView) convertView.findViewById(R.id.nombre_prop);
                    holder.estadoActa = (TextView) convertView.findViewById(R.id.estado_acta);
                    holder.parentesco = (TextView) convertView.findViewById(R.id.parentesco);
                    holder.pagar = (ImageView) convertView.findViewById(R.id.pagar);
                    holder.cancelar = (ImageView) convertView.findViewById(R.id.borrar);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nombreProp.setText(myList.get(position).getNombreParentesco());
        holder.estadoActa.setText(myList.get(position).getNombreEstadoSolicitud());
        holder.parentesco.setText(myList.get(position).getNombreParentesco());
        holder.pagar.setImageDrawable(getContext().getDrawable(R.drawable.payment));
        holder.cancelar.setImageDrawable(getContext().getDrawable(R.drawable.trash));
        if("Confirmada".equals(holder.estadoActa.getText().toString())){
            holder.pagar.setVisibility(View.VISIBLE);
            holder.pagar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), MPMainActivity.class);
                    getContext().startActivity(i);
                }
            });
            holder.cancelar.setVisibility(View.VISIBLE);
            holder.cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProgressDialog dialog = Utils.createLoadingIndicator(getContext());
                    CancelRequestAsynctask asynctask = new CancelRequestAsynctask(getContext(), myCallback, dialog);
                    List<String> params = new ArrayList<>();

                    SolicitudActa acta1 = getItem(position);
                    params.add(acta1.getId());

                    ConnectionParams conectParams = new ConnectionParams();
                    conectParams.setmControllerId(ServiceUtils.Controllers.CIUDADANO_CONTROLLER + "/" + ServiceUtils.Controllers.LISTADO_PATH);
                    conectParams.setmActionId(ServiceUtils.Actions.CANCELAR_SOLICITUD);
                    conectParams.setmSearchType(ServiceUtils.SearchType.CANCELAR_SOLICITUD_SEARCH_TYPE);
                    conectParams.setParams(params);
                    dialog.show();
                    asynctask.execute(conectParams);
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SolicitudActa acta = getItem(position);
                    Intent i = new Intent(getContext(), DetalleSolicitud.class);
                    i.putExtra("userName", acta.getNombrePropietarioActa());
                    i.putExtra("userNroSoli", acta.getId());
                    i.putExtra("userParentesco", acta.getNombreParentesco());
                    i.putExtra("userCuponPago", acta.getCodigoDePago());
                    i.putExtra("userTipoLibro", acta.getNombrelibro());
                    i.putExtra("userFecha", acta.getFechaCambioEstado());
                    i.putExtra("userEstado", acta.getNombreEstadoSolicitud());
                    getContext().startActivity(i);
                }
            });
        } else {
            holder.pagar.setVisibility(View.GONE);
            holder.cancelar.setVisibility(View.GONE);
        }
        holder.pos = position;
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }


    //---------------static views for each row-----------//
    static class ViewHolder {

        TextView nombreProp;
        TextView estadoActa;
        TextView parentesco;
        ImageView pagar;
        ImageView cancelar;
        int pos; //to store the position of the item within the list
    }
}