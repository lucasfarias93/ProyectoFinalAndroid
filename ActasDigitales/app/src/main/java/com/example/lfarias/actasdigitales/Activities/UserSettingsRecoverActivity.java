package com.example.lfarias.actasdigitales.Activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.lfarias.actasdigitales.AsyncTask.SendEmailAsynctask;
import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Helpers.Utils;
import com.example.lfarias.actasdigitales.R;
import com.example.lfarias.actasdigitales.Services.ServiceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserSettingsRecoverActivity extends AppCompatActivity implements SendEmailAsynctask.Callback {

    @Bind(R.id.button_submit_code)
    Button mButtonCodeContinue;
    @Bind(R.id.button_code_exists)
    Button mButtonCodeExists;
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
    @Bind(R.id.text_new_code)
    TextView mTextNewCodeInstructions;
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
    ProgressDialog dialog;
    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings_recover);
        ButterKnife.bind(this);

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
        mTextNewCodeInstructions.setVisibility(View.GONE);
        mButtonCodeExists.setOnClickListener(new View.OnClickListener() {
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

        mButtonCodeNotExists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString();
                if(email.isEmpty()){
                    mEmail.setError("Este campo es obligatorio para continuar");
                } else if(!Utils.emailValidator(email)){
                    mEmail.setError("El email ingresado no es correcto o posee un formato inválido. Por favor reviselo y corrigalo");
                } else {
                    mTextIntroduction.setVisibility(View.VISIBLE);
                    mTextInit.setVisibility(View.GONE);
                    mEmail.setVisibility(View.GONE);
                    mButtonCodeExists.setVisibility(View.GONE);
                    mButtonCodeLayout.setVisibility(View.VISIBLE);
                    mButtonCodeNotExists.setVisibility(View.INVISIBLE);
                    mButtonCodeContinue.setVisibility(View.VISIBLE);
                    mTextLinkNewCode.setVisibility(View.VISIBLE);

                    sendEmail(mEmail.getText().toString());
                    createNotifications2();
                }
            }
        });

        mTextLinkNewCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextIntroduction.setVisibility(View.GONE);
                mTextInit.setVisibility(View.GONE);
                mTextCodeExists.setVisibility(View.GONE);
                mTextNewCodeInstructions.setVisibility(View.VISIBLE);

            }
        });

        mButtonCodeContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPasswordLayout.setVisibility(View.VISIBLE);
                mButtonCodeContinue.setVisibility(View.GONE);
                mTextLinkNewCode.setVisibility(View.GONE);
                mPutCode.setVisibility(View.GONE);
                mInputCode.setVisibility(View.GONE);
                mNuevaContraseñaIndicaciones.setVisibility(View.VISIBLE);
                mTextIntroduction.setVisibility(View.GONE);
                mTextCodeExists.setVisibility(View.GONE);
                mTextInit.setVisibility(View.GONE);
                mTextLinkNewCode.setVisibility(View.GONE);
                mTextNewCodeInstructions.setVisibility(View.GONE);
                mNewContraseñaSubmit.setVisibility(View.VISIBLE);
            }
        });

        mNewContraseñaSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Utils.createGlobalDialog(LoginActivity.class, "Contraseña modificada con éxito","Por favor inicie sesión con su nueva contraseña para continuar usando la aplicación").show();
                Intent i = new Intent(UserSettingsRecoverActivity.this, LoginActivity.class);
                startActivity(i);
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
        conectParams.setmControllerId(ServiceUtils.Controllers.RECUPERACION_CONTRASEÑA_PATH + "/" + ServiceUtils.Controllers.RECUPERACION_CONTRASEÑA_CONTROLLER);
        conectParams.setmActionId(ServiceUtils.Actions.ENVIAR_CODIGO);
        conectParams.setmSearchType(ServiceUtils.SearchType.ENVIAR_CODIGO);
        conectParams.setParams(params);
        dialog.show();
        provincesDataRetrieveAsynctask.execute(conectParams);
    }

    @Override
    public void sendEmail(Boolean success) {
        //if(success){
        dialog.hide();
        createNotifications2();
        /*} else {
            Utils.createGlobalDialog(UserSettingsRecoverActivity.this, "ERROR", "Se produjo un error al enviar el código. Por favor intente nuevamente").show();
        }*/
    }
}

