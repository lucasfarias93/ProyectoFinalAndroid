package com.example.lfarias.actasdigitales.Activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lfarias.actasdigitales.R;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserSettingsRecoverActivity extends AppCompatActivity {

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

    Context context;
    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";

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
                createNotifications2();
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

            }
        });
    }

    public void createNotifications() {
        Intent notificationIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
        //notificationIntent.setClassName("com.google.android.gm", "com.google.android.gm.ConversationListActivity");
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                (R.drawable.info_black),
                "Ir a GMAIL",
                contentIntent).build();

        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Resources res = context.getResources();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setTicker(("This is an example"))
                .setWhen(System.currentTimeMillis())
                .addAction(action)
                .addAction(action)
                .addAction(action)
                .setAutoCancel(true)
                .setContentTitle("Recuperación de contraseña")
                .setContentText("Se te envio un código de seguridad a tu casilla de correo asociada a tu cuenta de Actas Digitales. Por favor ingresa y obten el código para recuperar tu contraseña");
        Notification n = builder.build();

        nm.notify(1, n);

    }

    public void createNotifications2() {
        int notificationId = 1; // just use a counter in some util class...
        PendingIntent gmailRedirectIntent = getGMAILRedirectIntent(notificationId, context);
        PendingIntent dismissNotifIntent = getDismissIntent(notificationId, context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setPriority(NotificationCompat.PRIORITY_MAX) //HIGH, MAX, FULL_SCREEN and setDefaults(Notification.DEFAULT_ALL) will make it a Heads Up Display Style
                .setDefaults(Notification.FLAG_AUTO_CANCEL) // also requires VIBRATE permission
                .setSmallIcon(R.mipmap.ic_launcher) // Required!
                .setContentTitle("Message from test")
                .setContentText("message")
                .setAutoCancel(true)
                .addAction(R.drawable.info_black, "Go to GMAIL", gmailRedirectIntent)
                .addAction(R.drawable.info_outline_black, "Dismiss", dismissNotifIntent);

// Gets an instance of the NotificationManager service
        NotificationManager notifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

// Builds the notification and issues it.
        notifyMgr.notify(notificationId, builder.build());
    }

    public PendingIntent getGMAILRedirectIntent(int notificationId, Context context) {
        Intent notificationIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationIntent.putExtra(NOTIFICATION_ID, notificationId);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,  0, notificationIntent, 0);
        return resultPendingIntent;
    }

    public static PendingIntent getDismissIntent(int notificationId, Context context) {
        Intent intent = new Intent(context, UserSettingsRecoverActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,  0, intent, 0);
        return resultPendingIntent;
    }


}

