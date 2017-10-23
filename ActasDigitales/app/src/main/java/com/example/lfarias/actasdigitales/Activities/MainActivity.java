package com.example.lfarias.actasdigitales.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lfarias.actasdigitales.AsyncTask.DatabaseReadObject;
import com.example.lfarias.actasdigitales.Entities.Roles;
import com.example.lfarias.actasdigitales.R;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /*private TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8, textView9, textView10, textView11, textView12;
    private ImageView imageView1, imageView2, imageView3;*/

    private Button buttonLaunch;

    //DatabaseOpenHelper databaseInstance = DatabaseOpenHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();
        buttonLaunch = (Button) findViewById(R.id.buttonLaunch);
        buttonLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

}


