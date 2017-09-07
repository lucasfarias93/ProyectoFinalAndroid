package com.example.lfarias.actasdigitales.Activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lfarias.actasdigitales.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserSettingsRecoverActivity extends AppCompatActivity {

    @Bind(R.id.button_submit_code)
    Button mButtonCodeContinue;
    @Bind(R.id.button_code_exists)
    Button mButtonCodeExists;
    @Bind(R.id.code_layout)
    LinearLayout mButtonCodeLayout;
    @Bind(R.id.button_code_not_exists) Button mButtonCodeNotExists;
    @Bind(R.id.text_introduccion)
    TextView mTextIntroduction;
    @Bind(R.id.text_init) TextView mTextInit;
    @Bind(R.id.text_code_exists) TextView mTextCodeExists;
    @Bind(R.id.link_new_code) TextView mTextLinkNewCode;
    @Bind(R.id.text_new_code) TextView mTextNewCodeInstructions;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings_recover);
        ButterKnife.bind(this);

        context = getApplicationContext();

        mButtonCodeLayout.setVisibility(View.INVISIBLE);
        mButtonCodeContinue.setVisibility(View.INVISIBLE);
        mTextIntroduction.setVisibility(View.GONE);
        mTextCodeExists.setVisibility(View.GONE);
        mTextInit.setVisibility(View.VISIBLE);
        mTextLinkNewCode.setVisibility(View.GONE);
        mTextLinkNewCode.setTextColor(Color.BLUE);
        mTextNewCodeInstructions.setVisibility(View.GONE);
        mButtonCodeExists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonCodeLayout.setVisibility(View.VISIBLE);
                mButtonCodeExists.setVisibility(View.INVISIBLE);
                mButtonCodeNotExists.setVisibility(View.GONE);
                mTextInit.setVisibility(View.GONE);
                mTextCodeExists.setVisibility(View.VISIBLE);
                mButtonCodeContinue.setVisibility(View.VISIBLE);
                mTextLinkNewCode.setVisibility(View.VISIBLE);

            }
        });

        mButtonCodeNotExists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextIntroduction.setVisibility(View.VISIBLE);
                mTextInit.setVisibility(View.GONE);
                mButtonCodeExists.setVisibility(View.GONE);
                mButtonCodeLayout.setVisibility(View.VISIBLE);
                mButtonCodeNotExists.setVisibility(View.INVISIBLE);
                mButtonCodeContinue.setVisibility(View.VISIBLE);
                mTextLinkNewCode.setVisibility(View.VISIBLE);
                createNotifications();
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
    }

    public void createNotifications(){
        Intent notificationIntent = new Intent(context, UserSettingsRecoverActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setTicker(("This is an example"))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("Recuperación de contraseña")
                .setContentText("Se te envio un código de seguridad a tu casilla de correo asociada a tu cuenta de Actas Digitales. Por favor ingresa y obten el código para recuperar tu contraseña");
        Notification n = builder.build();

        nm.notify(1, n);

    }
}
