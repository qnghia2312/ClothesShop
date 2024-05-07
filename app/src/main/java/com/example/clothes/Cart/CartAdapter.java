package com.example.clothes.Cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.clothes.Model.Cart;
import com.example.clothes.Model.Product;
import com.example.clothes.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartHolder> {
    Context context;
    List<Cart> items;

    public interface CartAdapterListener {
        void onDeleteItemClick(int position);

        void onDetailsItemClick(int position);

        void IncreaseItem(int position);
        void DecreaseItem(int position);
        void ShowDetail(int position);
    }

    private CartAdapterListener adapterListener;

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }

    public void setCartAdapterListener(CartAdapterListener listener) {
        this.adapterListener = listener;
    }

    public CartAdapter(Context context, List<Cart> items) {
        this.context = context.getApplicationContext();
        this.items = items;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartHolder(LayoutInflater.from(context).inflate(R.layout.cart_item,  parent, false), adapterListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        holder.getName().setText(items.get(position).getName());
        holder.getPrice().setText(String.valueOf(items.get(position).getPrice() * items.get(position).getQuantity()));
        holder.getQuantity().setText(String.valueOf(items.get(position).getQuantity()));
        holder.getSize().setText(String.valueOf(items.get(position).getSize()));
        Glide.with(context)
                .load(items.get(position).getImage())
                .into(holder.getImg());

    }



    @Override
    public int getItemCount() {
        return items.size();
    }
}
