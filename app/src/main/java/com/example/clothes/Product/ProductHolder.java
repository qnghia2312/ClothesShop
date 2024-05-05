package com.example.clothes.Product;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clothes.R;


public class ProductHolder extends RecyclerView.ViewHolder {
    private ImageView product_img;
    private TextView product_name, product_price;
    EditText editTextQuantity;
    public ProductHolder(@NonNull View itemView) {
        super(itemView);
        product_img = itemView.findViewById(R.id.product_image);
        product_name = itemView.findViewById(R.id.product_name);
        product_price = itemView.findViewById(R.id.product_price);
        editTextQuantity = itemView.findViewById(R.id.editTextQuantity);
    }

    public ImageView getProduct_img() {
        return product_img;
    }

    public void setProduct_img(ImageView product_img) {
        this.product_img = product_img;
    }

    public TextView getProduct_name() {
        return product_name;
    }

    public void setProduct_name(TextView product_name) {
        this.product_name = product_name;
    }

    public TextView getProduct_price() {
        return product_price;
    }

    public void setProduct_price(TextView product_price) {
        this.product_price = product_price;
    }

    public EditText getEditTextQuantity() {
        return editTextQuantity;
    }

    public void setEditTextQuantity(EditText editTextQuantity) {
        this.editTextQuantity = editTextQuantity;
    }
}
