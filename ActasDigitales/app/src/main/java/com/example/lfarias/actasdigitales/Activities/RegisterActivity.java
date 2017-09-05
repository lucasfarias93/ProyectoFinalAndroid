package com.example.lfarias.actasdigitales.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lfarias.actasdigitales.AsyncTask.DatabaseReadObject;
import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Helpers.Utils;
import com.example.lfarias.actasdigitales.R;
import com.example.lfarias.actasdigitales.Services.ServiceUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
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

        List<String> provinces = new ArrayList<>();
        provinces.add("Provincia");
        provinces.add("Mendoza");
        provinces.add("Jujuy");
        provinces.add("Buenos Aires");
        provinces.add("Salta");
        provinces.add("San Juan");
        provinces.add("San Luis");
        provinces.add("Cordoba");
        provinces.add("Misiones");

        ArrayAdapter<String> dataProvincesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinces);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mProvince.setAdapter(dataProvincesAdapter);

        List<String> departments = new ArrayList<>();
        departments.add("Departamento");
        departments.add("Godoy Cruz");
        departments.add("Las Heras");
        departments.add("Guaymallen");
        departments.add("Ciudad");
        departments.add("Tupungato");

        ArrayAdapter<String> dataDepartmentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departments);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDepartment.setAdapter(dataDepartmentAdapter);

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

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReadObject readObjectAsyncTask = new DatabaseReadObject(RegisterActivity.this, RegisterActivity.this);
                List<String> params = new ArrayList<>();
                params.add(mTramideId.getText().toString());
                params.add(mDni.getText().toString());

                ConnectionParams conectParams = new ConnectionParams();
                conectParams.setController_id(ServiceUtils.Controllers.TRAMITE_DNI_CONTROLLER);
                conectParams.setAction_id(ServiceUtils.Actions.BUSCAR_CIUDADANO);
                conectParams.setParams(params);
                readObjectAsyncTask.execute(conectParams);
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
    public void getResultSet(JSONObject object) {
        String nombre = "";
        String apellido = "";
        if(object.length() != 0){
            try {
                nombre = (String)object.get("nombres");
                apellido = (String)object.get("apellido");
            }catch(JSONException e){
                e.printStackTrace();
            }
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
        } else {
            Utils.createGlobalDialog(RegisterActivity.this, "Error en la obtención de datos","El DNI / Nro. Tramite del documento es inválido").show();
        }
    }
}

