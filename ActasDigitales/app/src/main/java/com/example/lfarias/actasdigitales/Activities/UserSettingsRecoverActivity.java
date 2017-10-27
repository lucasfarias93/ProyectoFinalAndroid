package com.example.lfarias.actasdigitales.Activities;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.lfarias.actasdigitales.AsyncTask.ChangePasswordAsynctask;
import com.example.lfarias.actasdigitales.AsyncTask.SendEmailAsynctask;
import com.example.lfarias.actasdigitales.AsyncTask.UserIdAsynctask;
import com.example.lfarias.actasdigitales.AsyncTask.ValidateCodeAsynctask;
import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Entities.Usuarios;
import com.example.lfarias.actasdigitales.Helpers.SQLiteDatabaseHelper;
import com.example.lfarias.actasdigitales.Helpers.Utils;
import com.example.lfarias.actasdigitales.R;
import com.example.lfarias.actasdigitales.Services.ServiceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserSettingsRecoverActivity extends AppCompatActivity implements SendEmailAsynctask.Callback, ValidateCodeAsynctask.Callback, ChangePasswordAsynctask.Callback, UserIdAsynctask.Callback {

    @Bind(R.id.button_submit_code)
    Button mButtonCodeContinue;
    /*@Bind(R.id.button_code_exists)
    Button mButtonCodeExists;*/
    @Bind(R.id.code_layout)
    LinearLayout mButtonCodeLayout;
    @Bind(R.id.button_code_not_exists)
    Button mButtonCodeNotExists;
    @Bind(R.id.text_introduccion)
    TextView mTextIntroduction;
    @Bind(R.id.text_init)
    TextView mTextInit;
    @Bind(R.id.text_code_exists)
    TextView mTextCodeExists;
    @Bind(R.id.link_new_code)
    TextView mTextLinkNewCode;
    @Bind(R.id.email_sent_code)
    EditText mEmail;
    @Bind(R.id.address)
    EditText mInputCode;
    @Bind(R.id.text_nueva_contraseña)
    TextView mNuevaContraseñaIndicaciones;
    @Bind(R.id.nueva_password_layout)
    LinearLayout mPasswordLayout;
    @Bind(R.id.nueva_contraseña)
    EditText mNuevaContraseña;
    @Bind(R.id.repetir_nueva_contraseña)
    EditText mRepetirContraseña;
    @Bind(R.id.textView3)
    TextView mPutCode;
    @Bind(R.id.button_submit_contraseña)
    Button mNewContraseñaSubmit;

    Context context;
    SQLiteDatabaseHelper helper;
    ProgressDialog dialog;
    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    Usuarios user = new Usuarios();
    String password;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings_recover);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("Recuperar contraseña");
        mActionBar.setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        helper = new SQLiteDatabaseHelper(this);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(getIntent().getIntExtra(NOTIFICATION_ID, -1));

        dialog = Utils.createLoadingIndicator(this);

        context = getApplicationContext();

        mButtonCodeLayout.setVisibility(View.INVISIBLE);
        mButtonCodeContinue.setVisibility(View.INVISIBLE);
        mTextIntroduction.setVisibility(View.GONE);
        mTextCodeExists.setVisibility(View.GONE);
        mTextInit.setVisibility(View.VISIBLE);
        mPasswordLayout.setVisibility(View.GONE);
        mEmail.setVisibility(View.VISIBLE);
        mNuevaContraseñaIndicaciones.setVisibility(View.GONE);
        mTextLinkNewCode.setVisibility(View.GONE);
        mTextLinkNewCode.setTextColor(Color.BLUE);
        /*mButtonCodeExists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonCodeLayout.setVisibility(View.VISIBLE);
                mButtonCodeExists.setVisibility(View.INVISIBLE);
                mButtonCodeNotExists.setVisibility(View.GONE);
                mEmail.setVisibility(View.GONE);
                mTextInit.setVisibility(View.GONE);
                mTextCodeExists.setVisibility(View.VISIBLE);
                mButtonCodeContinue.setVisibility(View.VISIBLE);
                mTextLinkNewCode.setVisibility(View.VISIBLE);

            }
        });
*/
        mButtonCodeNotExists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = mEmail.getText().toString();
                if(email.isEmpty()){
                    mEmail.setError("Este campo es obligatorio para continuar");
                } else if(!Utils.emailValidator(email)){
                    mEmail.setError("El email ingresado no es correcto o posee un formato inválido. Por favor reviselo y corrigalo");
                } else {
                    user.setEmail(mEmail.getText().toString());
                    sendEmail(email);
                    //createNotifications2();
                }
            }
        });

        mTextLinkNewCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextIntroduction.setVisibility(View.GONE);
                mTextInit.setVisibility(View.GONE);
                mTextCodeExists.setVisibility(View.GONE);
                user.setEmail(mEmail.getText().toString());
                sendEmail(mEmail.getText().toString());
            }
        });

        mButtonCodeContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputCode.getText().toString();
                ValidateCodeAsynctask asynctask = new ValidateCodeAsynctask(UserSettingsRecoverActivity.this, UserSettingsRecoverActivity.this, dialog);
                List<String> params = new ArrayList<>();
                params.add( mInputCode.getText().toString());

                ConnectionParams conectParams = new ConnectionParams();
                conectParams.setmControllerId(ServiceUtils.Controllers.RECUPERACION_CONTRASEÑA_PATH + "/" + ServiceUtils.Controllers.COMMON_INDEX_METHOD);
                conectParams.setmActionId(ServiceUtils.Actions.VERIFICAR_CODIGO);
                conectParams.setmSearchType(ServiceUtils.SearchType.VALIDAR_CODIGO);
                conectParams.setParams(params);
                dialog.show();
                asynctask.execute(conectParams);
            }
        });

        mNewContraseñaSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contraseña = mNuevaContraseña.getText().toString();
                String repetirContraseña = mRepetirContraseña.getText().toString();

                if(contraseña.isEmpty()){
                    mNuevaContraseña.setError("Este campo es obligatorio");
                    if(repetirContraseña.isEmpty()){
                        mRepetirContraseña.setError("Este campo es obligatorio");
                    }
                } else if(repetirContraseña.isEmpty()){
                    mRepetirContraseña.setError("Este campo es obligatorio");
                } else if(!contraseña.equals(repetirContraseña)){
                    mNuevaContraseña.setError("Las nuevas contraseñas deben conicidir");
                } else {
                    password = mNuevaContraseña.getText().toString();
                    //TODO: hace falta loguearse para obtener el id de usuario.. Porque necesito el id de usuario (logueandome)
                    // si estoy fuera del login
                    ChangePasswordAsynctask asynctask = new ChangePasswordAsynctask(UserSettingsRecoverActivity.this, UserSettingsRecoverActivity.this, dialog);
                    List<String> params = new ArrayList<>();
                    params.add(email);
                    params.add(password);

                    ConnectionParams conectParams = new ConnectionParams();
                    conectParams.setmControllerId(ServiceUtils.Controllers.RECUPERAR_CONTRASEÑA_CAMBIO_CONTROLLER + "/" + ServiceUtils.Controllers.COMMON_INDEX_METHOD);
                    conectParams.setmActionId(ServiceUtils.Actions.CAMBIAR_CLAVE_MOBILE);
                    conectParams.setmSearchType(ServiceUtils.SearchType.CAMBIAR_CLAVE_MOBILE);
                    conectParams.setParams(params);
                    dialog.show();
                    asynctask.execute(conectParams);
                   /* UserIdAsynctask asynctask = new UserIdAsynctask(UserSettingsRecoverActivity.this, UserSettingsRecoverActivity.this, dialog);

                    ConnectionParams conectParams = new ConnectionParams();
                    conectParams.setmControllerId(ServiceUtils.Controllers.CIUDADANO_CONTROLLER + "/" + ServiceUtils.Controllers.COMMON_INDEX_METHOD);
                    conectParams.setmActionId(ServiceUtils.Actions.CIUDADANO_ID);
                    conectParams.setmSearchType(ServiceUtils.SearchType.USER_ID_SEARCH_TYPE);
                    dialog.show();
                    asynctask.execute(conectParams);*/
                }

            }
        });
    }

    public void createNotifications2() {
        int notificationId = new Random().nextInt(); // just use a counter in some util class...
        PendingIntent dismissIntent = UserSettingsRecoverActivity.getDismissIntent(notificationId, context);

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custon_notification);
        contentView.setTextViewText(R.id.title, "Custom notification");
        contentView.setTextViewText(R.id.text, "This is a custom layout");

        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.info_black)
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setContentTitle("Recuperación de cuenta")
                        .setContentText("Se le ha enviado un email a su casilla de correo con...")
                        .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Se le ha enviado un email a su casilla de correo con el código de recuperación de contraseña. Por favor revise su casilla de correo"))
                        .addAction(R.drawable.clear,
                                "Cerrar notificacion", dismissIntent);

        NotificationManager notifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notifyMgr.notify(notificationId, builder.build());
    }

    public static PendingIntent getDismissIntent(int notificationId, Context context) {
        Intent intent = new Intent(context, UserSettingsRecoverActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        PendingIntent dismissIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return dismissIntent;
    }

    public void sendEmail(String email) {
        SendEmailAsynctask provincesDataRetrieveAsynctask = new SendEmailAsynctask(UserSettingsRecoverActivity.this, UserSettingsRecoverActivity.this, dialog);
        List<String> params = new ArrayList<>();
        params.add(email);

        ConnectionParams conectParams = new ConnectionParams();
        conectParams.setmControllerId(ServiceUtils.Controllers.RECUPERACION_CONTRASEÑA_PATH + "/" + ServiceUtils.Controllers.COMMON_INDEX_METHOD);
        conectParams.setmActionId(ServiceUtils.Actions.ENVIAR_CODIGO);
        conectParams.setmSearchType(ServiceUtils.SearchType.ENVIAR_CODIGO);
        conectParams.setParams(params);
        dialog.show();
        provincesDataRetrieveAsynctask.execute(conectParams);
    }

    @Override
    public void sendEmail(Boolean success) {
        if(success){
        dialog.dismiss();
        createNotifications2();
            mTextIntroduction.setVisibility(View.VISIBLE);
            mTextInit.setVisibility(View.GONE);
            mEmail.setVisibility(View.GONE);
            //mButtonCodeExists.setVisibility(View.GONE);
            mButtonCodeLayout.setVisibility(View.VISIBLE);
            mButtonCodeNotExists.setVisibility(View.INVISIBLE);
            mButtonCodeContinue.setVisibility(View.VISIBLE);
            mTextLinkNewCode.setVisibility(View.VISIBLE);
        } else {
            dialog.dismiss();
        }
    }

    @Override
    public void validateCode(Boolean success) {

            mTextLinkNewCode.setVisibility(View.GONE);
            if(success){
                dialog.dismiss();
                mPasswordLayout.setVisibility(View.VISIBLE);
                mButtonCodeContinue.setVisibility(View.GONE);
                mTextLinkNewCode.setVisibility(View.GONE);
                mPutCode.setVisibility(View.GONE);
                mInputCode.setVisibility(View.GONE);
                mNuevaContraseñaIndicaciones.setVisibility(View.VISIBLE);
                mTextIntroduction.setVisibility(View.GONE);
                mTextCodeExists.setVisibility(View.GONE);
                mTextInit.setVisibility(View.GONE);
            mNewContraseñaSubmit.setVisibility(View.VISIBLE);
        } else {
            dialog.dismiss();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void changePassword(Boolean success) {
        if(success){
            dialog.dismiss();
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mNuevaContraseña.clearFocus();
            mRepetirContraseña.clearFocus();
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            mNuevaContraseña.setText("");
            mRepetirContraseña.setText("");
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().findViewById(R.id.activity_user_settings_recover), "Contraseña cambiada exitosamente", Snackbar.LENGTH_LONG)
                    .setAction("IR AL LOGIN", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(UserSettingsRecoverActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                    });
            // Changing message text color
            snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));

            // Changing action button text color
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(getResources().getColor(R.color.white));
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
        }
        else {
            dialog.dismiss();
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mNuevaContraseña.clearFocus();
            mRepetirContraseña.clearFocus();
            mNuevaContraseña.setText("");
            mRepetirContraseña.setText("");
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().findViewById(R.id.activity_user_settings_recover), "Ocurrio un error al cambiar la contraseña. Intente nuevamente", Snackbar.LENGTH_LONG);

            // Changing action button text color
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(getResources().getColor(R.color.white));
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();

        }
    }

    @Override
    public void getUserId(Object success) {

        if(!success.equals("false")) {

        } else {

        }
    }
}

