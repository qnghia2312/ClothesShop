package com.example.clothes.Model;

import java.util.Map;
import java.util.TreeMap;

public class size_stock {
    private  String product_id;

    private Map<String, Integer> size_stock = new TreeMap<String, Integer>();

    public size_stock() {
    }

    public size_stock(String product_id, Map<String, Integer> size_stock) {
        this.product_id = product_id;
        this.size_stock = size_stock;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public Map<String, Integer> getSize_stock() {
        return size_stock;
    }

    public void setSize_stock(Map<String, Integer> size_stock) {
        this.size_stock = size_stock;
    }
}
