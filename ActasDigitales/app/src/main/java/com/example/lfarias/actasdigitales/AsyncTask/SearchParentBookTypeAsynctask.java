package com.example.lfarias.actasdigitales.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.lfarias.actasdigitales.Cache.CacheService;
import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Helpers.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by acer on 22/10/2017.
 */

public class SearchParentBookTypeAsynctask extends AsyncTask<ConnectionParams, Void, List<String>> {

    Context context;
    Callback callback;
    ProgressDialog dialog;

    public SearchParentBookTypeAsynctask(Context context, Callback callback) {
        this.callback = callback;
        this.context = context;
    }

    public interface Callback {
        void getParent(Object success) throws JSONException;
    }

    @Override
    protected List<String> doInBackground(ConnectionParams... params) {
        List<String> resultSet = new ArrayList<>();

        try {
            URL url = Utils.urlBuilder(params[0].getmControllerId(), params[0].getmActionId(), params[0].getParams());

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
            case 12:
                if (!result.get(0).toString().isEmpty()) {
                    try {
                        JSONObject obj = Utils.convertStringIntoJson(result.get(0));
                        callback.getParent(result.get(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                } else {
                    Utils.createGlobalDialog(context, "Error en el inicio de sesión", "Ocurrio un error al obtener los datos del servidor, revise los datos o intente mas tarde").show();
                    break;
                }

            default:
                Utils.createGlobalDialog(context, "Error en el inicio de sesión", "Ocurrio un error al obtener los datos del servidor, revise los datos o intente mas tarde").show();
                break;
        }
    }
}
