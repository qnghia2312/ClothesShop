package com.example.clothes.Model;

public class Account {
    private String acc;
    private String pass;
    private String rule;
    private boolean state;
    private String user_id;

    public Account() {
    }

    public Account(String acc, String pass, String rule, boolean state, String user_id) {
        this.acc = acc;
        this.pass = pass;
        this.rule = rule;
        this.state = state;
        this.user_id = user_id;
    }

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
