package com.example.clothes.Order;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clothes.Model.order;
import com.example.clothes.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> implements Filterable {
    List<order> list;
    Context context;
    public OrderAdapter(List<order> list, Context context){
        this.list = list;
        this.context = context;
    }
    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_order, parent, false);
        return new OrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        final order order_temp = list.get(position);
        if(order_temp==null) return;
        holder.id.setText(order_temp.getId());
        String status = "";
        switch (order_temp.getStatus()){
            case 1:
                status = "Đang xử lý";
                holder.status.setTextColor(Color.BLUE);
                break;
            case 2:
                status = "Đang giao";
                holder.status.setTextColor(Color.GREEN);
                break;
            case 3:
                status = "Đã nhận";
                holder.status.setTextColor(Color.BLACK);
                break;
            case 4:
                status = "Đã hủy";
                holder.status.setTextColor(Color.RED);
                break;
        }
        holder.status.setText(status);
        holder.createAt.setText(order_temp.getCreateAt());
        holder.updateAt.setText(order_temp.getUpdateAt());
        holder.totalPrice.setText(order_temp.getTotalPrice()+" VND");
        holder.layout.setOnClickListener(view -> pass(order_temp));
    }

    private void pass(order orderTemp) {
        Intent intent = new Intent(context, OrderDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("order", orderTemp);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView id, status, createAt, updateAt, totalPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout_item_order);
            id = itemView.findViewById(R.id.item_order_id);
            status = itemView.findViewById(R.id.item_order_status);
            createAt = itemView.findViewById(R.id.item_order_createAt);
            updateAt = itemView.findViewById(R.id.item_order_updateAt);
            totalPrice = itemView.findViewById(R.id.item_order_totalPrice);
        }
    }
}
