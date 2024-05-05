package com.example.clothes.Cart;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clothes.R;

public class CartHolder  extends RecyclerView.ViewHolder {
    private ImageView img;
    private TextView name, price, quantity, size;
    ImageButton btnViewDetailsCart, btnDeleteCart;
    Button btnThanhToan, buttonDecrease, buttonIncrease, btnPayAll;
    TextView totalPriceCartItem, quantityCartItem;


    public CartHolder(@NonNull View itemView, final CartAdapter.CartAdapterListener adapterListener) {
        super(itemView);
        img =  itemView.findViewById(R.id.cart_image);
        name = itemView.findViewById(R.id.nameCartItem);
        price = itemView.findViewById(R.id.totalPriceCartItem);
        quantity = itemView.findViewById(R.id.quantityCartItem);
        btnViewDetailsCart = itemView.findViewById(R.id.btnViewDetailsCart);
        btnDeleteCart = itemView.findViewById(R.id.btnDeleteCart);
        btnThanhToan = itemView.findViewById(R.id.btnThanhToan);
        totalPriceCartItem = itemView.findViewById(R.id.totalPriceCartItem);
        quantityCartItem = itemView.findViewById(R.id.quantityCartItem);
        buttonIncrease  = itemView.findViewById(R.id.buttonIncrease);
        buttonDecrease = itemView.findViewById(R.id.buttonDecrease);
        size = itemView.findViewById(R.id.textViewSize);
//        btnPayAll = itemView.findViewById(R.id.btnPayAll);

        btnViewDetailsCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapterListener != null){
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        adapterListener.ShowDetail(position);
                    }
                }
            }
        });

        btnDeleteCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapterListener != null) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        adapterListener.onDeleteItemClick(position);
                    }
                }
            }
        });

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapterListener != null) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        try{
                            int totalPrice = Integer.parseInt(totalPriceCartItem.getText().toString());
                            adapterListener.onPayItemClick(position, totalPrice);
                        } catch (NumberFormatException e){
                            // lỗi

                        }

                    }
                }
            }
        });

        buttonDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapterListener != null) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        adapterListener.DecreaseItem(position);
                    }
                }
            }
        });

        buttonIncrease.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (adapterListener != null) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        adapterListener.IncreaseItem(position);
                    }
                }
            }
        });

    }

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getPrice() {
        return price;
    }

    public void setPrice(TextView price) {
        this.price = price;
    }

    public TextView getQuantity() {
        return quantity;
    }

    public void setQuantity(TextView quantity) {
        this.quantity = quantity;
    }

    public TextView getSize() {
        return size;
    }

    public void setSize(TextView size) {
        this.size = size;
    }
}
