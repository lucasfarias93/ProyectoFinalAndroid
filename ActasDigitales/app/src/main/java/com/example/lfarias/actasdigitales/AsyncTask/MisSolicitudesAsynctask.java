package com.example.lfarias.actasdigitales.AsyncTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.view.ContextThemeWrapper;

import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Helpers.Utils;
import com.example.lfarias.actasdigitales.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by lfarias on 10/26/17.
 */

public class MisSolicitudesAsynctask extends AsyncTask<ConnectionParams, Void, List<String>> {
    Context context;
    Callback callback;
    List<JSONObject> listadoSolicitudes = new ArrayList<>();

    public MisSolicitudesAsynctask(Context context, Callback callback) {
        this.callback = callback;
        this.context = context;
    }

    public interface Callback {
        void getSolicitudesList(List<JSONObject> success);
    }

    @Override
    protected List<String> doInBackground(ConnectionParams... params) {
        List<String> resultSet = new ArrayList<>();

        try {
            URL urlEncoded = Utils.urlBuilder(params[0].getmControllerId(), params[0].getmActionId(), params[0].getParams());
            String urlDecoded = URLDecoder.decode(urlEncoded.toString(), "UTF-8");
            URL url = new URL(urlDecoded);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(7000);
            conn.setConnectTimeout(7000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(
                        conn.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();
                String result = sb.toString().trim();
                resultSet.add(result);
                if (result.equals("false")) {
                    resultSet.add(new StringBuilder().append(500).toString());
                } else {
                    resultSet.add(params[0].getmSearchType().toString());
                }
                return resultSet;

            } else {
                resultSet.add(new String("false:" + responseCode));
                resultSet.add(new StringBuilder().append(500).toString());
                return resultSet;
            }
        } catch (Exception e) {

            resultSet.add(new String("Exception:" + e.getMessage()));
            resultSet.add(new StringBuilder().append(500).toString());
            return resultSet;
        }

    }

    @Override
    protected void onPostExecute(List<String> result) {
        Integer searchType = Integer.parseInt(result.get(1));

        switch (searchType) {
            case 13:
                try {
                    JSONObject object = new JSONObject(result.get(0));
                    JSONArray array = object.getJSONArray("listSolicitudacta");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object_list = array.getJSONObject(i);
                        listadoSolicitudes.add(object_list);
                    }
                    callback.getSolicitudesList(listadoSolicitudes);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            default:
                final AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.AppTheme_PopupOverlay);
                    builder = new AlertDialog.Builder(ctw);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle("Error de obtencion de solicitudes")
                        .setMessage("Ocurrio un error al obtener las solicitudes asociadas a su usuario. Por favor intente nuevamente mas tarde o contacte al soporte.")
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(R.drawable.error_1)
                        .show();
        }
    }
}
