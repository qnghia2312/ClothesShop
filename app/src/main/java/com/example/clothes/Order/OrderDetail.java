package com.example.clothes.Order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clothes.Model.importOrder;
import com.example.clothes.Model.order;
import com.example.clothes.Model.Product;
import com.example.clothes.Model.User;
import com.example.clothes.R;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetail extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ImageButton back;
    Chip cancel, compelete;
    TextView id, name, phone, address, createAt, updateAt, payment,
            totalPrice, status;
    ListView showProduct;
    order or = new order();
    User us = new User();
    List<importOrder> listOrder = new ArrayList<>();
    List<String> listShow = new ArrayList<>();
    List<Product> listProduct = new ArrayList<>();
    ArrayAdapter<String> adapter;
    int sumP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);
        setCotrol();
        setData();
        setEvent();
    }
    private void setCotrol() {
        back = findViewById(R.id.orderDetail_back);
        compelete = findViewById(R.id.orderDetail_complete);
        cancel = findViewById(R.id.orderDetail_cancel);
        id = findViewById(R.id.order_detail_id);
        name = findViewById(R.id.order_detail_name);
        phone = findViewById(R.id.order_detail_phone);
        address = findViewById(R.id.order_detail_address);
        createAt = findViewById(R.id.order_detail_createAt);
        updateAt = findViewById(R.id.order_detail_updateAt);
        payment = findViewById(R.id.order_detail_payment);
        totalPrice = findViewById(R.id.order_detail_totalPrice);
        status = findViewById(R.id.order_detail_status);
        showProduct = findViewById(R.id.order_detail_show);
    }
    private void setEvent() {
        back.setOnClickListener(view -> finish());
        compelete.setOnClickListener(view -> {
            if(or!=null){
                int status = or.getStatus();
                switch (status){
                    case 1:
                        Toast.makeText(OrderDetail.this,"Đơn hàng đang trong quá trình xử lý!", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        or.setStatus(3);
                        updateOrder(or);
                        Toast.makeText(OrderDetail.this,"Xác nhận đơn hàng thành công!", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(OrderDetail.this,"Đơn hàng đã được xác nhận trước đó!", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(OrderDetail.this,"Đơn hàng đã được hủy trước đó!", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
        cancel.setOnClickListener(view -> {
            if(or!=null){
                if(or.getStatus()==4){
                    Toast.makeText(OrderDetail.this,"Đơn hàng đã được hủy trước đó!", Toast.LENGTH_SHORT).show();
                }else{
                    or.setStatus(4);
                    updateOrder(or);
                    Toast.makeText(OrderDetail.this,"Hủy đơn hàng thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateOrder(order or) {
        database.child("order").child(or.getId()).setValue(or);
    }

    private void setData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle==null) return;
        or =  (order) bundle.get("order");
        assert or != null;
        id.setText(or.getId());
        createAt.setText(or.getCreateAt());
        updateAt.setText(or.getUpdateAt());
        payment.setText(or.getPayment());
        address.setText(or.getAddress());
        totalPrice.setText(or.getTotalPrice()+" VNĐ");
        String statuss = "";
        switch (or.getStatus()){
            case 1:
                statuss = "Đang chờ xử lý";
                cancel.setVisibility(View.VISIBLE);
                break;
            case 2:
                statuss = "Đang giao";
                compelete.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.GONE);
                break;
            case 3:
                statuss = "Đã nhận";
                compelete.setVisibility(View.GONE);
                break;
            case 4:
                statuss = "Đã hủy";
                break;
        }
        status.setText(statuss);
        String user_id = or.getUser_id();
        database.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User u = dataSnapshot.getValue(User.class);
                    if(u.getUser_id().equals(user_id)){
                        us = u;
                        break;
                    }
                }
                name.setText(us.getName());
                phone.setText(us.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.toException();
            }
        });
        database.child("product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProduct.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Product pr = dataSnapshot.getValue(Product.class);
                    if (pr != null) {
                        listProduct.add(pr);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderDetail.this,"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();
            }
        });
        database.child("orderDetail").child(or.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOrder.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    importOrder io = new importOrder();
                    io.setId(dataSnapshot.getKey());
                    Map<String, Object> sizeMapData = (Map<String, Object>) dataSnapshot.getValue();
                    Map<String, Integer> sizeMap = new HashMap<>();
                    for (Map.Entry<String, Object> entry : sizeMapData.entrySet()) {
                        sizeMap.put(entry.getKey(), ((Long) entry.getValue()).intValue());
                    }
                    for (Product pr: listProduct) {
                        if (pr.getId().equals(io.getId())){
                            io.setName(pr.getName());
                            io.setPrice(pr.getPrice());
                            break;
                        }
                    }
                    int sum = 0;
                    for (int value: sizeMap.values()){
                        sum+=value;
                    }
                    io.setStock(sum);
                    int total = sum*io.getPrice();
                    io.setTotalPrice(total);
                    io.setSize_stock(sizeMap);
                    listOrder.add(io);
                }
                showData(listOrder);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderDetail.this,"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showData(List<importOrder> listOrder) {
        listShow.clear();
        sumP =0;
        for (importOrder io: listOrder) {
            sumP += io.getTotalPrice();
            String data = "Sản phẩm: "+io.getName()+" - Giá sản phẩm: "+ io.getPrice()
                    +" VND\n Số lượng: ";
            for (String s: io.getSize_stock().keySet()){
                data += (s+": "+String.valueOf(io.getSize_stock().get(s))+"  ");
            }
            data += ("\n Tổng giá: "+ String.valueOf(io.getTotalPrice()));
            listShow.add(data);
        }

        adapter = new ArrayAdapter(OrderDetail.this, android.R.layout.simple_list_item_1,listShow);
        showProduct.setAdapter(adapter);
    }

}