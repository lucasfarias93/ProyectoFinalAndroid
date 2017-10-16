package com.example.lfarias.actasdigitales.Cache;

import com.example.lfarias.actasdigitales.Entities.SolicitudActa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 16/10/2017.
 */

public class CacheService {

    List<SolicitudActa> solicitudesUser1 = new ArrayList<>();
    private static CacheService instance = null;

    public static CacheService getInstance() {
        if(instance == null) {
            instance = new CacheService();
        }
        return instance;
    }

    public void crearActaUser1(SolicitudActa acta){
        solicitudesUser1.add(acta);
    }

    public List<SolicitudActa> getActaUser1(){
        return solicitudesUser1;
    }
}
