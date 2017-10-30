package com.example.lfarias.actasdigitales.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Px;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Html;
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
import android.widget.Toast;

import com.example.lfarias.actasdigitales.AsyncTask.BuscarDatosImagenAsynctask;
import com.example.lfarias.actasdigitales.AsyncTask.ChangePasswordAsynctask;
import com.example.lfarias.actasdigitales.AsyncTask.CrearSolicitudAsynctask;
import com.example.lfarias.actasdigitales.AsyncTask.DatabaseReadObject;
import com.example.lfarias.actasdigitales.AsyncTask.ImagenActaAsynctask;
import com.example.lfarias.actasdigitales.AsyncTask.SearchParentBookTypeAsynctask;
import com.example.lfarias.actasdigitales.Cache.CacheService;
import com.example.lfarias.actasdigitales.Entities.ConnectionParams;
import com.example.lfarias.actasdigitales.Entities.ResponseEntities.ResponseParents;
import com.example.lfarias.actasdigitales.Entities.SolicitudActa;
import com.example.lfarias.actasdigitales.Helpers.Utils;
import com.example.lfarias.actasdigitales.MercadoPago.MainExample.MPMainActivity;
import com.example.lfarias.actasdigitales.R;
import com.example.lfarias.actasdigitales.Services.ServiceUtils;
import com.google.gson.Gson;
import com.mercadopago.MercadoPagoBaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.BindDimen;
import okhttp3.internal.Util;

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
        CacheService.getInstance().clear();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Solicitar acta </font>"));
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
    public static class PlaceholderFragment extends Fragment implements ImagenActaAsynctask.Callback, SearchParentBookTypeAsynctask.Callback, CrearSolicitudAsynctask.Callback, BuscarDatosImagenAsynctask.Callback {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        View rootView;
        ProgressDialog dialog;

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

            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:

                    rootView = inflater.inflate(R.layout.fragment_page_1, container, false);
                    final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rdgGrupo);
                    radioGroup.setVisibility(View.GONE);
                    final TextView viewText = (TextView) rootView.findViewById(R.id.sin_opciones);
                    viewText.setVisibility(View.GONE);
                    final RadioButton buttonPropio = (RadioButton) radioGroup.findViewById(R.id.rdbOne);
                    final RadioButton buttonMadre = (RadioButton) radioGroup.findViewById(R.id.rdbTwo);
                    final RadioButton buttonPadre = (RadioButton) radioGroup.findViewById(R.id.rdbThree);
                    final RadioButton buttonConyuge = (RadioButton) radioGroup.findViewById(R.id.rdbSeven);
                    radioGroup.clearCheck();
                    final Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner1);
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
                            switch (spinner.getSelectedItem().toString()) {
                                case "Nacimiento":
                                    CacheService.getInstance().setTipoLibro(ServiceUtils.RequestData.nacimiento);
                                    callSearchTypeAsynctask(getContext(), PlaceholderFragment.this);
                                    break;
                                case "Matrimonio":
                                    CacheService.getInstance().setTipoLibro(ServiceUtils.RequestData.matrimonio);
                                    callSearchTypeAsynctask(getContext(), PlaceholderFragment.this);
                                    break;
                                case "Defunción":
                                    CacheService.getInstance().setTipoLibro(ServiceUtils.RequestData.defuncion);
                                    callSearchTypeAsynctask(getContext(), PlaceholderFragment.this);
                                    break;
                                case "Unión Convivencial":
                                    CacheService.getInstance().setTipoLibro(ServiceUtils.RequestData.union);
                                    callSearchTypeAsynctask(getContext(), PlaceholderFragment.this);
                                    break;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    rootView.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (radioGroup.getCheckedRadioButtonId() == -1) {
                                Toast.makeText(getContext(), "Tiene que seleccionar un parentezco", Toast.LENGTH_SHORT).show();
                            } else {
                                int idButton = radioGroup.getCheckedRadioButtonId();
                                RadioButton button = (RadioButton) rootView.findViewById(idButton);
                                switch (button.getText().toString()) {
                                    case "Padre":
                                        CacheService.getInstance().setParentesco(ServiceUtils.RequestData.padre);
                                        break;
                                    case "Madre":
                                        CacheService.getInstance().setParentesco(ServiceUtils.RequestData.matrimonio);
                                        break;
                                    case "Hijos":
                                        CacheService.getInstance().setParentesco(ServiceUtils.RequestData.hijos);
                                        break;
                                    case "Conyuge":
                                        CacheService.getInstance().setParentesco(ServiceUtils.RequestData.conyuge);
                                        break;
                                    case "Propia":
                                        CacheService.getInstance().setParentesco(ServiceUtils.RequestData.propia);
                                        break;
                                }

                                ViewPager mViewPager = (ViewPager) container.findViewById(R.id.container);
                                mViewPager.setCurrentItem(1);
                            }
                        }
                    });

                    break;

                case 2:
                    final CacheService instance = CacheService.getInstance();
                    rootView = inflater.inflate(R.layout.fragment_page_2, container, false);
                    ImageView imagen = (ImageView) rootView.findViewById(R.id.imagen_acta1);
                    LinearLayout layout_1 = (LinearLayout) rootView.findViewById(R.id.layout_1);
                    LinearLayout layout_2 = (LinearLayout) rootView.findViewById(R.id.layout_2);
                    LinearLayout layout_3 = (LinearLayout) rootView.findViewById(R.id.layout_3);
                    imagen.setVisibility(View.GONE);
                    layout_1.setVisibility(View.GONE);
                    layout_2.setVisibility(View.GONE);
                    layout_3.setVisibility(View.GONE);
                    final Button mButtonVisualize = (Button) rootView.findViewById(R.id.visualizar);

                    mButtonVisualize.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog = Utils.createLoadingIndicator(getContext());
                            if ("Visualizar".equals(mButtonVisualize.getText().toString())) {
                                ImagenActaAsynctask asynctask = new ImagenActaAsynctask(getContext(), PlaceholderFragment.this);
                                List<String> params = new ArrayList<>();
                                params.add(String.valueOf(CacheService.getInstance().getTipoLibro()));
                                params.add(String.valueOf(CacheService.getInstance().getParentesco()));

                                ConnectionParams conectParams = new ConnectionParams();
                                conectParams.setmControllerId(ServiceUtils.Controllers.CIUDADANO_CONTROLLER + "/" + ServiceUtils.Controllers.COMMON_INDEX_METHOD);
                                conectParams.setmActionId(ServiceUtils.Actions.BUSCAR_IMAGEN_MOBILE);
                                conectParams.setmSearchType(ServiceUtils.SearchType.IMAGEN_ACTA_SEARCH_TYPE);
                                conectParams.setParams(params);
                                dialog.show();
                                asynctask.execute(conectParams);

                                ImageView imagen = (ImageView) rootView.findViewById(R.id.imagen_acta1);
                                LinearLayout layout_1 = (LinearLayout) rootView.findViewById(R.id.layout_1);
                                LinearLayout layout_2 = (LinearLayout) rootView.findViewById(R.id.layout_2);
                                LinearLayout layout_3 = (LinearLayout) rootView.findViewById(R.id.layout_3);

                                imagen.setVisibility(View.VISIBLE);
                                layout_1.setVisibility(View.VISIBLE);
                                layout_2.setVisibility(View.VISIBLE);
                                layout_3.setVisibility(View.VISIBLE);

                                mButtonVisualize.setText("Confirmar");
                            } else if ("Confirmar".equals(mButtonVisualize.getText().toString())) {
                                CrearSolicitudAsynctask asynctask = new CrearSolicitudAsynctask(getContext(), PlaceholderFragment.this);
                                List<String> params = new ArrayList<>();
                                params.add(String.valueOf(CacheService.getInstance().getIdUser()));
                                params.add(String.valueOf(CacheService.getInstance().getParentesco()));
                                params.add(String.valueOf(CacheService.getInstance().getTipoLibro()));
                                TextView nombre = (TextView) rootView.findViewById(R.id.nombre_1);
                                if (nombre.getText().toString().isEmpty()) {
                                    Utils.createGlobalDialog(getContext(), "No se pudo crear la solicitud", "No se pudo crear la solicitud debido a falta de datos necesarios. Por favor contacte al soporte.").show();
                                } else {
                                    String nombre1 = nombre.getText().toString().substring(nombre.getText().toString().indexOf(",") + 1);
                                    nombre1.trim();
                                    String apellido = nombre.getText().toString().substring(0, nombre.getText().toString().indexOf(","));
                                    String apellido_1 = apellido.substring(apellido.indexOf(": ") + 1);
                                    String apellido_2 = apellido_1.substring(1, apellido_1.length());

                                    params.add(nombre1);
                                    params.add(apellido_2);

                                    ConnectionParams conectParams = new ConnectionParams();
                                    conectParams.setmControllerId(ServiceUtils.Controllers.CIUDADANO_CONTROLLER + "/" + ServiceUtils.Controllers.SOLICITUD_PATH);
                                    conectParams.setmActionId(ServiceUtils.Actions.CREAR_SOLICITUD);
                                    conectParams.setmSearchType(ServiceUtils.SearchType.CREAR_SOLICITUD_SEARCH_TYPE);
                                    conectParams.setParams(params);
                                    asynctask.execute(conectParams);
                                }
                            }
                        }
                    });

                    Button mButtonReportError = (Button) rootView.findViewById(R.id.reportar);
                    mButtonReportError.setVisibility(View.VISIBLE);
                    mButtonReportError.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getContext(), ReportErrorActivity.class);
                            TextView añoActa = (TextView) rootView.findViewById(R.id.año_acta);
                            TextView nroActa = (TextView) rootView.findViewById(R.id.nro_acta);
                            TextView nroLibro = (TextView) rootView.findViewById(R.id.nro_libro);

                            TextView nombre = (TextView) rootView.findViewById(R.id.nombre_1);

                            if (!añoActa.getText().toString().isEmpty()) {
                                i.putExtra("año_acta", añoActa.getText().toString());
                            }
                            if (!nroActa.getText().toString().isEmpty()) {
                                i.putExtra("nro_acta", nroActa.getText().toString());
                            }
                            if (!nroLibro.getText().toString().isEmpty()) {
                                i.putExtra("nro_libro", nroLibro.getText().toString());
                            }
                            if (!nombre.getText().toString().isEmpty()) {
                                String nombre1 = nombre.getText().toString().substring(nombre.getText().toString().indexOf(",") + 1);
                                nombre1.trim();
                                String apellido = nombre.getText().toString().substring(0, nombre.getText().toString().indexOf(","));
                                String apellido_1 = apellido.substring(apellido.indexOf(": ") + 1);
                                String apellido_2 = apellido_1.substring(1, apellido_1.length());


                                i.putExtra("nombre", nombre1);
                                i.putExtra("apellido", apellido_2);
                            }
                            startActivity(i);
                        }
                    });

                    Button mButtonCancelar = (Button) rootView.findViewById(R.id.cancelar);
                    mButtonCancelar.setVisibility(View.VISIBLE);
                    mButtonCancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getActivity().finish();
                            Intent i = new Intent(getContext(), LandingPageActivity.class);
                            startActivity(i);
                        }
                    });
                    break;

                case 3:
                    break;

            }
            return rootView;
        }

        @Override
        public void getImageBase64(String success) {
            dialog.dismiss();
            if (!success.isEmpty() && success != null) {
                final String imageBase64 = success;

                try {
                    byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    final ImageView view = (ImageView) rootView.findViewById(R.id.imagen_acta1);
                    if (decodedByte == null) {
                        view.setImageResource(R.drawable.image_not_loaded);
                    } else {
                        view.setImageBitmap(decodedByte);
                        callUserData();
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), DetailImageActivity.class);
                                intent.putExtra("image", imageBase64);
                                startActivity(intent);
                            }
                        });
                    }
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                    Utils.createGlobalDialog(getContext(), "Error", "Hubo un error al convertir la imagen del acta. Por favor intente nuevamente.").show();

                }
            } else if("false".equals(success)){
                TextView view1 = (TextView)rootView.findViewById(R.id.nombre_1);
                view1.setText("NO SE ENCONTRARON DATOS ASOCIADOS AL ACTA ELEGIDA");
                view1.setTypeface(Typeface.DEFAULT_BOLD);
                view1.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                view1.setVisibility(View.VISIBLE);

                final AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ContextThemeWrapper ctw = new ContextThemeWrapper(getContext(), R.style.AppTheme_PopupOverlay);
                    builder = new AlertDialog.Builder(ctw);
                } else {
                    builder = new AlertDialog.Builder(getContext());
                }
                builder.setTitle("Error al obtener la imagen")
                        .setMessage("Ocurrio un error al obtener la imagen de su acta. Por favor intente nuevamente mas tarde o contacte al soporte.")
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.drawable.error_1)
                        .show();
                final ImageView view = (ImageView) rootView.findViewById(R.id.imagen_acta1);
                view.setImageResource(R.drawable.image_not_loaded);
            }


        }

        public void callUserData() {
            BuscarDatosImagenAsynctask asynctask = new BuscarDatosImagenAsynctask(getContext(), PlaceholderFragment.this);

            ConnectionParams conectParams = new ConnectionParams();
            conectParams.setmControllerId(ServiceUtils.Controllers.CIUDADANO_CONTROLLER + "/" + ServiceUtils.Controllers.COMMON_INDEX_METHOD);
            conectParams.setmActionId(ServiceUtils.Actions.BUSCAR_DATOS_MOBILE);
            conectParams.setmSearchType(ServiceUtils.SearchType.OBTENER_DATOS_SEARCH_TYPE);
            dialog.show();
            asynctask.execute(conectParams);
        }

        @Override
        public void getParent(JSONArray success) {
            List<ResponseParents> parents = new ArrayList<>();
            try {
                RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rdgGrupo);
                radioGroup.setVisibility(View.GONE);
                final TextView viewText = (TextView) rootView.findViewById(R.id.sin_opciones);
                viewText.setVisibility(View.GONE);
                RadioButton button = (RadioButton) radioGroup.findViewById(R.id.rdbOne);
                button.setVisibility(View.GONE);
                RadioButton button1 = (RadioButton) radioGroup.findViewById(R.id.rdbFour);
                button1.setVisibility(View.GONE);
                RadioButton button2 = (RadioButton) radioGroup.findViewById(R.id.rdbSeven);
                button2.setVisibility(View.GONE);
                RadioButton button3 = (RadioButton) radioGroup.findViewById(R.id.rdbThree);
                button3.setVisibility(View.GONE);
                RadioButton button4 = (RadioButton) radioGroup.findViewById(R.id.rdbTwo);
                button4.setVisibility(View.GONE);
                for (int index = 0; index < success.length(); index++) {
                    JSONObject jsonObject = success.getJSONObject(index);

                    String nombre = jsonObject.get("nombreparentesco").toString();
                    switch (nombre) {
                        case "Propia":
                            radioGroup.setVisibility(View.VISIBLE);
                            button.setVisibility(View.VISIBLE);
                            button.setText(nombre);
                            viewText.setVisibility(View.GONE);
                            break;
                        case "Hijos":
                            radioGroup.setVisibility(View.VISIBLE);
                            button1.setVisibility(View.VISIBLE);
                            button1.setText(nombre);
                            viewText.setVisibility(View.GONE);
                            break;
                        case "Conyuge":
                            radioGroup.setVisibility(View.VISIBLE);
                            button2.setVisibility(View.VISIBLE);
                            button2.setText(nombre);
                            viewText.setVisibility(View.GONE);
                            break;
                        case "Padre":
                            radioGroup.setVisibility(View.VISIBLE);
                            button3.setVisibility(View.VISIBLE);
                            button3.setText(nombre);
                            viewText.setVisibility(View.GONE);
                            break;
                        case "Madre":
                            radioGroup.setVisibility(View.VISIBLE);
                            button4.setVisibility(View.VISIBLE);
                            button4.setText(nombre);
                            viewText.setVisibility(View.GONE);
                            break;
                        default:
                            radioGroup.setVisibility(View.GONE);
                            viewText.setVisibility(View.VISIBLE);
                            break;

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        public void callSearchTypeAsynctask(Context context, SearchParentBookTypeAsynctask.Callback callback) {
            SearchParentBookTypeAsynctask provincesDataRetrieveAsynctask = new SearchParentBookTypeAsynctask(context, callback);
            List<String> params = new ArrayList<>();
            params.add(String.valueOf(CacheService.getInstance().getTipoLibro()));

            ConnectionParams conectParams = new ConnectionParams();
            conectParams.setmControllerId(ServiceUtils.Controllers.CIUDADANO_CONTROLLER + "/" + ServiceUtils.Controllers.COMMON_INDEX_METHOD);
            conectParams.setmActionId(ServiceUtils.Actions.BUSCAR_PARENTESCO);
            conectParams.setmSearchType(ServiceUtils.SearchType.BUSCAR_PARENTESCO_SEARCH_TYPE);
            conectParams.setParams(params);

            provincesDataRetrieveAsynctask.execute(conectParams);
        }

        @Override
        public void createRequest(final String success) {

            final AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ContextThemeWrapper ctw = new ContextThemeWrapper(getContext(), R.style.AppTheme_PopupOverlay);
                builder = new AlertDialog.Builder(ctw);
            } else {
                builder = new AlertDialog.Builder(getContext());
            }
            builder.setTitle("Solicitud de acta creada con éxito")
                    .setMessage("Su acta ha sido creada. Desea realizar el pago de los códigos provinciales?")
                    .setPositiveButton("PAGAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent i = new Intent(getContext(), MPMainActivity.class);
                            i.putExtra("idSolicitud", success);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            getActivity().finish();
                        }
                    })
                    .setIcon(R.drawable.success_1)
                    .show();

        }

        @Override
        public void getUserDataMobile(JSONObject success) {
            dialog.dismiss();
            if (success.length() == 0) {

            } else {
                try {
                    TextView fechaView = (TextView) rootView.findViewById(R.id.año_acta);
                    TextView nroActaView = (TextView) rootView.findViewById(R.id.nro_acta);
                    TextView nroLibroView = (TextView) rootView.findViewById(R.id.nro_libro);
                    TextView oficinaView = (TextView) rootView.findViewById(R.id.oficina);
                    TextView dniView = (TextView) rootView.findViewById(R.id.dni);
                    TextView nombreCompletoView = (TextView) rootView.findViewById(R.id.nombre_1);

                    String nombreProp = success.getString("persona");
                    String apellidoProp = success.getString("apellido");
                    String dni = success.getString("dni");
                    String nrolibro = success.getString("nrolibro");
                    String nroActa = success.getString("nroacta");
                    String ubicacion = success.getString("ubicacion");

                    fechaView.setText("Fecha: " + success.getString("fecha_nacimiento"));
                    nombreCompletoView.setText("Nombre Completo: " + apellidoProp + "," + nombreProp);
                    nroActaView.setText("Nro. de Acta: " + success.getString("nroacta"));
                    nroLibroView.setText("Nro. de Libro: " + success.getString("nrolibro"));
                    oficinaView.setText("Oficina de Insc.: " + success.getString("ubicacion"));
                    dniView.setText("DNI: " + success.getString("dni"));

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
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
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
        switch (item.getItemId()) {
            case R.id.action_settings:
                finish();
                CacheService.getInstance().clear();
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
