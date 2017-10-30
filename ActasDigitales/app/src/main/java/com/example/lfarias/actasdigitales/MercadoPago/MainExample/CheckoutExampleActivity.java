package com.example.lfarias.actasdigitales.MercadoPago.MainExample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mp_activity_checkout_example);
        mActivity = this;
        mPublicKey = ExampleUtils.DUMMY_MERCHANT_PUBLIC_KEY;
        mCheckoutPreferenceId = ExampleUtils.DUMMY_PREFERENCE_ID;
        mDefaultColor = ContextCompat.getColor(this, R.color.colorPrimary);

        /*ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("Pagar con MercadoPago");
        mActionBar.setDisplayHomeAsUpEnabled(true);*/

        mRegularLayout = findViewById(R.id.regularLayout);
        requestNumberId = getIntent().getStringExtra("idSolicitud");

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
        createSnackBar(paymentStatus);
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
    public void generate_pdf(Boolean success) {

    }

    public void createSnackBar(final String status) {
        if("pending".equals(status)){
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
                    .setIcon(R.drawable.success_1)
                    .show();
        } else if("approved".equals(status)){
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
                            PayRequestAsynctask asynctask = new PayRequestAsynctask(CheckoutExampleActivity.this, CheckoutExampleActivity.this);
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

                            asynctask.execute(conectParams);

                            Intent i = new Intent(CheckoutExampleActivity.this, MisSolicitudesActivity.class);
                            startActivity(i);
                            finish();
                        }
                    })
                    .setPositiveButton("INICIO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            PayRequestAsynctask asynctask = new PayRequestAsynctask(CheckoutExampleActivity.this, CheckoutExampleActivity.this);
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

                            asynctask.execute(conectParams);
                            Intent i = new Intent(CheckoutExampleActivity.this, LandingPageActivity.class);
                            startActivity(i);
                            finish();
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

        /*InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        Snackbar snackbar = Snackbar
                .make(getWindow().getDecorView().findViewById(R.id.register_layout), "El pago ha sido realizado correctamente. En breve le mandaremos el acta solicitada", Snackbar.LENGTH_LONG)
                .setAction("IR AL INICIO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckoutExampleActivity.this.finish();
                        Intent i = new Intent(CheckoutExampleActivity.this, LandingPageActivity.class);
                        startActivity(i);
                    }
                })
                .setAction("MIS SOLICITUDES", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckoutExampleActivity.this.finish();
                        Intent i = new Intent(CheckoutExampleActivity.this, MisSolicitudesActivity.class);
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
        snackbar.show();*/
    }
}
