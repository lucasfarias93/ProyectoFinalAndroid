package com.example.lfarias.actasdigitales.Activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lfarias.actasdigitales.AsyncTask.DatabaseReadObject;
import com.example.lfarias.actasdigitales.AsyncTask.RegisterUserAsynctask;
import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Entities.Departamento;
import com.example.lfarias.actasdigitales.Entities.Localidad;
import com.example.lfarias.actasdigitales.Entities.Provincia;
import com.example.lfarias.actasdigitales.Helpers.Utils;
import com.example.lfarias.actasdigitales.R;
import com.example.lfarias.actasdigitales.Services.ServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Creado por Lucas.Farias
 * <p>
 * Archivo creado: 9 de Septiembre de 2017
 * <p>
 * Descripción: Activity principal en la que se incluyo toda la lógica del registro del usuario en la aplicación.
 * Se manejaron los eventos de transición, la comunicación con los procesos de llamada al web service,
 * el manejo de errores tanto unitarios como de la creación de pop-ups o alertas de error.
 */

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatabaseReadObject.Callback, RegisterUserAsynctask.Callback {

    @Bind(R.id.dni)
    TextInputEditText mDni;
    @Bind(R.id.tramide_id)
    TextInputEditText mTramideId;
    @Bind(R.id.name)
    EditText mName;
    @Bind(R.id.user)
    TextInputEditText mUser;
    @Bind(R.id.password)
    TextInputEditText mPassword;
    @Bind(R.id.repeat_password)
    TextInputEditText mRepeatPassword;
    @Bind(R.id.email)
    TextInputEditText mEmail;
    @Bind(R.id.spinner_phone)
    Spinner mSpinner;
    @Bind(R.id.phone_number)
    TextInputEditText mPhoneNumber;
    @Bind(R.id.button_register)
    Button mButton;
    @Bind(R.id.last_name)
    EditText mLast_name;
    @Bind(R.id.descripcion1)
    TextView mDescription;
    @Bind(R.id.spinner_privincia)
    Spinner mProvince;
    @Bind(R.id.spinner_department)
    Spinner mDepartment;
    @Bind(R.id.spinner_localidad)
    Spinner mLocalidad;
    @Bind(R.id.address)
    TextInputEditText mAddress;
    @Bind(R.id.button_continue)
    Button mContinue;
    @Bind(R.id.register_layout)
    LinearLayout layout;
    @Bind(R.id.first_dni)
    ImageView dni1;
    @Bind(R.id.second_dni)
    ImageView dni2;
    @Bind(R.id.terminos)
    TextView mTermAndConditions;
    @Bind(R.id.descripcion2)
    TextView mDescripcion2;
    @Bind(R.id.checkbox)
    CheckBox mCheckbox;
    @Bind(R.id.image_contact)
    ImageView mContact;
    @Bind(R.id.image_password)
    ImageView mPassword1;
    @Bind(R.id.image_password2)
    ImageView mPassword2;
    @Bind(R.id.image_email)
    ImageView mEmail2;
    @Bind(R.id.image_telefono)
    ImageView mTelefono;
    @Bind(R.id.image_domicilio)
    ImageView mDomicilio;

    @Bind(R.id.dni_layout)
    TextInputLayout dniLayout1;
    @Bind(R.id.tramite_layout)
    TextInputLayout tramiteLayout;
    @Bind(R.id.user_layout)
    TextInputLayout userLayout;
    @Bind(R.id.password_layout)
    TextInputLayout passwordLayout;
    @Bind(R.id.repeat_password_layout)
    TextInputLayout repeatLayout;
    @Bind(R.id.email_layout)
    TextInputLayout emailLayout;
    @Bind(R.id.number_phone) TextInputLayout phoneLayout;

    List<Provincia> provincias;
    List<Departamento> departamentos;
    List<Localidad> localidades;
    ArrayAdapter<String> dataProvincesAdapter;
    ArrayAdapter<String> dataDepartmentAdapter;
    ArrayAdapter<String> dataLocalidadAdapter;
    ProgressDialog dialog;

    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /*ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("Registro de usuario");
        mActionBar.setDisplayHomeAsUpEnabled(true);*/
        ButterKnife.bind(this);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mDni.clearFocus();
        mTramideId.clearFocus();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(getIntent().getIntExtra(NOTIFICATION_ID, -1));

        dialog = Utils.createLoadingIndicator(RegisterActivity.this);

        List<String> categories = new ArrayList<>();
        categories.add("Tipo de telefono");
        categories.add("Telefono Fijo");
        categories.add("Telefono Celular");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter);

        mName.setVisibility(View.GONE);
        mUser.setVisibility(View.GONE);
        mPassword.setVisibility(View.GONE);
        mRepeatPassword.setVisibility(View.GONE);
        mEmail.setVisibility(View.GONE);
        mSpinner.setVisibility(View.GONE);
        mLast_name.setVisibility(View.GONE);
        mPhoneNumber.setVisibility(View.GONE);
        mDepartment.setVisibility(View.GONE);
        mProvince.setVisibility(View.GONE);
        mLocalidad.setVisibility(View.GONE);
        mAddress.setVisibility(View.GONE);
        mButton.setVisibility(View.GONE);
        mTermAndConditions.setVisibility(View.GONE);
        mCheckbox.setVisibility(View.GONE);
        mContact.setVisibility(View.GONE);
        mPassword1.setVisibility(View.GONE);
        mPassword2.setVisibility(View.GONE);
        mEmail2.setVisibility(View.GONE);
        mTelefono.setVisibility(View.GONE);
        mDomicilio.setVisibility(View.GONE);

        mCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckbox.isChecked()) {
                    mCheckbox.setError(null);
                }
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String user = mUser.getText().toString();
                String password = mPassword.getText().toString();
                String repeatPassword = mRepeatPassword.getText().toString();
                String email = mEmail.getText().toString();

                if (user.isEmpty()) {
                    userLayout.setError("Este campo es obligatorio");
                    mUser.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                    userLayout.setErrorTextAppearance(R.style.error_red);
                }

                if ((!password.isEmpty()) && (!repeatPassword.isEmpty()) && password.equals(repeatPassword) && Utils.emailValidator(email) && mCheckbox.isChecked()) {

                    DatabaseReadObject userDataRetrieveAsynctask = new DatabaseReadObject(RegisterActivity.this, RegisterActivity.this, dialog);
                    List<String> params = new ArrayList<>();
                    params.add(mUser.getText().toString());
                    params.add(mTramideId.getText().toString());
                    params.add(mDni.getText().toString());
                    params.add(mPassword.getText().toString());
                    params.add(mRepeatPassword.getText().toString());
                    params.add(mName.getText().toString());
                    params.add(mLast_name.getText().toString());
                    params.add(mEmail.getText().toString());

                    ConnectionParams conectParams = new ConnectionParams();
                    conectParams.setmControllerId(ServiceUtils.Controllers.REGISTER_USER_CONTROLLER);
                    conectParams.setmActionId(ServiceUtils.Actions.REGISTER_USER);
                    conectParams.setmSearchType(ServiceUtils.SearchType.REGISTER_USER_SEARCH_TYPE);
                    conectParams.setParams(params);
                    dialog.show();
                    userDataRetrieveAsynctask.execute(conectParams);

                } else if (password.isEmpty()) {
                    passwordLayout.setError("Este campo es obligatorio");
                    mPassword.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                    passwordLayout.setErrorTextAppearance(R.style.error_red);
                    if (repeatPassword.isEmpty()) {
                        repeatLayout.setError("Este campo es obligatorio");
                        mRepeatPassword.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                        repeatLayout.setErrorTextAppearance(R.style.error_red);
                        if (email.isEmpty()) {
                            emailLayout.setError("Este campo es obligatorio");
                            mEmail.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                            emailLayout.setErrorTextAppearance(R.style.error_red);
                        } else if (!Utils.emailValidator(email)) {
                            emailLayout.setError("El formato del email ingresado no es válidio");
                            mEmail.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                            emailLayout.setErrorTextAppearance(R.style.error_red);
                        }
                    }
                } else if (repeatPassword.isEmpty()) {
                    repeatLayout.setError("Este campo es obligatorio");
                    mRepeatPassword.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                    repeatLayout.setErrorTextAppearance(R.style.error_red);
                    if (email.isEmpty()) {
                        emailLayout.setError("Este campo es obligatorio");
                        mEmail.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                        emailLayout.setErrorTextAppearance(R.style.error_red);
                    } else if (!Utils.emailValidator(email)) {
                        emailLayout.setError("El formato del email ingresado no es válidio");
                        mEmail.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                        emailLayout.setErrorTextAppearance(R.style.error_red);
                    }
                } else if (email.isEmpty()) {
                    emailLayout.setError("Este campo es obligatorio");
                    mEmail.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                    emailLayout.setErrorTextAppearance(R.style.error_red);
                } else if (!Utils.emailValidator(email)) {
                    emailLayout.setError("El formato del email ingresado no es válidio");
                    mEmail.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                    emailLayout.setErrorTextAppearance(R.style.error_red);
                }
                if (!(password.equals(repeatPassword))) {
                    passwordLayout.setError("Las contraseñas ingresadas no coinciden");
                    mPassword.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                    passwordLayout.setErrorTextAppearance(R.style.error_red);
                    if (email.isEmpty()) {
                        emailLayout.setError("Este campo es obligatorio");
                        mEmail.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                        emailLayout.setErrorTextAppearance(R.style.error_red);
                    } else if (!Utils.emailValidator(email)) {
                        emailLayout.setError("El formato del email ingresado no es válidio");
                        mEmail.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                        emailLayout.setErrorTextAppearance(R.style.error_red);
                    }
                } else if (email.isEmpty()) {
                    emailLayout.setError("Este campo es obligatorio");
                    mEmail.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                    emailLayout.setErrorTextAppearance(R.style.error_red);
                } else if (!Utils.emailValidator(email)) {
                    emailLayout.setError("El formato del email ingresado no es válidio");
                    mEmail.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                    emailLayout.setErrorTextAppearance(R.style.error_red);
                }
                if (!mCheckbox.isChecked()) {
                    mCheckbox.setError("Debe aceptar los terminos y condiciones para poder registrarse");
                }
                if ("Tipo de telefono".equals(mSpinner.getSelectedItem())  || mPhoneNumber.getText().toString().isEmpty()){
                    phoneLayout.setError("Debe seleccionar un tipo y numero de telefono");
                    mPhoneNumber.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                    phoneLayout.setErrorTextAppearance(R.style.error_red);
                }
            }
        });

        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dniLayout1.setError(null);
                tramiteLayout.setError(null);
                if (mDni.getText().toString().isEmpty()) {
                    dniLayout1.setError("Este campo es obligatorio");
                    mDni.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                    dniLayout1.setErrorTextAppearance(R.style.error_red);
                    if (mTramideId.getText().toString().isEmpty()) {
                        tramiteLayout.setError("Este campo es obligatorio");
                        mTramideId.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                        tramiteLayout.setErrorTextAppearance(R.style.error_red);
                    }
                } else if (mTramideId.getText().toString().isEmpty()) {
                    tramiteLayout.setError("Este campo es obligatorio");
                    mTramideId.getBackground().setColorFilter(getResources().getColor(R.color.color_error2), PorterDuff.Mode.SRC_ATOP);
                    tramiteLayout.setErrorTextAppearance(R.style.error_red);
                } else {

                    DatabaseReadObject userDataRetrieveAsynctask = new DatabaseReadObject(RegisterActivity.this, RegisterActivity.this, dialog);
                    List<String> params = new ArrayList<>();
                    params.add(mTramideId.getText().toString());
                    params.add(mDni.getText().toString());

                    ConnectionParams conectParams = new ConnectionParams();
                    conectParams.setmControllerId(ServiceUtils.Controllers.TRAMITE_DNI_CONTROLLER);
                    conectParams.setmActionId(ServiceUtils.Actions.BUSCAR_CIUDADANO);
                    conectParams.setmSearchType(ServiceUtils.SearchType.CIUDADANO_SEARCH_TYPE);
                    conectParams.setParams(params);
                    dialog.show();
                    userDataRetrieveAsynctask.execute(conectParams);
                }
            }
        });

        mProvince.setSelection(0);
        mProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int idProvincia;
                if (mProvince.getSelectedItem().equals("Seleccione Provincia")) {
                } else {
                    DatabaseReadObject departmentDataRetrieveAsynctask = new DatabaseReadObject(RegisterActivity.this, RegisterActivity.this, dialog);
                    List<String> params = new ArrayList<>();
                    for (int i = 0; i < provincias.size(); i++) {
                        if (mProvince.getSelectedItem().toString().equals(provincias.get(i).getNombreProvincia())) {
                            idProvincia = provincias.get(i).getId();
                            params.add(String.valueOf(idProvincia));
                        }
                    }

                    ConnectionParams conectParams = new ConnectionParams();
                    conectParams.setmControllerId(ServiceUtils.Controllers.DEPARTAMENTO_CONTROLLER);
                    conectParams.setmActionId(ServiceUtils.Actions.BUSCAR_DEPARTAMENTO_SEGUN_PROVINCIA);
                    conectParams.setmSearchType(ServiceUtils.SearchType.DEPARTAMENTO_SEARCH_TYPE);
                    conectParams.setParams(params);
                    dialog.show();
                    departmentDataRetrieveAsynctask.execute(conectParams);
                    mDepartment.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mDepartment.setSelection(0);
        mDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int idDepartamento;
                if (mDepartment.getSelectedItem().equals("Seleccione Departamento")) {
                } else {
                    DatabaseReadObject localidadDataRetrieveAsynctask = new DatabaseReadObject(RegisterActivity.this, RegisterActivity.this, dialog);
                    List<String> params = new ArrayList<>();
                    for (int i = 0; i < departamentos.size(); i++) {
                        if (mDepartment.getSelectedItem().toString().equals(departamentos.get(i).getNombreDepartamento())) {
                            idDepartamento = departamentos.get(i).getId();
                            params.add(String.valueOf(idDepartamento));
                        }
                    }

                    ConnectionParams conectParams = new ConnectionParams();
                    conectParams.setmControllerId(ServiceUtils.Controllers.LOCALIDAD_CONTROLLER);
                    conectParams.setmActionId(ServiceUtils.Actions.BUSCAR_LOCALIDAD_SEGUN_DEPARTAMENTO);
                    conectParams.setmSearchType(ServiceUtils.SearchType.LOCALIDAD_SEARCH_TYPE);
                    conectParams.setParams(params);
                    dialog.show();
                    localidadDataRetrieveAsynctask.execute(conectParams);
                    mLocalidad.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mLocalidad.setSelection(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void getUserData(JSONObject object) {
        String nombre = "";
        String apellido = "";
        mDni.setEnabled(false);
        mDni.setClickable(false);
        mTramideId.setEnabled(false);
        mTramideId.setClickable(false);
        mName.setEnabled(false);
        mName.setClickable(false);
        mLast_name.setEnabled(false);
        mLast_name.setClickable(false);
        mUser.setFocusable(true);
        if (object.length() != 0) {
            try {
                dialog.hide();
                dni1.setVisibility(View.GONE);
                dni2.setVisibility(View.GONE);
                mButton.setVisibility(View.VISIBLE);
                mDescripcion2.setVisibility(View.GONE);
                mTermAndConditions.setVisibility(View.VISIBLE);
                mCheckbox.setVisibility(View.VISIBLE);
                mContact.setVisibility(View.VISIBLE);
                mPassword1.setVisibility(View.VISIBLE);
                mPassword2.setVisibility(View.VISIBLE);
                mEmail2.setVisibility(View.VISIBLE);
                mTelefono.setVisibility(View.VISIBLE);
                mDomicilio.setVisibility(View.VISIBLE);

                mTermAndConditions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(RegisterActivity.this, TermsAndCondsActivity.class);
                        startActivity(i);
                    }
                });

                nombre = (String) object.get("nombres");
                apellido = (String) object.get("apellido");

                DatabaseReadObject provincesDataRetrieveAsynctask = new DatabaseReadObject(RegisterActivity.this, RegisterActivity.this, dialog);
                List<String> params = new ArrayList<>();
                params.add("1");

                ConnectionParams conectParams = new ConnectionParams();
                conectParams.setmControllerId(ServiceUtils.Controllers.PROVINCIA_CONTROLLER);
                conectParams.setmActionId(ServiceUtils.Actions.BUSCAR_PROVINCIAS);
                conectParams.setmSearchType(ServiceUtils.SearchType.PROVINCIA_SEARCH_TYPE);
                conectParams.setParams(params);
                dialog.show();
                provincesDataRetrieveAsynctask.execute(conectParams);

            } catch (JSONException e) {
                e.printStackTrace();
                dialog.dismiss();
                Utils.createGlobalDialog(RegisterActivity.this, "Error en la obtención de datos", "El DNI / Nro. Tramite del documento es inválido").show();
            }

            mContinue.setVisibility(View.GONE);
            mDescription.setVisibility(View.GONE);
            mName.setText(nombre);
            mLast_name.setText(apellido);
            mLast_name.setVisibility(View.VISIBLE);
            mName.setVisibility(View.VISIBLE);
            mUser.setVisibility(View.VISIBLE);
            mTramideId.setVisibility(View.VISIBLE);
            mPassword.setVisibility(View.VISIBLE);
            mRepeatPassword.setVisibility(View.VISIBLE);
            mEmail.setVisibility(View.VISIBLE);
            mSpinner.setVisibility(View.VISIBLE);
            mPhoneNumber.setVisibility(View.VISIBLE);
            mAddress.setVisibility(View.VISIBLE);
            mProvince.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getProvinces(JSONObject object) {
        try {
            JSONArray array = object.getJSONObject("listProvincia").getJSONArray("items");
            provincias = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                dialog.hide();
                JSONObject privinciaJSONObject = array.getJSONObject(i);
                Provincia provincia = new Provincia();
                provincia.setId(Integer.parseInt(privinciaJSONObject.getString("id")));
                provincia.setNombreProvincia(privinciaJSONObject.getString("nombreprovincia"));
                provincias.add(provincia);
            }
        } catch (JSONException exc) {
            dialog.dismiss();
            Utils.createGlobalDialog(RegisterActivity.this, "Error en la obtención de datos", "No se pudieron cargar datos de las provincias para el registro").show();
        }
        List<String> provinciasNombre = new ArrayList<>();
        provinciasNombre.add("Seleccione Provincia");
        for (Provincia provincia : provincias) {
            provinciasNombre.add(provincia.getNombreProvincia());
        }

        dataProvincesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinciasNombre);
        dataProvincesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mProvince.setAdapter(dataProvincesAdapter);
    }

    @Override
    public void getDepartmentByProvince(JSONObject object) {
        try {
            JSONArray array = object.getJSONArray("items");
            departamentos = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                dialog.dismiss();
                JSONObject departamentoJSONObject = array.getJSONObject(i);
                Departamento departamento = new Departamento();
                departamento.setId(Integer.parseInt(departamentoJSONObject.getString("id")));
                departamento.setNombreDepartamento(departamentoJSONObject.getString("nombredepartamento"));
                departamento.setIdProvincia(Integer.parseInt(departamentoJSONObject.getString("idprovincia")));
                departamentos.add(departamento);
            }
        } catch (JSONException exc) {
            dialog.dismiss();
            Utils.createGlobalDialog(RegisterActivity.this, "Error en la obtención de datos", "No se pudieron cargar datos de los departamentos para el registro").show();
        }
        List<String> departamentosNombre = new ArrayList<>();
        departamentosNombre.add("Seleccione Departamento");
        for (Departamento departamento : departamentos) {
            departamentosNombre.add(departamento.getNombreDepartamento());
        }

        dataDepartmentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departamentosNombre);
        dataDepartmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDepartment.setAdapter(dataDepartmentAdapter);

        List<String> localidadesNombre = new ArrayList<>();
        localidadesNombre.add("Seleccione Localidad");

        dataLocalidadAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, localidadesNombre);
        dataLocalidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLocalidad.setAdapter(dataLocalidadAdapter);
    }

    @Override
    public void getLocalidadByDepartment(JSONObject object) {
        try {
            JSONArray array = object.getJSONArray("items");
            localidades = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                dialog.dismiss();
                JSONObject localidadJSONObject = array.getJSONObject(i);
                Localidad localidad = new Localidad();
                localidad.setId(Integer.parseInt(localidadJSONObject.getString("id")));
                localidad.setNombreLocalidad(localidadJSONObject.getString("nombrelocalidad"));
                localidad.setIdDepartamento(Integer.parseInt(localidadJSONObject.getString("iddepartamento")));
                localidades.add(localidad);
            }
        } catch (JSONException exc) {
            dialog.dismiss();
            Utils.createGlobalDialog(RegisterActivity.this, "Error en la obtención de datos", "No se pudieron cargar datos de las localidades para el registro").show();
        }
        List<String> localidadesNombre = new ArrayList<>();
        localidadesNombre.add("Seleccione Localidad");
        for (Localidad localidad : localidades) {
            localidadesNombre.add(localidad.getNombreLocalidad());
        }

        dataLocalidadAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, localidadesNombre);
        dataLocalidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLocalidad.setAdapter(dataLocalidadAdapter);
    }

    @Override
    public void registerUser(Boolean success, String response) {
        if (success) {
            dialog.dismiss();
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().findViewById(R.id.register_layout), "Usuario registrado exitosamente", Snackbar.LENGTH_LONG)
                    .setAction("IR AL LOGIN", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                    });
            // Changing message text color
            snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));

            // Changing action button text color
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(RegisterActivity.this, R.color.black));
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
            createNotifications();
        } else {
            dialog.dismiss();
            Utils.createGlobalDialog(RegisterActivity.this, "Error en la creación del nuevo usuario", response).show();
        }
    }

    public void createNotifications() {
        int notificationId = new Random().nextInt(); // just use a counter in some util class...
        PendingIntent dismissIntent = RegisterActivity.getDismissIntent(notificationId, RegisterActivity.this);

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custon_notification);
        contentView.setTextViewText(R.id.title, "Custom notification");
        contentView.setTextViewText(R.id.text, "This is a custom layout");

        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.success)
                        .setColor(ContextCompat.getColor(RegisterActivity.this, R.color.colorPrimary))
                        .setContentTitle("Creación de usuario")
                        .setContentText("Se le ha enviado un email a su casilla de correo con..")
                        .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Se le ha enviado un email a su casilla de correo con el nuevo usuario y contraseña con el que podra ingresar al sistema. Por favor revise su casilla de correo"))
                        .addAction(R.drawable.clear,
                                "Cerrar notificacion", dismissIntent);

        NotificationManager notifyMgr = (NotificationManager) RegisterActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);

        notifyMgr.notify(notificationId, builder.build());
    }

    public static PendingIntent getDismissIntent(int notificationId, Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        PendingIntent dismissIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return dismissIntent;
    }

    @Override
    public boolean onSupportNavigateUp() {
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
}


