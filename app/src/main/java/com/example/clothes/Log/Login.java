package com.example.clothes.Log;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//
//import com.banhang.Adapter.Account;
import com.example.clothes.DataLogin;
import com.example.clothes.MainActivity;
import com.example.clothes.Model.Account;
import com.example.clothes.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    private EditText username_txt , password_txt;
    private TextView btn_register_now;
    private Button btn_login;
    private ChildEventListener mChildEventListener;
    List<Account> list_acc = new ArrayList<>();
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setInit();
        setData();
        handlerLogin();
    }
    private void setData()
    {
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Account account = new Account();
                account = snapshot.getValue(Account.class);
                if(account!=null){
                    list_acc.add(account);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        Query query = FirebaseDatabase.getInstance().getReference("account");
        query.addChildEventListener(mChildEventListener);
    }
    private boolean check()
    {
        username = username_txt.getText().toString().trim();
        password = password_txt.getText().toString().trim();
        if(username.isEmpty() || password.isEmpty())
        {
            Toast.makeText(Login.this,"Lack information, please fill up",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void handlerLogin()
    {

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checkLogin = false;
                if(check()){

                    if(!list_acc.isEmpty()){
                        for (Account acc: list_acc) {
                            if(acc.getAcc().equals(username) && acc.getPass().equals(password)){
                                if(acc.isState() && (acc.getRule().equals("KHACHHANG")||acc.getRule().equals("NHANVIEN"))) {
                                    Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    DataLogin.id1= acc.getUser_id();
                                    startActivity(intent);
                                    finish();
                                    checkLogin = true;
                                }
                            }
                        }
                    }
                    if(!checkLogin)
                        Toast.makeText(Login.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();}
            }

        });

        btn_register_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, RegisterActivity.class));
            }
        });
    }
    private void setInit()
    {
        username_txt = findViewById(R.id.username_txt);
        password_txt = findViewById(R.id.password_login);
        btn_login = findViewById(R.id.btn_login);
        btn_register_now = findViewById(R.id.btn_register_now);
    }

}