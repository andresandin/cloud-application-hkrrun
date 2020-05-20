package com.example.myapplication.server;

public class ResponseRegister {

    private String token;
    private int result;

    public ResponseRegister() {
        this("",0);
    }

    public ResponseRegister(String token, int result) {
        this.token = token;
        this.result = result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

}
