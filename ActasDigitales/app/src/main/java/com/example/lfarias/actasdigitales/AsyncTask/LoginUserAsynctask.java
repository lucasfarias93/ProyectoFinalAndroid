package com.example.lfarias.actasdigitales.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.lfarias.actasdigitales.Cache.CacheService;
import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Helpers.DecodeTextUtils;
import com.example.lfarias.actasdigitales.Helpers.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.CacheRequest;
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
 * Created by acer on 18/09/2017.
 */

public class LoginUserAsynctask extends AsyncTask<ConnectionParams, Void, List<String>> {

    Context context;
    Callback callback;
    ProgressDialog dialog;
    String tokenEmpty = "PHPSESSID=";

    public LoginUserAsynctask(Context context, Callback callback, ProgressDialog dialog) {
        this.callback = callback;
        this.context = context;
        this.dialog = dialog;
    }

    public interface Callback {
        void loginUser(int success);
    }

    @Override
    protected List<String> doInBackground(ConnectionParams... params) {
        List<String> resultSet = new ArrayList<>();

        try {
            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);
            URL url = Utils.urlBuilder(params[0].getmControllerId(), params[0].getmActionId(), params[0].getParams());

            System.setProperty("http.keepAlive", "true");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(7000 /* milliseconds */);
            conn.setConnectTimeout(7000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Keep-Alive", "header");
            HttpCookie cookie = new HttpCookie("lang", "es");
            cookie.setDomain("190.15.213.87:81");
            cookie.setPath("/");
            cookie.setVersion(0);
            cookieManager.getCookieStore().add(new URI("http://190.15.213.87:81/"), cookie);
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
                String result  = sb.toString().trim();
                resultSet.add(result);
                if(result.equals("false")){
                    resultSet.add(new StringBuilder().append(500).toString());
                }else {
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
                case 7:
                    callback.loginUser(getResult(result.get(0)));
                    break;

                default:
                    dialog.hide();
                    Utils.createGlobalDialog(context, "Error en el inicio de sesión","Ocurrio un error al obtener los datos del servidor, revise los datos o intente mas tarde").show();
            }
    }

    public int getResult(String result){
        int resultInt;
        String resultado = result.toString().substring(1, result.toString().length()-1);
        if(tokenEmpty.equals(resultado)){
            resultInt = 2;
        } else if ("false".contains(resultado)){
            resultInt = 0;
        } else {
            int tokenString = "=".indexOf(resultado);
            String token = resultado.substring(tokenString + 1);
            CacheService.setTokenSessionId(token);
            resultInt = 2;
        }
        return resultInt;
    }
}
