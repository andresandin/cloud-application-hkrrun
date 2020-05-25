package com.example.myapplication.server;

public class ResponseLogin {
    private String token;
    private int privilege;

    public ResponseLogin() {
        this("",0);
    }

    public ResponseLogin(String token, int privilege) {
        this.token = token;
        this.privilege = privilege;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }
}
