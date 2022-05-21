package com.example.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityLogin extends AppCompatActivity {
    Button btn_login;
    TextView tvNewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login = findViewById(R.id.btn_login);
        tvNewAccount = findViewById(R.id.tv_new_account);
        btn_login.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityLogin.this, ActivityHome.class);
            startActivity(intent);
        });

        tvNewAccount.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
            startActivity(intent);
        });

    }
}