package com.example.lfarias.actasdigitales.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.lfarias.actasdigitales.Entities.Roles;
import com.example.lfarias.actasdigitales.Helpers.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by acer on 28/08/2017.
 */

public class DatabaseReadObject extends AsyncTask<String, Void, String> {

    Context context;
    //List<Object> object = new ArrayList<>();

    public DatabaseReadObject(Context context) {
        this.context = context;
    }

    //public interface Callback {
    //    void getResultSet(List<Object> object);
    //}

    @Override
    protected String doInBackground(String... params) {

        try {
            URL url = new URL("http://190.15.213.87:81/tramitedni");

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("dni", "36850606");
            Log.e("params",postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(Utils.getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

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
        Toast.makeText(context, result,
                Toast.LENGTH_LONG).show();
    }
}
