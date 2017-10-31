package com.example.lfarias.actasdigitales.Helpers;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
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

public class CustomAdapter extends ArrayAdapter<SolicitudActa> implements Filterable{

    private Context context;
    private ArrayList<SolicitudActa> myList;
    private CancelRequestAsynctask.Callback myCallback;
    private ArrayList<SolicitudActa> orig;

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
                    holder.success = (ImageView) convertView.findViewById(R.id.success);
                    holder.delete = (ImageView) convertView.findViewById(R.id.delete);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nombreProp.setText(myList.get(position).getNombrePropietarioActa());
        holder.estadoActa.setText(myList.get(position).getNombreEstadoSolicitud());
        holder.parentesco.setText(myList.get(position).getNombreParentesco());
        holder.pagar.setImageDrawable(getContext().getDrawable(R.drawable.payment));
        holder.cancelar.setImageDrawable(getContext().getDrawable(R.drawable.trash));
        holder.delete.setImageDrawable(getContext().getDrawable(R.drawable.delete));
        holder.success.setImageDrawable(getContext().getDrawable(R.drawable.success_1));

        holder.delete.setVisibility(View.INVISIBLE);
        holder.success.setVisibility(View.INVISIBLE);
        if("Confirmada".equals(holder.estadoActa.getText().toString())){
            holder.pagar.setVisibility(View.VISIBLE);
            holder.pagar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MisSolicitudesActivity)context).finish();
                    Intent i = new Intent(getContext(), MPMainActivity.class);
                    i.putExtra("idSolicitud", myList.get(position).getId());
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
                    holder.pagar.setVisibility(View.INVISIBLE);
                    holder.cancelar.setVisibility(View.INVISIBLE);

                    holder.delete.setVisibility(View.VISIBLE);
                    holder.success.setVisibility(View.VISIBLE);

                }
            });

        } else if("Cancelada".equals(holder.estadoActa.getText().toString())){
            holder.pagar.setVisibility(View.INVISIBLE);
            holder.cancelar.setVisibility(View.INVISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
        } else if("Pagada".equals(holder.estadoActa.getText().toString())){
            holder.pagar.setVisibility(View.INVISIBLE);
            holder.cancelar.setVisibility(View.INVISIBLE);
            holder.delete.setVisibility(View.INVISIBLE);
            holder.success.setVisibility(View.VISIBLE);
        } else if("Pendiente de pago".equals(holder.estadoActa.getText().toString())){
            holder.pagar.setVisibility(View.INVISIBLE);
            holder.cancelar.setVisibility(View.INVISIBLE);
            holder.delete.setVisibility(View.INVISIBLE);
            holder.success.setVisibility(View.INVISIBLE);
        }

        holder.pos = position;
        return convertView;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<SolicitudActa> results = new ArrayList<>();
                if (orig == null)
                    orig = myList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final SolicitudActa g : orig) {
                            if ((g.getNombrePropietarioActa().toLowerCase().contains(constraint.toString())) || ((g.getNombreEstadoSolicitud().toLowerCase().contains(constraint.toString()))) || ((g.getNombreParentesco().toLowerCase().contains(constraint.toString())))){
                                results.add(g);
                            }
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                myList = (ArrayList<SolicitudActa>) results.values;
                notifyDataSetChanged();
            }
        };
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
        ImageView delete;
        ImageView success;
        int pos; //to store the position of the item within the list
    }
}