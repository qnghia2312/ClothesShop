package com.example.clothes.Model;

public class User {
    private String user_id;
    private String name;
    private String rule;
    private String email;
    private String address;
    private String birth;
    private String avata;
    private String phone;
    private String sex;

    public User(String user_id, String name, String rule, String email, String address, String birth, String avata, String phone, String sex) {
        this.user_id = user_id;
        this.name = name;
        this.rule = rule;
        this.email = email;
        this.address = address;
        this.birth = birth;
        this.avata = avata;
        this.phone = phone;
        this.sex = sex;
    }

    public User() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAvata() {
        return avata;
    }

    public void setAvata(String avata) {
        this.avata = avata;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
