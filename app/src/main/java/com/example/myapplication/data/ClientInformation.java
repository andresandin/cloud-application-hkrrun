package com.example.myapplication.data;

public class ClientInformation {

    private static ClientInformation ourInstance = new ClientInformation();

    private boolean darkMode = false, loginAnimation = false;
    private String token = "";
    //private ArrayList<House> houseList = new ArrayList<>();

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

    public boolean isLoginAnimation() {
        return loginAnimation;
    }

    public void setLoginAnimation(boolean loginAnimation) {
        this.loginAnimation = loginAnimation;
    }
}
