package com.example.myapplication.data;

public class ClientInformation {

    private static ClientInformation ourInstance = new ClientInformation();

    private String token = "";

    public static ClientInformation getInstance() {
        return ourInstance;
    }

    private ClientInformation(){

    }

    public String getToken() {
        return token;
    }

    //Method used to set the token that is used to authorize the client.
    public void setToken(String token) {
        this.token = token;
    }


}
