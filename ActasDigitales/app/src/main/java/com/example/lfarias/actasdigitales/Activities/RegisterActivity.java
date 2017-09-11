package com.example.lfarias.actasdigitales.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lfarias.actasdigitales.AsyncTask.DatabaseReadObject;
import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
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

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatabaseReadObject.Callback{

    @Bind(R.id.dni) EditText mDni;
    @Bind(R.id.tramide_id) EditText mTramideId;
    @Bind(R.id.name) EditText mName;
    @Bind(R.id.user) EditText mUser;
    @Bind(R.id.password) EditText mPassword;
    @Bind(R.id.repeat_password) EditText mRepeatPassword;
    @Bind(R.id.email) EditText mEmail;
    @Bind(R.id.spinner_phone) Spinner mSpinner;
    @Bind(R.id.phone_number) EditText mPhoneNumber;
    @Bind(R.id.button_register) Button mButton;
    @Bind(R.id.descripcion1)TextView mDescription;
    @Bind(R.id.spinner_privincia) Spinner mProvince;
    @Bind(R.id.spinner_department) Spinner mDepartment;
    @Bind(R.id.address) EditText mAddress;
    @Bind(R.id.button_continue) Button mContinue;

    List<Provincia> provincias;
    ArrayAdapter<String> dataProvincesAdapter;
    ArrayAdapter<String> dataDepartmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mDni.clearFocus();
        mTramideId.clearFocus();

        mSpinner.setOnItemSelectedListener(this);
        mProvince.setOnItemSelectedListener(this);
        mDepartment.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<>();
        categories.add("Tipo de telefono");
        categories.add("Telefono Fijo");
        categories.add("Telefono Celular");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter);


        /*List<String> provinces = new ArrayList<>();
        provinces.add("Provincia");
        provinces.add("Mendoza");
        provinces.add("Jujuy");
        provinces.add("Buenos Aires");
        provinces.add("Salta");
        provinces.add("San Juan");
        provinces.add("San Luis");
        provinces.add("Cordoba");
        provinces.add("Misiones");*/

        ;

        /*List<String> departments = new ArrayList<>();
        departments.add("Departamento");
        departments.add("Godoy Cruz");
        departments.add("Las Heras");
        departments.add("Guaymallen");
        departments.add("Ciudad");
        departments.add("Tupungato");*/

        /* = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departments);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDepartment.setAdapter(dataDepartmentAdapter);*/

        mName.setVisibility(View.GONE);
        mUser.setVisibility(View.GONE);
        mPassword.setVisibility(View.GONE);
        mRepeatPassword.setVisibility(View.GONE);
        mEmail.setVisibility(View.GONE);
        mSpinner.setVisibility(View.GONE);
        mPhoneNumber.setVisibility(View.GONE);
        mDepartment.setVisibility(View.GONE);
        mProvince.setVisibility(View.GONE);
        mAddress.setVisibility(View.GONE);

        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DecodeTextUtils.decodeStringText("Secci\u00f3n");
                DatabaseReadObject userDataRetrieveAsynctask = new DatabaseReadObject(RegisterActivity.this, RegisterActivity.this);
                List<String> params = new ArrayList<>();
                params.add(mTramideId.getText().toString());
                params.add(mDni.getText().toString());

                ConnectionParams conectParams = new ConnectionParams();
                conectParams.setmControllerId(ServiceUtils.Controllers.TRAMITE_DNI_CONTROLLER);
                conectParams.setmActionId(ServiceUtils.Actions.BUSCAR_CIUDADANO);
                conectParams.setmSearchType(ServiceUtils.SearchType.CIUDADANO_SEARCH_TYPE);
                conectParams.setParams(params);
                userDataRetrieveAsynctask.execute(conectParams);
            }
        });
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
        if(object.length() != 0){
            try {
                nombre = (String)object.get("nombres");
                apellido = (String)object.get("apellido");

                DatabaseReadObject provincesDataRetrieveAsynctask = new DatabaseReadObject(RegisterActivity.this, RegisterActivity.this);
                List<String> params = new ArrayList<>();
                params.add("1");

                ConnectionParams conectParams = new ConnectionParams();
                conectParams.setmControllerId(ServiceUtils.Controllers.PROVINCIA_CONTROLLER);
                conectParams.setmActionId(ServiceUtils.Actions.BUSCAR_PROVINCIAS);
                conectParams.setmSearchType(ServiceUtils.SearchType.PROVINCIA_SEARCH_TYPE);
                conectParams.setParams(params);
                provincesDataRetrieveAsynctask.execute(conectParams);

            }catch(JSONException e){
                e.printStackTrace();
                Utils.createGlobalDialog(RegisterActivity.this, "Error en la obtención de datos","El DNI / Nro. Tramite del documento es inválido").show();
            }

            mContinue.setVisibility(View.GONE);
            mDescription.setVisibility(View.GONE);
            mName.setText(apellido + ", "+ nombre);
            mName.setVisibility(View.VISIBLE);
            mUser.setVisibility(View.VISIBLE);
            mTramideId.setVisibility(View.VISIBLE);
            mPassword.setVisibility(View.VISIBLE);
            mRepeatPassword.setVisibility(View.VISIBLE);
            mEmail.setVisibility(View.VISIBLE);
            mSpinner.setVisibility(View.VISIBLE);
            mPhoneNumber.setVisibility(View.VISIBLE);
            mDepartment.setVisibility(View.VISIBLE);
            mProvince.setVisibility(View.VISIBLE);
            mAddress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getProvinces(JSONObject object) {
        try {
            JSONArray array = object.getJSONObject("listProvincia").getJSONArray("items");
            provincias = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject privinciaJSONObject = array.getJSONObject(i);
                Provincia provincia = new Provincia();
                provincia.setId(Integer.parseInt(privinciaJSONObject.getString("id")));
                provincia.setNombreProvincia(privinciaJSONObject.getString("nombreprovincia"));
                provincias.add(provincia);
            }
        }catch (JSONException exc){
            Utils.createGlobalDialog(RegisterActivity.this, "Error en la obtención de datos","No se pudieron cargar datos de las provincias para el registro").show();
        }
        List<String> provinciasNombre = new ArrayList<>();
        for (Provincia provincia : provincias){
            provinciasNombre.add(provincia.getNombreProvincia());
        }

        dataProvincesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinciasNombre);
        dataProvincesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mProvince.setAdapter(dataProvincesAdapter);
    }
}

