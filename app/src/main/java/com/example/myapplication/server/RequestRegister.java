package com.example.myapplication.server;

public class RequestRegister {
    private String username;
    private String password;
    private String email;
    private String ssn;
    private int privilege;

    public RequestRegister() {
        this("","","","",0);
    }

    public RequestRegister(String username, String password, String email, String ssn, int privilege) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.ssn = ssn;
        this.privilege = privilege;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String Password) {
        this.password = Password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }
}
