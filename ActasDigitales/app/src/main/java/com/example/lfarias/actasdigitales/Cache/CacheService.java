package com.example.lfarias.actasdigitales.Cache;

import com.example.lfarias.actasdigitales.Entities.SolicitudActa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by acer on 16/10/2017.
 */

public class CacheService {

    List<SolicitudActa> solicitudesUser1 = new ArrayList<>();
    List<SolicitudActa> solicitudesUserHost = new ArrayList<>();

    private static CacheService instance = null;

    public static CacheService getInstance() {
        if (instance == null) {
            instance = new CacheService();
        }
        return instance;
    }

    public void initActaUser1(String userId) {
        if("lucas".equals(userId)){
            solicitudesUser1 = createUser1MockData();
        } else if ("diego".equals(userId)){
        }
    }

    public void crearActaUser1(SolicitudActa acta) {
        solicitudesUser1.add(acta);
    }

    public List<SolicitudActa> getActaUser1() {
        return solicitudesUser1;
    }

    public static List<SolicitudActa> createUser1MockData() {
        SolicitudActa acta1 = new SolicitudActa();
        acta1.setNombrePropietario("FARIAS, Jorge Horacio");
        acta1.setNroSolicitud("2");
        acta1.setParentesco("Padre");
        acta1.setCuponPagoCodigo("2xd53");
        acta1.setTipoSolicitud("Nacimiento");
        Date cDate1 = Calendar.getInstance().getTime();
        String fDate1 = new SimpleDateFormat("yyyy-MM-dd").format(cDate1);
        acta1.setFechaSolicitud(fDate1);
        acta1.setEstadoSolicitud("Pagada");

        SolicitudActa acta2 = new SolicitudActa();
        acta2.setNombrePropietario("CERUTTI, Virginia");
        acta2.setNroSolicitud("3");
        acta2.setParentesco("Madre");
        acta2.setCuponPagoCodigo("-");
        acta2.setTipoSolicitud("Nacimiento");
        Date cDate2 = Calendar.getInstance().getTime();
        String fDate2 = new SimpleDateFormat("yyyy-MM-dd").format(cDate2);
        acta2.setFechaSolicitud(fDate2);
        acta2.setEstadoSolicitud("Confirmada");

        SolicitudActa acta3 = new SolicitudActa();
        acta3.setNombrePropietario("FARIAS CERUTTI, María Belen");
        acta3.setNroSolicitud("4");
        acta3.setParentesco("Hermana");
        acta3.setCuponPagoCodigo("-");
        acta3.setTipoSolicitud("Nacimiento");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, -5);
        Date cDate3 = calendar.getTime();
        String fDate3 = new SimpleDateFormat("yyyy-MM-dd").format(cDate3);
        acta3.setFechaSolicitud(fDate3);
        acta3.setEstadoSolicitud("Pendiente");

        List<SolicitudActa> actasUser1 = new ArrayList<>();
        actasUser1.add(acta1);
        actasUser1.add(acta2);
        actasUser1.add(acta3);

        return actasUser1;
    }

    public void clearUser1MockData(){
        solicitudesUser1.clear();
    }
}