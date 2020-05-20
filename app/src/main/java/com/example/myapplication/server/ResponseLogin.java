package com.example.myapplication.server;

public class ResponseLogin {
    private String token;
    private String username;
    private int result;

    public ResponseLogin() {
        this("","",0);
    }

    public ResponseLogin(String token, String username, int result) {
        this.token = token;
        this.username = username;
        this.result = result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
