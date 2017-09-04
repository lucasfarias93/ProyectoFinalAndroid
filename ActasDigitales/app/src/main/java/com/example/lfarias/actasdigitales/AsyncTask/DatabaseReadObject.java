package com.example.lfarias.actasdigitales.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Helpers.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by acer on 28/08/2017.
 */

public class DatabaseReadObject extends AsyncTask<ConnectionParams, Void, String> {

    Context context;
    Callback callback;
    //List<Object> object = new ArrayList<>();

    public DatabaseReadObject(Context context, Callback callback) {
        this.callback = callback;
        this.context = context;
    }

    public interface Callback {
        void getResultSet(JSONObject object);
    }

    @Override
    protected String doInBackground(ConnectionParams... params) {

        try {
            /*Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority("190.15.213.87:81")
                    .appendPath("tramitedni")
                    .appendPath("buscar_ciudadano_por_id_dni_mobile")
                    .appendPath("19114724")
                    .appendPath("36850606");
            String myUrl = builder.build().toString();*/
            URL url = Utils.urlBuilder(params[0].getController_id(), params[0].getAction_id(), params[0].getParams());

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
                return sb.toString();

            }
            else {
                return new String("false : "+responseCode);
            }
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result) {
        //Toast.makeText(context, result,
        //        Toast.LENGTH_LONG).show();
        try{
            JSONObject obj = Utils.convertStringIntoJson(result);
            callback.getResultSet(obj);
        } catch(JSONException e){
            e.printStackTrace();
        }
    }
}
