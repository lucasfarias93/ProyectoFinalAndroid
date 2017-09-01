package com.example.lfarias.actasdigitales.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lfarias.actasdigitales.Helpers.Utils;
import com.example.lfarias.actasdigitales.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    @Bind(R.id.dni) EditText mDni;
    @Bind(R.id.tramide_id) EditText mTramideId;
    @Bind(R.id.name) EditText mName;
    @Bind(R.id.user) EditText mUser;
    @Bind(R.id.password) EditText mPassword;
    @Bind(R.id.repeat_password) EditText mRepeatPassword;
    @Bind(R.id.email) EditText mEmail;
    @Bind(R.id.spinner) Spinner mSpinner;
    @Bind(R.id.phone_number) EditText mPhoneNumber;
    @Bind(R.id.button_register) Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
        mSpinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<>();
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
        mPhoneNumber.setVisibility(View.GONE);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((mDni.getText().toString().equals("36850606") && (mTramideId.getText().toString().equals("19114724")))){
                    mName.setVisibility(View.VISIBLE);
                    mUser.setVisibility(View.VISIBLE);
                    mTramideId.setVisibility(View.VISIBLE);
                    mPassword.setVisibility(View.VISIBLE);
                    mRepeatPassword.setVisibility(View.VISIBLE);
                    mEmail.setVisibility(View.VISIBLE);
                    mSpinner.setVisibility(View.VISIBLE);
                    mPhoneNumber.setVisibility(View.VISIBLE);
                    mName.setText("Lucas Sebastian Farias");
                } else {
                    Utils.createGlobalDialog(RegisterActivity.this, "Error en la obtención de datos","El DNI / Nro. Tramite del documento es inválido").show();
                }
            }
        });

       // setUpView();
    }

    //public void setUpView(){

    //}

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}

