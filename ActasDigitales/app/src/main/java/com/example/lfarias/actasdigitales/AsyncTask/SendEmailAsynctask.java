package com.example.lfarias.actasdigitales.AsyncTask;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.view.ContextThemeWrapper;

import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Helpers.DecodeTextUtils;
import com.example.lfarias.actasdigitales.Helpers.Utils;
import com.example.lfarias.actasdigitales.R;

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
 * Created by acer on 17/09/2017.
 */

public class SendEmailAsynctask extends AsyncTask<ConnectionParams, Void, List<String>> {
    Context context;
    Callback callback;
    ProgressDialog dialog;

    public SendEmailAsynctask(Context context, Callback callback, ProgressDialog dialog) {
        this.callback = callback;
        this.context = context;
    }

    public interface Callback {
        void sendEmail(Boolean success);
    }

    @Override
    protected List<String> doInBackground(ConnectionParams... params) {
        List<String> resultSet = new ArrayList<>();

        try {
            URL urlEncoded = Utils.urlBuilder(params[0].getmControllerId(), params[0].getmActionId(), params[0].getParams());
            String urlDecoded = URLDecoder.decode(urlEncoded.toString(), "UTF-8");
            URL url = new URL(urlDecoded);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(7000 /* milliseconds */);
            conn.setConnectTimeout(7000 /* milliseconds */);
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
            case 4:
                if(contains(result.get(0))){
                    callback.sendEmail(true);
                }
                break;

            default:
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.AppTheme_PopupOverlay);
                    builder = new AlertDialog.Builder(ctw);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle("Error al enviar el código")
                        .setMessage("Ocurrio un error al enviar el código a su email. Por favor intente nuevamente mas tarde o contacte al soporte.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                callback.sendEmail(false);
                            }
                        })
                        .setIcon(R.drawable.information)
                        .show();

        }
    }

    public boolean contains(String result){
        if(result.contains("true")){
            return true;
        } else {
            return false;
        }
    }
}

