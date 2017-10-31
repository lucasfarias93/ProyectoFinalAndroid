package com.example.lfarias.actasdigitales.MercadoPago.MainExample;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lfarias.actasdigitales.Activities.LandingPageActivity;
import com.example.lfarias.actasdigitales.Activities.LoginActivity;
import com.example.lfarias.actasdigitales.Activities.MisSolicitudesActivity;
import com.example.lfarias.actasdigitales.Activities.RegisterActivity;
import com.example.lfarias.actasdigitales.Activities.UserSettingsRecoverActivity;
import com.example.lfarias.actasdigitales.AsyncTask.ChangePasswordAsynctask;
import com.example.lfarias.actasdigitales.AsyncTask.PayRequestAsynctask;
import com.example.lfarias.actasdigitales.Cache.CacheService;
import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Helpers.Utils;
import com.example.lfarias.actasdigitales.MercadoPago.ExampleUtils.ExampleUtils;
import com.example.lfarias.actasdigitales.R;
import com.example.lfarias.actasdigitales.Services.ServiceUtils;
import com.mercadopago.core.MercadoPagoCheckout;
import com.mercadopago.exceptions.MercadoPagoError;
import com.mercadopago.model.Payment;
import com.mercadopago.preferences.CheckoutPreference;
import com.mercadopago.preferences.DecorationPreference;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.LayoutUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.internal.Util;

/**
 * Created by lfarias on 10/3/17.
 */

public class CheckoutExampleActivity extends AppCompatActivity implements PayRequestAsynctask.Callback {


    private Activity mActivity;

    private View mRegularLayout;
    private String mPublicKey;

    private Integer mDefaultColor;
    private Integer mSelectedColor;
    private Button mpButton;
    private String mCheckoutPreferenceId;
    private ImageView imageView2;
    private String requestNumberId;
    ProgressDialog dialog1;

    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mp_activity_checkout_example);
        mActivity = this;
        mPublicKey = ExampleUtils.DUMMY_MERCHANT_PUBLIC_KEY;
        mCheckoutPreferenceId = ExampleUtils.DUMMY_PREFERENCE_ID;
        mDefaultColor = ContextCompat.getColor(this, R.color.colorPrimary);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(getIntent().getIntExtra(NOTIFICATION_ID, -1));

        /*ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("Pagar con MercadoPago");
        mActionBar.setDisplayHomeAsUpEnabled(true);*/

        mRegularLayout = findViewById(R.id.regularLayout);
        if (getIntent().getStringExtra("idSolicitud") != null) {
            requestNumberId = getIntent().getStringExtra("idSolicitud");
        }

        dialog1 = Utils.createLoadingIndicator(this);

    }

    public void onContinueClicked(View view) {
        startMercadoPagoCheckout();
    }

    private void startMercadoPagoCheckout() {
        new MercadoPagoCheckout.Builder()
                .setActivity(this)
                .setPublicKey(mPublicKey)
                .setCheckoutPreference(getCheckoutPreference())
                .startForPayment();
    }


    private CheckoutPreference getCheckoutPreference() {
        return new CheckoutPreference(mCheckoutPreferenceId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LayoutUtil.showRegularLayout(this);
        String paymentStatus = null;
        if (requestCode == MercadoPagoCheckout.CHECKOUT_REQUEST_CODE) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                Payment payment = JsonUtil.getInstance().fromJson(data.getStringExtra("payment"), Payment.class);
                paymentStatus = payment.getStatus();
            } else if (resultCode == RESULT_CANCELED) {
                if (data != null && data.getStringExtra("mercadoPagoError") != null) {
                    MercadoPagoError mercadoPagoError = JsonUtil.getInstance().fromJson(data.getStringExtra("mercadoPagoError"), MercadoPagoError.class);
                    Toast.makeText(mActivity, "Error: " + mercadoPagoError.getMessage(), Toast.LENGTH_SHORT).show();
                    paymentStatus = "rejected";
                } else {
                    Toast.makeText(mActivity, "Cancel", Toast.LENGTH_SHORT).show();
                    paymentStatus = "rejected";
                }
            }
        }
        createNotification(paymentStatus);
        createSnackBar(paymentStatus);
    }

    public void createNotification(String status) {
            int notificationId = new Random().nextInt(); // just use a counter in some util class...

            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custon_notification);
            contentView.setTextViewText(R.id.title, "Custom notification");
            contentView.setTextViewText(R.id.text, "This is a custom layout");
            String estado = null;
            if("approved".equals(status)){
                estado = "Pago Aprobado";
            }
            if("pending".equals(status)){
                estado = "Pago Pendiente de Aprobación";
            }
            if("rejected".equals(status)){
                estado = "Pago Rechazado";
            }
            NotificationCompat.Builder builder =
                    (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.success_1)
                            .setColor(ContextCompat.getColor(CheckoutExampleActivity.this, R.color.colorPrimary))
                            .setContentTitle("Actas Digitales")
                            .setContentText(estado)
                            .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
                            .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Se ha completado el pago de los códigos provinciales. En breve le enviaremos el acta firmada a su casilla de correo"));

        NotificationManager notifyMgr = (NotificationManager) CheckoutExampleActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);

        notifyMgr.notify(notificationId, builder.build());
    }

    @Override
    public void onResume() {
        super.onResume();
        showRegularLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_request_act, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                finish();
                CacheService.getInstance().clearUser1MockData();
                Intent i = new Intent(CheckoutExampleActivity.this, LoginActivity.class);
                startActivity(i);
                break;

            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void showRegularLayout() {
        mRegularLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void generate_pdf(Boolean success, Activity activity) {
        dialog1.dismiss();
        if (success) {
            if (activity instanceof LandingPageActivity) {
                Intent i = new Intent(CheckoutExampleActivity.this, LandingPageActivity.class);
                startActivity(i);
                CheckoutExampleActivity.this.finish();
            } else if (activity instanceof MisSolicitudesActivity) {
                Intent i = new Intent(CheckoutExampleActivity.this, MisSolicitudesActivity.class);
                startActivity(i);
                CheckoutExampleActivity.this.finish();
            }
        } else {
            final AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ContextThemeWrapper ctw = new ContextThemeWrapper(CheckoutExampleActivity.this, R.style.AppTheme_PopupOverlay);
                builder = new AlertDialog.Builder(ctw);
            } else {
                builder = new AlertDialog.Builder(CheckoutExampleActivity.this);
            }
            builder.setTitle("Error al firmar el acta")
                    .setMessage("Ocurrio un error al firmar el acta asociada a este pago. Por favor intente nuevamente mas tarde o contacte al soporte.")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setIcon(R.drawable.error_1)
                    .show();
        }
    }

    public void createSnackBar(final String status) {
        if ("pending".equals(status)) {
            final AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ContextThemeWrapper ctw = new ContextThemeWrapper(CheckoutExampleActivity.this, R.style.AppTheme_PopupOverlay);
                builder = new AlertDialog.Builder(ctw);
            } else {
                builder = new AlertDialog.Builder(CheckoutExampleActivity.this);
            }
            builder.setTitle("Pago pendiente de aprobación")
                    .setMessage("El pago esta siendo procesado y puede ser aprobado en los proximos días")
                    .setNegativeButton("MIS SOLICITUDES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            PayRequestAsynctask asynctask = new PayRequestAsynctask(CheckoutExampleActivity.this, CheckoutExampleActivity.this, new MisSolicitudesActivity());
                            List<String> params = new ArrayList<>();
                            params.add(status);
                            long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
                            params.add(String.valueOf(number));
                            params.add(String.valueOf(requestNumberId));

                            ConnectionParams conectParams = new ConnectionParams();
                            conectParams.setmControllerId(ServiceUtils.Controllers.CIUDADANO_CONTROLLER + "/" + ServiceUtils.Controllers.COMMON_INDEX_METHOD);
                            conectParams.setmActionId(ServiceUtils.Actions.PAGAR_SOLICITUD);
                            conectParams.setmSearchType(ServiceUtils.SearchType.PAGAR_SOLICITUD_SEARCH_TYPE);
                            conectParams.setParams(params);
                            dialog1.show();
                            asynctask.execute(conectParams);

                        }
                    })
                    .setPositiveButton("INICIO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            PayRequestAsynctask asynctask = new PayRequestAsynctask(CheckoutExampleActivity.this, CheckoutExampleActivity.this, new LandingPageActivity());
                            List<String> params = new ArrayList<>();
                            params.add(status);
                            long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
                            params.add(String.valueOf(number));
                            params.add(String.valueOf(requestNumberId));

                            ConnectionParams conectParams = new ConnectionParams();
                            conectParams.setmControllerId(ServiceUtils.Controllers.CIUDADANO_CONTROLLER + "/" + ServiceUtils.Controllers.COMMON_INDEX_METHOD);
                            conectParams.setmActionId(ServiceUtils.Actions.PAGAR_SOLICITUD);
                            conectParams.setmSearchType(ServiceUtils.SearchType.PAGAR_SOLICITUD_SEARCH_TYPE);
                            conectParams.setParams(params);
                            dialog1.show();
                            asynctask.execute(conectParams);
                        }
                    })
                    .setIcon(R.drawable.success_1)
                    .show();
        } else if ("approved".equals(status)) {
            final AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ContextThemeWrapper ctw = new ContextThemeWrapper(CheckoutExampleActivity.this, R.style.AppTheme_PopupOverlay);
                builder = new AlertDialog.Builder(ctw);
            } else {
                builder = new AlertDialog.Builder(CheckoutExampleActivity.this);
            }
            builder.setTitle("Pago confirmado")
                    .setMessage("El pago fue confirmado. Proximamente recibira en su mail su acta firmada.")
                    .setNegativeButton("MIS SOLICITUDES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            PayRequestAsynctask asynctask = new PayRequestAsynctask(CheckoutExampleActivity.this, CheckoutExampleActivity.this, new MisSolicitudesActivity());
                            List<String> params = new ArrayList<>();
                            params.add(status);
                            long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
                            params.add(String.valueOf(number));
                            params.add(String.valueOf(requestNumberId));

                            ConnectionParams conectParams = new ConnectionParams();
                            conectParams.setmControllerId(ServiceUtils.Controllers.CIUDADANO_CONTROLLER + "/" + ServiceUtils.Controllers.COMMON_INDEX_METHOD);
                            conectParams.setmActionId(ServiceUtils.Actions.PAGAR_SOLICITUD);
                            conectParams.setmSearchType(ServiceUtils.SearchType.PAGAR_SOLICITUD_SEARCH_TYPE);
                            conectParams.setParams(params);
                            dialog1.show();
                            asynctask.execute(conectParams);
                            createNotificationSuccess("Acta digital firmada");

                        }
                    })
                    .setPositiveButton("INICIO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            PayRequestAsynctask asynctask = new PayRequestAsynctask(CheckoutExampleActivity.this, CheckoutExampleActivity.this, new LandingPageActivity());
                            List<String> params = new ArrayList<>();
                            params.add(status);
                            long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
                            params.add(String.valueOf(number));
                            params.add(String.valueOf(requestNumberId));

                            ConnectionParams conectParams = new ConnectionParams();
                            conectParams.setmControllerId(ServiceUtils.Controllers.CIUDADANO_CONTROLLER + "/" + ServiceUtils.Controllers.COMMON_INDEX_METHOD);
                            conectParams.setmActionId(ServiceUtils.Actions.PAGAR_SOLICITUD);
                            conectParams.setmSearchType(ServiceUtils.SearchType.PAGAR_SOLICITUD_SEARCH_TYPE);
                            conectParams.setParams(params);
                            dialog1.show();
                            asynctask.execute(conectParams);
                            createNotificationSuccess("Acta digital firmada");
                        }
                    })
                    .setIcon(R.drawable.success_1)
                    .show();
        } else {
            final AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ContextThemeWrapper ctw = new ContextThemeWrapper(CheckoutExampleActivity.this, R.style.AppTheme_PopupOverlay);
                builder = new AlertDialog.Builder(ctw);
            } else {
                builder = new AlertDialog.Builder(CheckoutExampleActivity.this);
            }
            builder.setTitle("Pago rechazado")
                    .setMessage("El pago fue rechazado. Intente nuevamente más tarde")
                    .setNegativeButton("MIS SOLICITUDES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent i = new Intent(CheckoutExampleActivity.this, MisSolicitudesActivity.class);
                            startActivity(i);
                            finish();
                        }
                    })
                    .setPositiveButton("INICIO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent i = new Intent(CheckoutExampleActivity.this, LandingPageActivity.class);
                            startActivity(i);
                            finish();
                        }
                    })
                    .setIcon(R.drawable.error_1)
                    .show();
        }
    }
    public void createNotificationSuccess(String status) {
        int notificationId = new Random().nextInt(); // just use a counter in some util class...
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custon_notification);
        contentView.setTextViewText(R.id.title, "Custom notification");
        contentView.setTextViewText(R.id.text, "This is a custom layout");

        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.success_1)
                        .setColor(ContextCompat.getColor(CheckoutExampleActivity.this, R.color.colorPrimary))
                        .setContentTitle("Actas Digitales")
                        .setContentText(status)
                        .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Le hemos enviado el acta en formato digital firmada a su casilla de correos. Gracias por usar Actas Digitales!"));

        NotificationManager notifyMgr = (NotificationManager) CheckoutExampleActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);

        notifyMgr.notify(notificationId, builder.build());
    }
}
