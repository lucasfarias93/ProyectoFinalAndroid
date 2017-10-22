package com.example.lfarias.actasdigitales.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import com.example.lfarias.actasdigitales.R;

public class DetailImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_image);

        getSupportActionBar().hide();
        String imageBase64 = getIntent().getStringExtra("image");
        byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        ImageView view = (ImageView)findViewById(R.id.imagen_acta);
        view.setImageBitmap(decodedByte);
    }

    @Override
    public boolean onSupportNavigateUp(){
        supportFinishAfterTransition();
        return true;
    }
}
