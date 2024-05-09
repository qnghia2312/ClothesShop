package com.example.clothes.Cart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clothes.DataLogin;
import com.example.clothes.Model.Cart;
import com.example.clothes.Model.Product;
import com.example.clothes.Product.ProductDetailsActivity;
import com.example.clothes.R;
import com.example.clothes.Setting.ItemOffset;
import com.example.clothes.databinding.FragmentCartBinding;
import com.example.clothes.Model.order;
import com.google.common.util.concurrent.AtomicDouble;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import org.json.JSONObject;


import ZaloPay.Api.CreateOrder;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

//import vn.zalopay.sdk.ZaloPayError;
//import vn.zalopay.sdk.ZaloPaySDK;
//import vn.zalopay.sdk.Environment;
//import vn.zalopay.sdk.listeners.PayOrderListener;
public class CartFragment extends Fragment {
    String user_id = DataLogin.id1;

    private FragmentCartBinding binding;

    private CartAdapter adapter;
    Button btnPayAll;
    View paymentMethodLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        AtomicDouble totalCost = new AtomicDouble(0);


        btnPayAll = view.findViewById(R.id.btnPayAll);
        btnPayAll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), Payment.class);
                startActivity(intent);
//                PayForAll(totalCost);
            }
        });

        List<Cart> items = new ArrayList<>();


        // Khởi tạo RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        DividerItemDecoration itemDecorator = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecorator);

        // lưu trữ adapter
        adapter = new CartAdapter(requireActivity(), items);
        recyclerView.setAdapter(adapter);
        //Thêm khoảng cách giữa các items
        ItemOffset itemOffset = new ItemOffset(20);
        recyclerView.addItemDecoration(itemOffset);

        // Thiết lập listener cho Adapter
        adapter.setCartAdapterListener(new CartAdapter.CartAdapterListener() {
            @Override
            public void onDeleteItemClick(int position) {
                showDeleteDialog(position);
            }


            @Override
            public void IncreaseItem(int position) {
                Increase(position);
            }

            @Override
            public void DecreaseItem(int position) {
                Decrease(position);
            }

            @Override
            public void ShowDetail(int position) {
                ShowProductDetails(position);
            }
        });

        // Khởi tạo Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("cart");


        Query query = reference.orderByChild("user_id").equalTo(user_id);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    btnPayAll.setEnabled(false);
                }
                items.clear();
                for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Cart cart = childSnapshot.getValue(Cart.class);
                    items.add(cart);
                }
                // Lấy dữ liệu tất cả sản phẩm
                DatabaseReference productsRef = database.getReference("product");
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
                                double cost = product.getPrice()*cart.getQuantity();
                                totalCost.addAndGet(cost);
                            }
                        }

                        // Cập nhật adapter sau khi lấy được dữ liệu sản phẩm
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý lỗi
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

            }
        });


        return view;
    }

    private void ShowProductDetails(int position) {
//        Toast.makeText(getActivity(), "position: "+ position,Toast.LENGTH_LONG).show();
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart");
        Query query = cartRef.orderByChild("user_id").equalTo(user_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int index = 0;
                for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                    if (index == position) {
                        Cart cart = itemSnapshot.getValue(Cart.class);
                        String Id = cart.getProduct_id();
                        Intent intent = new Intent(requireContext(), com.example.clothes.Product.ProductDetailsActivity.class);
                        intent.putExtra("Id_details", Id);
                        startActivity(intent);
                        break;
                    }
                    index++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log error or show error message
            }
        });

    }

    // Xử lý và xóa item
    private void showDeleteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Order");
        builder.setMessage("Do you want to delete this product?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteItemFromCart(position);
                Toast.makeText(getContext(), "Delete at: "+ position, Toast.LENGTH_LONG).show();
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

    private void deleteItemFromCart(int position) {
        // Tham chiếu đến node 'cart' trong Firebase
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart");
        Query query = cartRef.orderByChild("user_id").equalTo(user_id);


        // Lấy key của item cần xóa từ Firebase
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int index = 0;
                for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                    if (index == position) {
                        itemSnapshot.getRef().removeValue();
                        adapter.removeItem(position);
                        break;
                    }
                    index++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log error or show error message
            }
        });
    }

    // Xem chi tiết
    private void DetailsCart(int position) {
        Toast.makeText(getContext(), "item details at: "+ position, Toast.LENGTH_LONG).show();
    }

    private void Decrease(int position){
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart");
        Query query = cartRef.orderByChild("user_id").equalTo(user_id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int index = 0;
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        if (index == position) {
                            int currentQuantity = itemSnapshot.child("quantity").getValue(Integer.class);

                            if (currentQuantity > 1) {
                                currentQuantity--; // Giảm số lượng
                                itemSnapshot.getRef().child("quantity").setValue(currentQuantity); // Cập nhật lại Firebase
                            } else {
                            }
                            break;
                        }
                        index++;
                    }
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void Increase(int position){
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart");
        Query query = cartRef.orderByChild("user_id").equalTo(user_id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Kiểm tra xem có dữ liệu trả về không
                if (snapshot.exists()) {
                    int index = 0;
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        if (index == position) {
                            // Lấy giá trị quantity hiện tại từ Firebase
                            //Lấy size, productId
                            int currentQuantity = itemSnapshot.child("quantity").getValue(Integer.class);
                            String size = itemSnapshot.child("size").getValue(String.class);
                            String prId = itemSnapshot.child("product_id").getValue(String.class);
                            final int[] crQuan = {currentQuantity};
//                            Toast.makeText(getActivity(), "id: "+ prId+", size: "+size, Toast.LENGTH_LONG).show();
                            DatabaseReference prSizeRef = FirebaseDatabase.getInstance().getReference("productSize/"+prId);
                            prSizeRef.child(size).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        int limit = snapshot.getValue(Integer.class);
                                        if(crQuan[0]<limit){
                                            crQuan[0]++;
                                            // Cập nhật lại Firebase
                                            itemSnapshot.getRef().child("quantity").setValue(crQuan[0]);
                                        }else{
                                            Toast.makeText(getActivity(), "At limit!", Toast.LENGTH_LONG).show();
                                        }

                                    }else{
                                        Toast.makeText(getActivity(), "null!", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            break;

                        }
                        index++;
                    }
                } else {
                    // Không tìm thấy item
                    Toast.makeText(getContext(), "Không tìm thấy item", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}