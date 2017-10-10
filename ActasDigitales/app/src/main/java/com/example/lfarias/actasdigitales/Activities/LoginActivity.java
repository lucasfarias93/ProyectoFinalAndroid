package com.example.lfarias.actasdigitales.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lfarias.actasdigitales.AsyncTask.DatabaseReadObject;
import com.example.lfarias.actasdigitales.AsyncTask.LoginUserAsynctask;
import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Entities.Usuarios;
import com.example.lfarias.actasdigitales.Helpers.SQLiteDatabaseHelper;
import com.example.lfarias.actasdigitales.Helpers.Utils;
import com.example.lfarias.actasdigitales.R;
import com.example.lfarias.actasdigitales.Services.ServiceUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via mEmail/password.
 */
public class LoginActivity extends AppCompatActivity implements LoginUserAsynctask.Callback {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView mUserView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private TextView mRegister, mForgotPassword;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("Inicio de sesión");
        mActionBar.setDisplayHomeAsUpEnabled(true);

        dialog = Utils.createLoadingIndicator(LoginActivity.this);

        // Set up the login form.
        mUserView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mUserView.clearFocus();
        mPasswordView.clearFocus();

        Button mEmailSignInButton = (Button) findViewById(R.id.login_button);
        mEmailSignInButton.setElevation(20);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUserView == null || mUserView.getText().toString().isEmpty()) {
                    mUserView.setError("Este campo es obligatorio");
                    if (mPasswordView == null || mPasswordView.getText().toString().isEmpty()) {
                        mPasswordView.setError("Este campo es obligatorio");
                    }
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
        mRegister.setTextColor(getResources().getColor(R.color.colorPrimary));
        mForgotPassword = (TextView) findViewById(R.id.forgotPassword);
        mForgotPassword.setTextColor(getResources().getColor(R.color.colorPrimary));

        mForgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, UserSettingsRecoverActivity.class);
                startActivity(i);
            }
        });

        mRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
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
    public void loginUser(Boolean success) {
        if(success){
            dialog.hide();
            Intent i = new Intent(LoginActivity.this, LandingPageActivity.class);
            startActivity(i);
        } else {
            dialog.hide();
            Utils.createGlobalDialog(this, "Error en el inicio de sesión", "Ocurrió un problema con los datos ingresados. El usuario no existe o los datos no son válidos").show();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        return true;
    }
}

