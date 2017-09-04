package com.example.lfarias.actasdigitales.Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 04/09/2017.
 */

public class ConnectionParams {

    String controller_id;
    String action_id;

    List<String> params = new ArrayList<>();

    public String getController_id() {
        return controller_id;
    }

    public void setController_id(String controller_id) {
        this.controller_id = controller_id;
    }

    public String getAction_id() {
        return action_id;
    }

    public void setAction_id(String action_id) {
        this.action_id = action_id;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }
}
