package com.example.clothes.Home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.clothes.DataLogin;
import com.example.clothes.Log.RegisterActivity;
import com.example.clothes.Model.Product;
import com.example.clothes.Model.User;
import com.example.clothes.Product.ProductDetailsActivity;
import com.example.clothes.R;
import com.example.clothes.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {
    private String  user_id = DataLogin.id1;
    private TextView tv_hello;
    private ImageView profile_image;
    private EditText edt_name, edt_phone_number,  edt_email, edt_address, edt_day_of_birth;
    private Spinner spin_gender;
    private Button btn_Confirm_Change;
    private ArrayAdapter adapter_gender;
    private List<String> data_genders = new ArrayList<>();
    private boolean isDataAdded = false;
    private FragmentHomeBinding binding;

    //Lấy người đang đăng nhập(user_id)
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("user");
    Query query = reference.orderByChild("user_id").equalTo(user_id);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        tv_hello = view.findViewById(R.id.tv_hello);
        edt_name = view.findViewById(R.id.edt_name);
        edt_phone_number = view.findViewById(R.id.edt_phone_number);
        edt_email = view.findViewById(R.id.edt_email);
        edt_address = view.findViewById(R.id.edt_address);
        edt_day_of_birth = view.findViewById(R.id.edt_day_of_birth);
        profile_image = view.findViewById(R.id.profile_image);
        spin_gender = view.findViewById(R.id.spin_gender);
        btn_Confirm_Change = view.findViewById(R.id.btn_Confirm_Change);

//        link();
        setBirth();
        addGender();
        showDaTa();

        return view;

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showDaTa() {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //lấy dữ liệu đầu tiên
                DataSnapshot childSnapshot = snapshot.getChildren().iterator().next();
                User us = childSnapshot.getValue(User.class);

                tv_hello.setText("Xin chào "+ us.getName());
                edt_name.setText(us.getName());
                edt_phone_number.setText(us.getPhone());
                edt_email.setText(us.getEmail());
                edt_address.setText(us.getAddress());
                edt_day_of_birth.setText(us.getBirth());
                Glide.with(HomeFragment.this).load(us.getAvata()).into(profile_image);

                adapter_gender = new ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, data_genders);
                spin_gender.setAdapter(adapter_gender);
                isDataAdded = true;
                String gender = us.getSex();
                int position = data_genders.indexOf(gender);
                if (position != -1) {
                    spin_gender.setSelection(position);
                }

                btn_Confirm_Change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setTitle("Save change");
                        builder.setMessage("Do you want to save all change?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = edt_name.getText().toString();
                                String phone = edt_phone_number.getText().toString();
                                String address = edt_address.getText().toString();
                                String birth = edt_day_of_birth.getText().toString();
                                if(name.isEmpty() || phone.isEmpty() || address.isEmpty() || birth.isEmpty()){
                                    Toast.makeText(requireContext(), "Please do not leave it blank!", Toast.LENGTH_LONG).show();
                                } else{
                                    us.setName(name);
                                    us.setPhone(phone);
                                    us.setAddress(address);
                                    us.setBirth(birth);
//                                    Toast.makeText(requireContext(), "gender: "+ spin_gender.getSelectedItem().toString(), Toast.LENGTH_LONG ).show();
                                    us.setSex(spin_gender.getSelectedItem().toString());

                                    childSnapshot.getRef().setValue(us);
                                    Toast.makeText(requireContext(), "Chỉnh sửa dữ liệu thành công!", Toast.LENGTH_LONG).show();
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    private void setBirth()
    {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        edt_day_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext()
                        , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        edt_day_of_birth.setText(formatDate(dayOfMonth,month,year));
                    }
                }, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                datePickerDialog.show();

            }
        });
    }

    private String formatDate(int day, int month, int year)
    {
        return day + "/" + month + "/"+ year;
    }

    private void addGender() {
        if(!isDataAdded){
            data_genders.add("Khác");
            data_genders.add("Nữ");
            data_genders.add("Nam");

        }



    }

//    private void link(){
//        tv_hello = getView().findViewById(R.id.tv_hello);
//        edt_name = getView().findViewById(R.id.edt_name);
//        edt_phone_number = getView().findViewById(R.id.edt_phone_number);
//        edt_email = getView().findViewById(R.id.edt_email);
//        edt_address = getView().findViewById(R.id.edt_address);
//        edt_day_of_birth = getView().findViewById(R.id.edt_day_of_birth);
//        profile_image = getView().findViewById(R.id.profile_image);
//        spin_gender = getView().findViewById(R.id.spin_gender);
//        btn_Confirm_Change = getView().findViewById(R.id.btn_Confirm_Change);
//    }
}