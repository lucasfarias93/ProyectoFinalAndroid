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

    static String nroActa;
    static String añoActa;
    static String nroLibro;
    static String nombre;
    static String apellido;

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

    public static String getNroLibro() {
        return nroLibro;
    }

    public static void setNroLibro(String nroLibro) {
        CacheService.nroLibro = nroLibro;
    }

    public static String getAñoActa() {
        return añoActa;
    }

    public static void setAñoActa(String añoActa) {
        CacheService.añoActa = añoActa;
    }

    public static String getNroActa() {
        return nroActa;
    }

    public static void setNroActa(String nroActa) {
        CacheService.nroActa = nroActa;
    }

    public static String getApellido() {
        return apellido;
    }

    public static void setApellido(String apellido) {
        CacheService.apellido = apellido;
    }

    public static String getNombre() {
        return nombre;
    }

    public static void setNombre(String nombre) {
        CacheService.nombre = nombre;
    }
}