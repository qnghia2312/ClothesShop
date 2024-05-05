package com.example.clothes.Product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.clothes.Model.Product;

import java.util.List;


public class ProductsViewModel extends ViewModel {

    private MutableLiveData<List<Product>> _products = new MutableLiveData<>();

    public void setProducts(List<Product> products) {
        _products.setValue(products);
    }

    public LiveData<List<Product>> getProducts() {
        return _products;
    }
}