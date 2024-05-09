package com.example.clothes.Log;

import android.app.Activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//import com.banhang.Adapter.Account;
//import com.banhang.Adapter.User;
//import com.banhang.R;
import com.example.clothes.Model.Account;
import com.example.clothes.Model.User;
import com.example.clothes.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {


    private EditText emailTxt,passwordTxt,nameTxt, phoneTxt, addressTxt, confirmPasswordTxt;
    private Button btnRegister;
    private Spinner gender;
    private ArrayAdapter adapter_gender;
    private List<String> data_genders = new ArrayList<>();
    private EditText birth;
    private String gender_str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        setBirth();
        setEvent();
        setEventRegister();
    }
    private void setEventRegister()
    {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_txt = emailTxt.getText().toString();
                String password_txt = passwordTxt.getText().toString();
                String confirmPassword_txt = confirmPasswordTxt.getText().toString();
                String name_txt = nameTxt.getText().toString();
                String phone_txt = phoneTxt.getText().toString();
                String address_txt = addressTxt.getText().toString();

                if(email_txt.isEmpty() || password_txt.isEmpty() || confirmPassword_txt.isEmpty()
                        || name_txt.isEmpty() || phone_txt.isEmpty() || address_txt.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this,"Lack information, please fill up",Toast.LENGTH_SHORT).show();
                }
                //check passeword and confirm password
                else if(!password_txt.equals(confirmPassword_txt))
                {
                    Toast.makeText(RegisterActivity.this, "Password isn't matching", Toast.LENGTH_SHORT).show();
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("user");
                String userId = myRef.push().getKey();
                User user = new User();
                user.setUser_id(userId);
                user.setAddress(address_txt);
                user.setEmail(email_txt);
                user.setAvata("https://hanoispiritofplace.com/wp-content/uploads/2016/06/hinh-anh-nu-sinh-9.jpg");
                user.setName(name_txt);
                user.setPhone(phone_txt);
                user.setRule("KHACHHANG");
                user.setBirth(birth.getText().toString());
                user.setSex(gender_str);
                myRef.child(userId).setValue(user, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(RegisterActivity.this,"Add user success", Toast.LENGTH_SHORT).show();
                    }
                });

                Account account = new Account(email_txt,password_txt,"KHACHHANG",true,userId);
                DatabaseReference myAccount = database.getReference("account");
                myAccount.child(userId).setValue(account, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(RegisterActivity.this,"Add account success", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void setBirth()
    {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this
                        , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        birth.setText(formatDate(dayOfMonth,month,year));
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

    private void setEvent()
    {
        initData();
        adapter_gender = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data_genders);
        gender.setAdapter(adapter_gender);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(gender.getSelectedItem().equals("Nam"))
                {
                    gender_str = "Nam";
                }
                else if(gender.getSelectedItem().equals("Nữ"))
                {
                    gender_str = "Nữ";
                }
                else {
                    gender_str = "Khác";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void initData()
    {
        data_genders.add("Nam");
        data_genders.add("Nữ");
        data_genders.add("Khác");
    }

    private void init()
    {
        emailTxt = findViewById(R.id.email_txt);
        passwordTxt = findViewById(R.id.password_txt);
        nameTxt = findViewById(R.id.name_txt);
        phoneTxt = findViewById(R.id.phone_txt);
        addressTxt = findViewById(R.id.address_txt);
        btnRegister = findViewById(R.id.btn_register);
        confirmPasswordTxt = findViewById(R.id.confirm_pw_txt);
        gender = findViewById(R.id.gender);
        birth = findViewById(R.id.birth);
    }
}
