package com.example.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler h=new Handler();
        // نفذ كود معين بعد مدة معينة فبستخدم post delayed
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(splash.this,login.class);
                startActivity(intent);
            }
        },2000);


    }
}