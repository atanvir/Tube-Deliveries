package com.TUBEDELIVERIES.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AuthModel {

    @SerializedName("user")
    ResponseBean user;

    public ResponseBean getAuth() {
        return auth;
    }

    public void setAuth(ResponseBean auth) {
        this.auth = auth;
    }

    @SerializedName("auth")
    ResponseBean auth;

    @SerializedName("registerResponse")
    ResponseBean registerResponse;


    @SerializedName("nearBy")
    private List<ResponseBean> nearBy;

    public List<ResponseBean> getNearBy() {
        return nearBy;
    }

    public void setNearBy(List<ResponseBean> nearBy) {
        this.nearBy = nearBy;
    }

    public ResponseBean getRegisterResponse() {
        return registerResponse;
    }

    public void setRegisterResponse(ResponseBean registerResponse) {
        this.registerResponse = registerResponse;
    }

    public ResponseBean getUser() {
        return user;
    }

    public void setUser(ResponseBean user) {
        this.user = user;
    }
}
