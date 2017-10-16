package com.example.lfarias.actasdigitales.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Px;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lfarias.actasdigitales.AsyncTask.ImagenActaAsynctask;
import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Helpers.Utils;
import com.example.lfarias.actasdigitales.MercadoPago.MainExample.MPMainActivity;
import com.example.lfarias.actasdigitales.R;
import com.example.lfarias.actasdigitales.Services.ServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;

public class RequestActActivity extends AppCompatActivity {


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("Solicitar acta");
        mActionBar.setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements ImagenActaAsynctask.Callback {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        View rootView;

        private static final String ARG_SECTION_NUMBER = "section_number";
        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            //TODO: crear 3 layout distintos uno para cada fragment y añadirlo asi.

            rootView = new View(getContext());

            switch(getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_page_1, container, false);
                    final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rdgGrupo);
                    Spinner spinner =(Spinner) rootView.findViewById(R.id.spinner1);
                    List<String> spinnerArray = new ArrayList<>();
                    spinnerArray.add("Seleccione tipo de acta");
                    spinnerArray.add("Nacimiento");
                    spinnerArray.add("Matrimonio");
                    spinnerArray.add("Defunción");
                    spinnerArray.add("Unión Convivencial");
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(spinnerArrayAdapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            switch ((int)id){
                                case 0:

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    rootView.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ViewPager mViewPager = (ViewPager)container.findViewById(R.id.container);
                            mViewPager.setCurrentItem(1);
                        }
                    });

                    break;

                case 2:
                    rootView = inflater.inflate(R.layout.fragment_page_2, container, false);
                    ImageView imagen = (ImageView)rootView.findViewById(R.id.imagen_acta);

                    String imageDataBytes = getContext().getString(R.string.image_base64).substring(getContext().getString(R.string.image_base64).indexOf(",")+1);

                    byte[] recvpicbyte = (Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(recvpicbyte, 0, recvpicbyte.length);

                    imagen.setImageBitmap(decodedByte);

                    ImagenActaAsynctask asynctask = new ImagenActaAsynctask(getContext(), this);

                    List<String> params = new ArrayList<>();
                    params.add("1/1");

                    ConnectionParams conectParams = new ConnectionParams();
                    conectParams.setmControllerId(ServiceUtils.Controllers.CIUDADANO_CONTROLLER + "/" + ServiceUtils.Controllers.COMMON_INDEX_METHOD);
                    conectParams.setmActionId(ServiceUtils.Actions.BUSCAR_IMAGEN_MOBILE);
                    conectParams.setmSearchType(ServiceUtils.SearchType.IMAGEN_ACTA_SEARCH_TYPE);
                    conectParams.setParams(params);
                    asynctask.execute(conectParams);

                    Button mButtonVisualize = (Button) rootView.findViewById(R.id.visualizar);
                    mButtonVisualize.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ViewPager mViewPager = (ViewPager)container.findViewById(R.id.container);
                            mViewPager.setCurrentItem(2);
                        }
                    });

                    Button mButtonReportError = (Button) rootView.findViewById(R.id.reportar);
                    mButtonReportError.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getContext(), ReportErrorActivity.class);
                            startActivity(i);
                        }
                    });
                    break;

                case 3:
                    //textView.setText(getResources().getString(R.string.payment_methods));
                    break;

            }
            return rootView;
        }

        @Override
        public void getImageBase64(String success) {
            if(!success.isEmpty() && success != null){
                JSONArray arr = null;
                try {
                    arr = new JSONArray(success);
                    JSONObject jObj = arr.getJSONObject(0);
                    String imageBase64 = jObj.getString("imagen");

                    byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    ImageView view = (ImageView) rootView.findViewById(R.id.imagen_acta);
                    view.setImageBitmap(decodedByte);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_request_act, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                finish();
                Intent i = new Intent(RequestActActivity.this, LoginActivity.class);
                startActivity(i);
                break;

            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
