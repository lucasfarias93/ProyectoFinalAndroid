package com.example.lfarias.actasdigitales.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lfarias.actasdigitales.AsyncTask.DatabaseReadObject;
import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Entities.Departamento;
import com.example.lfarias.actasdigitales.Entities.Localidad;
import com.example.lfarias.actasdigitales.Entities.Provincia;
import com.example.lfarias.actasdigitales.Helpers.DecodeTextUtils;
import com.example.lfarias.actasdigitales.Helpers.Utils;
import com.example.lfarias.actasdigitales.R;
import com.example.lfarias.actasdigitales.Services.ServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatabaseReadObject.Callback {

    @Bind(R.id.dni)
    EditText mDni;
    @Bind(R.id.tramide_id)
    EditText mTramideId;
    @Bind(R.id.name)
    EditText mName;
    @Bind(R.id.user)
    EditText mUser;
    @Bind(R.id.password)
    EditText mPassword;
    @Bind(R.id.repeat_password)
    EditText mRepeatPassword;
    @Bind(R.id.email)
    EditText mEmail;
    @Bind(R.id.spinner_phone)
    Spinner mSpinner;
    @Bind(R.id.phone_number)
    EditText mPhoneNumber;
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
    EditText mAddress;
    @Bind(R.id.button_continue)
    Button mContinue;
    @Bind(R.id.register_layout)
    LinearLayout layout;
    @Bind(R.id.button_report) Button mButtonReport;

    List<Provincia> provincias;
    List<Departamento> departamentos;
    List<Localidad> localidades;
    ArrayAdapter<String> dataProvincesAdapter;
    ArrayAdapter<String> dataDepartmentAdapter;
    ArrayAdapter<String> dataLocalidadAdapter;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mDni.clearFocus();
        mTramideId.clearFocus();

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
        mButtonReport.setVisibility(View.GONE);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mPassword.getText().toString();
                String repeatPassword = mRepeatPassword.getText().toString();
                String email = mEmail.getText().toString();

                if ((!password.isEmpty()) && (!repeatPassword.isEmpty()) && password.equals(repeatPassword) && Utils.emailValidator(email)) {
                    Intent i = new Intent(RegisterActivity.this, RequestActActivity.class);
                    startActivity(i);
                } else if(password.isEmpty()){
                    mPassword.setError("Este campo es obligatorio");
                    if(repeatPassword.isEmpty()){
                        mRepeatPassword.setError("Este campo es obligatorio");
                        if(email.isEmpty()){
                            mEmail.setError("Este campo es obligatorio");
                        } else if(!Utils.emailValidator(email)){
                            mEmail.setError("El formato del email ingresado no es válidio");
                        }
                    }
                } else if(repeatPassword.isEmpty()){
                    mRepeatPassword.setError("Este campo es obligatorio");
                    if(email.isEmpty()){
                        mEmail.setError("Este campo es obligatorio");
                    } else if(!Utils.emailValidator(email)){
                        mEmail.setError("El formato del email ingresado no es válido");
                    }
                } else if(email.isEmpty()){
                    mEmail.setError("Este campo es obligatorio");
                } else if(!Utils.emailValidator(email)){
                    mEmail.setError("El formato del email ingresado no es válido");
                }
                if(!(password.equals(repeatPassword))){
                    mPassword.setError("Las contraseñas ingresadas no coinciden");
                    if(email.isEmpty()){
                        mEmail.setError("Este campo es obligatorio");
                    } else if(!Utils.emailValidator(email)){
                        mEmail.setError("El formato del email ingresado no es válido");
                    }
                } else if(email.isEmpty()){
                    mEmail.setError("Este campo es obligatorio");
                } else if(!Utils.emailValidator(email)){
                    mEmail.setError("El formato del email ingresado no es válido");
                }
            }
        });

        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DecodeTextUtils.decodeStringText("Secci\u00f3n");
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
        if (object.length() != 0) {
            try {
                dialog.hide();
                mButton.setVisibility(View.VISIBLE);
                mButtonReport.setVisibility(View.VISIBLE);
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
}


