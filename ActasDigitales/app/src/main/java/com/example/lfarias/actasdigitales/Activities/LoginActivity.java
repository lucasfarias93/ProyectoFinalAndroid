package com.example.lfarias.actasdigitales.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.lfarias.actasdigitales.AsyncTask.LoginUserAsynctask;
import com.example.lfarias.actasdigitales.AsyncTask.UserIdAsynctask;
import com.example.lfarias.actasdigitales.Cache.CacheService;
import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Helpers.Utils;
import com.example.lfarias.actasdigitales.R;
import com.example.lfarias.actasdigitales.Services.ServiceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via mEmail/password.
 */
public class LoginActivity extends AppCompatActivity implements LoginUserAsynctask.Callback, UserIdAsynctask.Callback {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private TextInputEditText mUserView;
    private TextInputEditText mPasswordView;
    private View mLoginFormView;
    private TextView mRegister, mForgotPassword;
    ProgressDialog dialog;
    private TextInputLayout mUserLayout;
    private TextInputLayout mPasswordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*ActionBar mActionBar = getSupportActionBar();

        mActionBar.setTitle("Inicio de sesión");
        mActionBar.setDisplayHomeAsUpEnabled(true);*/

        mUserLayout = (TextInputLayout)findViewById(R.id.user_input);
        mPasswordLayout = (TextInputLayout)findViewById(R.id.pass_input);

        dialog = Utils.createLoadingIndicator(LoginActivity.this);

        // Set up the login form.
        mUserView = (TextInputEditText) findViewById(R.id.email);
        mPasswordView = (TextInputEditText) findViewById(R.id.password);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mUserView.clearFocus();
        mPasswordView.clearFocus();

        Button mEmailSignInButton = (Button) findViewById(R.id.login_button);
        mEmailSignInButton.setElevation(20);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUserView == null || mUserView.getText().toString().isEmpty()) {
                    mUserLayout.setError("Este campo es obligatorio");
                    mUserView.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                  mUserLayout.setErrorTextAppearance(R.style.error_red);

                    if (mPasswordView == null || mPasswordView.getText().toString().isEmpty()) {
                        mPasswordLayout.setError("Este campo es obligatorio");
                        mPasswordView.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                      mPasswordLayout.setErrorTextAppearance(R.style.error_red);
                    }
                } else if (mPasswordView == null || mPasswordView.getText().toString().isEmpty()) {
                    mPasswordView.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                    mPasswordLayout.setError("Este campo es obligatorio");
                    mPasswordLayout.setErrorTextAppearance(R.style.error_red);
                } else {
                    LoginUserAsynctask asynctask = new LoginUserAsynctask(LoginActivity.this, LoginActivity.this, dialog);
                    List<String> params = new ArrayList<>();
                    params.add(mUserView.getText().toString());
                    params.add(mPasswordView.getText().toString());

                    ConnectionParams conectParams = new ConnectionParams();
                    conectParams.setmControllerId(ServiceUtils.Controllers.LOGIN_USER_CONTROLLER);
                    conectParams.setmActionId(ServiceUtils.Actions.LOGIN_USER);
                    conectParams.setmSearchType(ServiceUtils.SearchType.LOGIN_USER_SEARCH_TYPE);
                    conectParams.setParams(params);

                    dialog.show();
                    asynctask.execute(conectParams);
                }
            }
    });

        mLoginFormView = findViewById(R.id.login_form);

        mRegister = (TextView) findViewById(R.id.register);
        mRegister.setTextColor(getResources().getColor(R.color.white));
        mForgotPassword = (TextView) findViewById(R.id.forgotPassword);
        mForgotPassword.setTextColor(getResources().getColor(R.color.white));

        mForgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(LoginActivity.this, UserSettingsRecoverActivity.class);
                startActivity(i);
            }
        });

        mRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void loginUser(int success) {
        switch(success){
            case 0:
                dialog.dismiss();
                Utils.createGlobalDialog(this, "Error en el inicio de sesión", "Ocurrió un problema con los datos ingresados. El usuario no existe o los datos no son válidos").show();
                break;

            case 1:
                LoginUserAsynctask asynctask = new LoginUserAsynctask(LoginActivity.this, LoginActivity.this, dialog);
                List<String> params = new ArrayList<>();
                params.add(mUserView.getText().toString());
                params.add(mPasswordView.getText().toString());

                ConnectionParams conectParams = new ConnectionParams();
                conectParams.setmControllerId(ServiceUtils.Controllers.LOGIN_USER_CONTROLLER);
                conectParams.setmActionId(ServiceUtils.Actions.LOGIN_USER);
                conectParams.setmSearchType(ServiceUtils.SearchType.LOGIN_USER_SEARCH_TYPE);
                conectParams.setParams(params);

                asynctask.execute(conectParams);
                break;

            case 2:
                dialog.dismiss();
                CacheService.getInstance().clear();
                UserIdAsynctask asynctask1 = new UserIdAsynctask(LoginActivity.this, LoginActivity.this, dialog);

                ConnectionParams conectParams1 = new ConnectionParams();
                conectParams1.setmControllerId(ServiceUtils.Controllers.CIUDADANO_CONTROLLER + "/" + ServiceUtils.Controllers.COMMON_INDEX_METHOD);
                conectParams1.setmActionId(ServiceUtils.Actions.CIUDADANO_ID);
                conectParams1.setmSearchType(ServiceUtils.SearchType.USER_ID_SEARCH_TYPE);
                dialog.show();
                asynctask1.execute(conectParams1);

                Intent i = new Intent(LoginActivity.this, LandingPageActivity.class);
                startActivity(i);
                break;

            default:
                dialog.dismiss();
                Utils.createGlobalDialog(this, "Error en el inicio de sesión", "Ocurrió un problema con los datos ingresados. El usuario no existe o los datos no son válidos").show();
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void getUserId(Object success, Boolean isAdmin) {
        if(isAdmin){
            Intent i = new Intent(LoginActivity.this, LoginActivity.class);
            startActivity(i);
            Utils.createGlobalDialog(LoginActivity.this, "Error en el inicio de sesion", "Usted no tiene permisos para acceder a la aplicaciòn con los datos ingresados").show();
        } else {
            if(!success.toString().isEmpty()){
                int userId = Integer.parseInt(success.toString().replaceAll("\"", ""));
                CacheService.getInstance().setIdUser(userId);
            }
        }

    }
}

