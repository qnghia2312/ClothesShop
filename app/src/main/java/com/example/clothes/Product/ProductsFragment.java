package com.example.clothes.Product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clothes.Model.Product;
import com.example.clothes.R;
import com.example.clothes.Setting.ItemOffset;
import com.example.clothes.databinding.FragmentProductsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {

    private FragmentProductsBinding binding;
    private ProductsViewModel viewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_products, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        //Thêm khoảng cách giữa các items
        ItemOffset itemOffset = new ItemOffset(20);
        recyclerView.addItemDecoration(itemOffset);
//        DividerItemDecoration itemDecorator = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(itemDecorator);

        // Khởi tạo Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("product");

        // Create ProductsViewModel instance
//        ProductsViewModel viewModel = ViewModelProviders.of(requireActivity()).get(ProductsViewModel.class);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Lấy dữ liệu snapshot
                List<Product> items = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Product product = childSnapshot.getValue(Product.class);
                    items.add(product);
                }
                // Update ViewModel with retrieved products
//                viewModel.setProducts(items);
                // Cập nhật RecyclerView
                recyclerView.setAdapter(new ProductAdapter(getActivity(), items));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi
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