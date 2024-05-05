package com.example.clothes.Product;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.clothes.Model.Product;
import com.example.clothes.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {
    Context context;
    List<Product> items;


    public ProductAdapter(Context context, List<Product> items) {
        this.context = context; //.getApplicationContext()
        this.items = items;

    }


    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ProductHolder(LayoutInflater.from(context).inflate(R.layout.item,  parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {

        holder.getProduct_name().setText(items.get(position).getName());
//        holder.product_img.setImageResource(Integer.parseInt(items.get(position).getImage()));
        Glide.with(context)
                .load(items.get(position).getImage())
                .into(holder.getProduct_img());
        holder.getProduct_price().setText(String.valueOf(items.get(position).getPrice() ));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getBindingAdapterPosition();
                String Id = items.get(position).getId();
                Intent intent = new Intent(view.getContext(), ProductDetailsActivity.class);
                intent.putExtra("Id_details", Id);
                view.getContext().startActivity(intent);
            }
        });


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Lấy vị trí của item được click
//                int position = holder.getBindingAdapterPosition();  //or getAbsoluteAdapterPosition
//                Toast.makeText(context, "Da click vao " + position, Toast.LENGTH_SHORT).show();
//                // Khởi tạo Activity mới với dữ liệu của item
//                Intent intent = new Intent(holder.itemView.getContext(), ProductDetailsActivity.class);
//                intent.putExtra("product_position", position);
////                context.startActivity(intent);
//                try {
//                    context.startActivity(intent);
//                } catch (ActivityNotFoundException e) {
//                    Log.e("ProductAdapter", "Activity not found: " + e.getMessage());
//                } catch (Exception e) {
//                    Log.e("ProductAdapter", "Lỗi khởi động Activity: " + e.getMessage());
//
//                }
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
