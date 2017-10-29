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

    private static String tokenSessionId;

    static int parentesco;
    static int tipoLibro;
    static int idUser;

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
        } else if ("diego".equals(userId)){
        }
    }

    public void crearActaUser1(SolicitudActa acta) {
        solicitudesUser1.add(acta);
    }

    public List<SolicitudActa> getActaUser1() {
        return solicitudesUser1;
    }


    public void clearUser1MockData(){
        solicitudesUser1.clear();
    }

    public static String getTokenSessionId() {
        return tokenSessionId;
    }

    public static void setTokenSessionId(String token) {
        tokenSessionId = token;
    }

    public static int getParentesco() {
        return parentesco;
    }

    public static void setParentesco(int parentesco) {
        CacheService.parentesco = parentesco;
    }

    public static int getTipoLibro() {
        return tipoLibro;
    }

    public static void setTipoLibro(int tipoLibro) {
        CacheService.tipoLibro = tipoLibro;
    }

    public static void clear() {
        instance = null;
    }

    public static int getIdUser() {
        return idUser;
    }

    public static void setIdUser(int idUser) {
        CacheService.idUser = idUser;
    }
}