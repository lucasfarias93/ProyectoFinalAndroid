package com.example.lfarias.actasdigitales.Entities.ResponseEntities;

import com.example.lfarias.actasdigitales.Entities.Provincia;

import java.util.List;

/**
 * Created by lfarias on 9/11/17.
 */

public class ProvinciaResponse {
    String module_name;
    String controller_name;
    String action_name;
    String limit_params;
    String scaffolg;
    String parameters[] = new String[1];

    List<Provincia> provincias;

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    public String getController_name() {
        return controller_name;
    }

    public void setController_name(String controller_name) {
        this.controller_name = controller_name;
    }

    public String getAction_name() {
        return action_name;
    }

    public void setAction_name(String action_name) {
        this.action_name = action_name;
    }

    public String getLimit_params() {
        return limit_params;
    }

    public void setLimit_params(String limit_params) {
        this.limit_params = limit_params;
    }

    public String getScaffolg() {
        return scaffolg;
    }

    public void setScaffolg(String scaffolg) {
        this.scaffolg = scaffolg;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    public List<Provincia> getProvincias() {
        return provincias;
    }

    public void setProvincias(List<Provincia> provincias) {
        this.provincias = provincias;
    }
}
