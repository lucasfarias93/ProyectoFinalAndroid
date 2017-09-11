package com.example.lfarias.actasdigitales.Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 04/09/2017.
 */

public class ConnectionParams {

    String mControllerId;
    String mActionId;
    Integer mSearchType;

    List<String> params = new ArrayList<>();

    public String getmControllerId() {
        return mControllerId;
    }

    public void setmControllerId(String mControllerId) {
        this.mControllerId = mControllerId;
    }

    public String getmActionId() {
        return mActionId;
    }

    public void setmActionId(String mActionId) {
        this.mActionId = mActionId;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public Integer getmSearchType() {
        return mSearchType;
    }

    public void setmSearchType(Integer mSearchType) {
        this.mSearchType = mSearchType;
    }
}
