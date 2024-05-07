package com.example.clothes.Model;

import java.io.Serializable;

public class order implements Serializable {
    private String id;
    private String user_id;
    private String createAt;
    private String updateAt;
    private int totalPrice;
    private int status;
    private String payment;
    private String  address;
    public order() {
    }

    public order(String id, String user_id, String createAt, String updateAt, int totalPrice, int status, String payment, String address) {
        this.id = id;
        this.user_id = user_id;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.totalPrice = totalPrice;
        this.status = status;
        this.payment = payment;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
