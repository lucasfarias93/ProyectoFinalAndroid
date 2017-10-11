package com.example.lfarias.actasdigitales.Helpers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.view.ContextThemeWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.lfarias.actasdigitales.R;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

/**
 * Creado por Lucas.Farias
 *
 * Archivo creado: 05 de Septiembre de 2017
 *
 * Descripción: Metodos de acceso global, usados tanto para la conexión al web service, como transformación de
 *              datos, armado de dialogos, etc.
 *
 */

public class Utils {

    public static final String SCHEME_URL = "http";
    public static final String SERVER_ADDRESS = "190.15.213.87:81";

    public static AlertDialog.Builder createGlobalDialog(Context context, String title, String message){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.AppTheme_PopupOverlay);
            builder = new AlertDialog.Builder(ctw);
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
                .setIcon(R.drawable.information);
        return builder;
    }

    public static URL urlBuilder(String controller, String actions, List<String> parameters) throws MalformedURLException, UnsupportedEncodingException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME_URL)
                .encodedAuthority(SERVER_ADDRESS)
                .appendPath(controller)
                .appendPath(actions);
        for(String param : parameters){
            builder.appendPath(param);
        }

        String myUrl = builder.build().toString();
        String afterDecode = URLDecoder.decode(myUrl, "UTF-8");
        URL url = new URL(afterDecode);
        return url;
    }

    public static JSONObject convertStringIntoJson(String json) throws JSONException{
        //funcions per a cridar el string amb JSON i convertir-lo de nou a JSON
        JSONObject jsonObject = new JSONObject(json);
        return jsonObject;
    }

    public static boolean emailValidator(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static ProgressDialog createLoadingIndicator(Context context){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Cargando");
        dialog.setMessage("Estamos procesando la información. Por favor espere...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        return dialog;
    }
}