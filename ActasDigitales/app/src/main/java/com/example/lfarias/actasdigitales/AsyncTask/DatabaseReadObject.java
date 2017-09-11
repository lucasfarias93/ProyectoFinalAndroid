package com.example.lfarias.actasdigitales.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Helpers.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by acer on 28/08/2017.
 */

public class DatabaseReadObject extends AsyncTask<ConnectionParams, Void, List<String>> {

    Context context;
    Callback callback;

    public DatabaseReadObject(Context context, Callback callback) {
        this.callback = callback;
        this.context = context;
    }

    public interface Callback {
        void getUserData(JSONObject object);
        void getProvinces(JSONObject object) throws JSONException;
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

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in=new BufferedReader(new
                        InputStreamReader(
                        conn.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();
                resultSet.add(sb.toString());
                resultSet.add(params[0].getmSearchType().toString());
                return resultSet;

            }
            else {
                resultSet.add(new String("false : "+responseCode));
                resultSet.add(params[0].getmSearchType().toString());
                return resultSet;
            }
        }
        catch(Exception e){
            resultSet.add(new String("Exception: " + e.getMessage()));
            resultSet.add(params[0].getmSearchType().toString());
            return resultSet;
        }

    }

    @Override
    protected void onPostExecute(List<String> result) {
        Integer searchType = Integer.parseInt(result.get(1));
        JSONObject obj;
        try{
            switch (searchType){
                case 0:
                     obj = Utils.convertStringIntoJson(result.get(0));
                    callback.getUserData(obj);
                    break;

                case 1:
                    obj = Utils.convertStringIntoJson(result.get(0));
                    callback.getProvinces(obj);
                    break;
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
    }
}
