package com.example.clothes.Order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clothes.DataLogin;
import com.example.clothes.Model.Cart;
import com.example.clothes.Model.order;
import com.example.clothes.R;
import com.example.clothes.Setting.ItemOffset;
import com.example.clothes.databinding.FragmentHomeBinding;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    private FragmentHomeBinding binding;
    private String user_id = DataLogin.id1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        //Khởi tạo recycle view
        RecyclerView recyclerView = view.findViewById(R.id.recyclerOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        //Tạo đường kẻ ngang, khoảng cách giữa các items
        DividerItemDecoration itemDecorator = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecorator);
        ItemOffset itemOffset = new ItemOffset(25);
        recyclerView.addItemDecoration(itemOffset);

        //Khởi tạo database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ordReference = database.getReference("order");

        Query query = ordReference.orderByChild("user_id").equalTo(user_id);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<order> items = new ArrayList<>();
                for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                    order ord = childSnapshot.getValue(order.class);
                    if(ord.getStatus()!=4){
                        items.add(ord);
                    }

                }

                // Cập nhật adapter sau khi lấy được dữ liệu

                recyclerView.setAdapter(new OrderAdapter(items, getActivity()));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
