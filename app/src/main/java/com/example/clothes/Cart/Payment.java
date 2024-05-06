package com.example.clothes.Cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clothes.DataLogin;
import com.example.clothes.Model.Cart;
import com.example.clothes.Model.Product;
import com.example.clothes.Model.User;
import com.example.clothes.Model.discountOnOrder;
import com.example.clothes.Model.importOrder;
import com.example.clothes.Model.size_stock;
import com.example.clothes.R;
import com.example.clothes.Setting.ItemOffset;
import com.google.android.material.chip.Chip;
import com.google.common.util.concurrent.AtomicDouble;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class Payment extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    FirebaseDatabase databases = FirebaseDatabase.getInstance();
    DatabaseReference reference = databases.getReference("cart");
    String user_id = DataLogin.id1;
    User user = new User();
    int totalCost = 0;
    private CartAdapter adapter;
    ImageButton btnBack;
    TextView name, phone, address, priceShow, totalPrice, discountPrice, fPrice2, fPrice;
    RecyclerView show;
    Chip paymentMethod;
    Button btnPay;
    Spinner voucher;
    List<Cart> items = new ArrayList<>();
    List<String> list_discount = new ArrayList<>();
    List<discountOnOrder> discountOnOrderList = new ArrayList<>();
    int fp=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        setControl();
        setData();
        setEvent();
    }

    private void setControl() {
        btnBack = findViewById(R.id.payment_back);
        name = findViewById(R.id.payment_name);
        phone = findViewById(R.id.payment_phone);
        address = findViewById(R.id.payment_address);
        priceShow = findViewById(R.id.payment_priceShow);
        totalPrice = findViewById(R.id.payment_totalPrice);
        discountPrice = findViewById(R.id.payment_discountPrice);
        fPrice2 = findViewById(R.id.payment_finalPrice2);
        fPrice = findViewById(R.id.finalPrice);
        show = findViewById(R.id.payment_show);
        paymentMethod = findViewById(R.id.payment_methods);
        btnPay = findViewById(R.id.payment_order);
        voucher = findViewById(R.id.payment_Vocher);
        list_discount.add(" ");
        show.setLayoutManager(new LinearLayoutManager(Payment.this));
        DividerItemDecoration itemDecorator = new DividerItemDecoration(Payment.this, DividerItemDecoration.VERTICAL);
        show.addItemDecoration(itemDecorator);

        // lưu trữ adapter
        adapter = new CartAdapter(Payment.this, items);
        show.setAdapter(adapter);
        //Thêm khoảng cách giữa các items
        ItemOffset itemOffset = new ItemOffset(20);
        show.addItemDecoration(itemOffset);
    }

    private void setData() {
        database.child("user").orderByChild("user_id").equalTo(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User us = dataSnapshot.getValue(User.class);
                    if(us != null){
                        user = us;
                        showDataUser(user);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Payment.this,"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();
            }
        });

        Query query = reference.orderByChild("user_id").equalTo(user_id);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Cart cart = childSnapshot.getValue(Cart.class);
                    items.add(cart);
                }
                // Lấy dữ liệu tất cả sản phẩm
                DatabaseReference productsRef = databases.getReference("product");
                productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot productSnapshot) {
                        Map<String, Product> products = new HashMap<>();
                        for (DataSnapshot childSnapshot : productSnapshot.getChildren()) {
                            Product product = childSnapshot.getValue(Product.class);
                            products.put(product.getId(), product);
                        }

                        // Cập nhật tên sản phẩm cho các item trong giỏ hàng
                        for (Cart cart : items) {
                            String productId = cart.getProduct_id();
                            Product product = products.get(productId);
                            if (product != null) {
                                cart.setName(product.getName());
                                cart.setImage(product.getImage());
                                cart.setPrice(product.getPrice());
                                int cost = product.getPrice()*cart.getQuantity();
                                totalCost += cost;
                            }
                        }
                        priceShow.setText("đ."+ totalCost);
                        totalPrice.setText("đ."+ totalCost);
                        // Cập nhật adapter sau khi lấy được dữ liệu sản phẩm
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý lỗi
                        Toast.makeText(Payment.this,"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();
                    }
                });



//                RecyclerView recyclerView = view.findViewById(R.id.recyclerCart);
//                recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
////                recyclerView.setAdapter(new CartAdapter(requireActivity(), items));
//                CartAdapter adapter = new CartAdapter(requireActivity(), items);
//                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Payment.this,"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();
            }
        });
        database.child("discountOnOrder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    discountOnOrder discount = new discountOnOrder();
                    discount = dataSnapshot.getValue(discountOnOrder.class);
                    if(discount!=null) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                            format.setTimeZone(TimeZone.getTimeZone("GMT"));
                            Calendar currentTime = Calendar.getInstance();
                            Calendar dateS = Calendar.getInstance();
                            Calendar dateE = Calendar.getInstance();
                            try {
                                dateS.setTime(format.parse(discount.getTimeStart()));
                                dateE.setTime(format.parse(discount.getTimeEnd()));
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            if (dateE.after(currentTime)&&currentTime.after(dateS)) {
                                discountOnOrderList.add(discount);
                                String data = "";
                                data = "Khuyến mãi: " + discount.getName() + "\n Thời gian"
                                        + discount.getTimeStart() + " - " + discount.getTimeEnd()
                                        + "\n Khuyến mãi: " + discount.getPercent() + " %."
                                        + " Cho đơn từ: "+discount.getCondition();
                                list_discount.add(data);
                            }
                        }
                    }
                }
                ArrayAdapter<String> adapterVC = new ArrayAdapter(Payment.this, android.R.layout.simple_list_item_1,list_discount);
                voucher.setAdapter(adapterVC);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Payment.this, "Không thể kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDataUser(User user) {
        name.setText(user.getName());
        phone.setText(user.getPhone());
        address.setText(user.getAddress());
    }

    private void setEvent() {
        btnBack.setOnClickListener(view -> finish());
        voucher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    fPrice.setText("Tổng thanh toán \n đ."+ totalCost);
                    discountPrice.setText("đ.0");
                    fPrice2.setText("đ."+ totalCost);
                }
                else{
                    discountOnOrder discount = discountOnOrderList.get(i-1);
                    if(totalCost < discount.getCondition()){
                        Toast.makeText(view.getContext(), "Không đủ điều kiện dùng Vocher", Toast.LENGTH_SHORT).show();
                        voucher.setSelection(0);
                    }else{
                        Toast.makeText(view.getContext(), "Đang dùng Vocher", Toast.LENGTH_SHORT).show();
                        fp = totalCost-(totalCost / 100 * discount.getPercent());
                        discountPrice.setText("đ."+totalCost / 100 * discount.getPercent());
                        fPrice.setText("Tổng thanh toán \n đ."+ fp);
                        fPrice2.setText("đ."+ fp);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Payment.this, "Không thể kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}