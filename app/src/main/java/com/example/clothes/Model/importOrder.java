package com.example.clothes.Model;

import java.util.HashMap;
import java.util.Map;

public class importOrder {
    int stock = 0;
    int price = 0;
    int totalPrice = 0;
    String id;
    String name;
    Map<String, Integer> size_stock = new HashMap<>();

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public importOrder(int price, int totalPrice, String id, String name, int stock, Map<String, Integer> size_stock) {
        this.price = price;
        this.totalPrice = totalPrice;
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.size_stock = size_stock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getSize_stock() {
        return size_stock;
    }

    public void setSize_stock(Map<String, Integer> size_stock) {
        this.size_stock = size_stock;
    }

    public importOrder() {
    }
}
