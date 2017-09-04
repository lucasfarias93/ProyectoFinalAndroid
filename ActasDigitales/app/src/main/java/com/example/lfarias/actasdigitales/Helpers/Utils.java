package com.example.lfarias.actasdigitales.Helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.lfarias.actasdigitales.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by lfarias on 9/1/17.
 */

public class Utils {

    public static final String SCHEME_URL = "http";
    public static final String SERVER_ADDRESS = "190.15.213.87:81";

    public static AlertDialog.Builder createGlobalDialog(Context context, String title, String message){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(R.drawable.alerts);
        return builder;
    }

    public static URL urlBuilder(String controller, String actions, List<String> parameters) throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME_URL)
                .encodedAuthority(SERVER_ADDRESS)
                .appendPath(controller)
                .appendPath(actions);
        for(String param : parameters){
            builder.appendPath(param);
        }

        String myUrl = builder.build().toString();
        URL url = new URL(myUrl);
        return url;
    }

    public static JSONObject convertStringIntoJson(String json) throws JSONException{
        //funcions per a cridar el string amb JSON i convertir-lo de nou a JSON
        JSONObject jsonObject = new JSONObject(json);
        return jsonObject;
    }
}