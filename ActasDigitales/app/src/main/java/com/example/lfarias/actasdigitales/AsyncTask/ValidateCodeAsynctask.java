package com.example.lfarias.actasdigitales.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Helpers.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by acer on 18/09/2017.
 */

public class ValidateCodeAsynctask extends AsyncTask<ConnectionParams, Void, List<String>> {
    Context context;
    Callback callback;
    ProgressDialog dialog;

    public ValidateCodeAsynctask(Context context, Callback callback, ProgressDialog dialog) {
        this.callback = callback;
        this.context = context;
    }

    public interface Callback {
        void validateCode(Boolean success);
    }

    @Override
    protected List<String> doInBackground(ConnectionParams... params) {
        List<String> resultSet = new ArrayList<>();

        try {
            URL urlEncoded = Utils.urlBuilder(params[0].getmControllerId(), params[0].getmActionId(), params[0].getParams());
            String urlDecoded = URLDecoder.decode(urlEncoded.toString(), "UTF-8");
            URL url = new URL(urlDecoded);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
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
            case 5:
                callback.validateCode(Boolean.parseBoolean(result.get(0)));
                break;

            default:
                dialog.hide();
                Utils.createGlobalDialog(context, "ERROR", "Ocurrio un error al obtener los datos del servidor, revise los datos o intente mas tarde").show();
        }
    }
}
