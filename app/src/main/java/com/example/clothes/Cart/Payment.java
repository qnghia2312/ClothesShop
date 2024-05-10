package com.example.clothes.Cart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clothes.DataLogin;
import com.example.clothes.Model.Cart;
import com.example.clothes.Model.Product;
import com.example.clothes.Model.User;
import com.example.clothes.Model.discountOnOrder;
import com.example.clothes.Model.importOrder;
import com.example.clothes.Model.order;
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

import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import ZaloPay.Api.CreateOrder;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
    RadioGroup paymentMethod;
    Button btnPay, btnGetLocation, btnChooseLocation;
    MapView mapView;
    Spinner voucher;
    List<Cart> items = new ArrayList<>();
    List<String> list_discount = new ArrayList<>();
    List<discountOnOrder> discountOnOrderList = new ArrayList<>();
    int fp=0;
    private LocationManager locationManager;
    private LocationListener locationListener;
    int PICK_IMAGE_REQUEST= 2020;
    Uri filePath=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        setControl();
        setData();
        setEvent();
    }

    private void setControl() {
        btnGetLocation = findViewById(R.id.btnGetLocation);
        btnChooseLocation = findViewById(R.id.btnChooseLocation);
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
        paymentMethod = findViewById(R.id.paymentMethod);
        btnPay = findViewById(R.id.payment_order);
        voucher = findViewById(R.id.payment_Vocher);
        mapView = findViewById(R.id.map);

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
                                int cost =  product.getPrice()*cart.getQuantity();
                                totalCost += cost;
                            }
                        }
                        priceShow.setText("đ."+ totalCost);
                        totalPrice.setText("đ."+ totalCost);
                        fPrice.setText("đ."+ totalCost);
                        fPrice2.setText("đ."+ totalCost);
                        fp = totalCost;
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

                    fp = totalCost;
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
//                Toast.makeText(Payment.this, "total: " + fp, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Payment.this, "Không thể kết nối", Toast.LENGTH_SHORT).show();
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(Payment.this, "total: " + fp, Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(Payment.this);
                builder.setTitle("Payment Confirmation");
                builder.setMessage("The total amount is " + fp + "đ. Do you want to continue?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedMethodId = paymentMethod.getCheckedRadioButtonId();
                        RadioButton selected = findViewById(selectedMethodId);
                        String method = selected.getText().toString();

                        boolean confirm = false;
                        if(method.equals("Thanh toán khi nhận hàng")){
                            Toast.makeText(Payment.this, "Thanh toán khi nhận hàng: " + fp, Toast.LENGTH_LONG).show();
                            confirm = true;
                        }else if(method.equals("Thanh toán ZaloPay")){
                            //Thanh toán
                            try {
                                thanhToanZaloPay(fp);
                                confirm = true;
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
//                        Toast.makeText(Payment.this, "confirm: "+ confirm, Toast.LENGTH_LONG).show();
                        if(confirm){
                            addToNewOrder(fp, method);
                            btnBack.callOnClick();
                        }else{
                            Toast.makeText(Payment.this, "Lỗi confirm. confirm  = "+ confirm, Toast.LENGTH_LONG).show();
                        }
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(Payment.this, "Get location", Toast.LENGTH_LONG).show();
                getLocation();
            }
        });

        btnChooseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Payment.this, "Choose location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Payment.this, MapsActivity.class);
                startActivityForResult(intent, 1);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            String selectedAddress = data.getStringExtra("selectedAddress");
            address.setText(selectedAddress);
        }
    }
    private void addToNewOrder(int fp, String method) {
        Query cartQuery = reference.orderByChild("user_id").equalTo(user_id);

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("order");
        DatabaseReference orderDetailsRef = FirebaseDatabase.getInstance().getReference("orderDetail");

        order ord = new order();
        String newId = orderRef.push().getKey();
        ord.setId(newId);
        ord.setStatus(1);
        ord.setUser_id(user_id);

        ord.setTotalPrice(fp);
        Date currentdate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formatted = dateFormat.format(currentdate);
        ord.setCreateAt(formatted);
        ord.setUpdateAt(formatted);
        ord.setPayment(method);
        ord.setAddress(String.valueOf(address.getText()));

        orderRef.child(newId).setValue(ord);


        cartQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot cartItem : snapshot.getChildren()){
                    Cart cart = cartItem.getValue(Cart.class);

                    DatabaseReference newOrderDetailRef = orderDetailsRef.child(newId);
                    // Thêm dữ liệu cho từng nhánh con
                    newOrderDetailRef.child(cart.getProduct_id()).child(cart.getSize()).setValue(cart.getQuantity());

                    cartItem.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void thanhToanZaloPay(int price) throws Exception {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        try {
            CreateOrder orderApi = new CreateOrder();
            JSONObject data = orderApi.createOrder(String.valueOf(price));

            String code=data.getString("return_code");
            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(Payment.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                        Toast.makeText(Payment.this, "Thanh toán thành công!", Toast.LENGTH_LONG).show();

                    }
                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        Toast.makeText(Payment.this, "Đã hủy thanh toán!", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        Toast.makeText(Payment.this, "Thanh toán thất bại!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
        catch (Exception e) {
        }

    }

    private void chooseLocation() {

    }

    private void getLocation() {
        if (isLocationEnabled()) {
            try {
                // Lấy vị trí hiện tại
                Location lc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(lc!=null){
                    Toast.makeText(Payment.this, "Location: "+ lc.toString(), Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(Payment.this, "null location!", Toast.LENGTH_LONG).show();
                }
            } catch (SecurityException e) {
                Toast.makeText(Payment.this, "Location false", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Vui lòng bật định vị để sử dụng chức năng này.", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}