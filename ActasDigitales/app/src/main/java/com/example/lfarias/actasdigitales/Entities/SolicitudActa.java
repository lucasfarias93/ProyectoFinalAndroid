package com.example.lfarias.actasdigitales.Entities;

import java.util.Date;

/**
 * Created by acer on 16/10/2017.
 */

public class SolicitudActa {
    public String nombrePropietario;
    public int nroSolicitud;
    public String cuponPagoCodigo;
    public String parentesco;
    public String tipoSolicitud;
    public Date fechaSolicitud;
    public String estadoSolicitud;

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }

    public int getNroSolicitud() {
        return nroSolicitud;
    }

    public void setNroSolicitud(int nroSolicitud) {
        this.nroSolicitud = nroSolicitud;
    }

    public String getCuponPagoCodigo() {
        return cuponPagoCodigo;
    }

    public void setCuponPagoCodigo(String cuponPagoCodigo) {
        this.cuponPagoCodigo = cuponPagoCodigo;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(String estadoSolicitud) {
        this.estadoSolicitud = estadoSolicitud;
    }
}
