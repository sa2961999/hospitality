package com.example.hospital;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
public class Home extends AppCompatActivity {
ImageView im_ambulance,im_intensive_care,im_xray,im_about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        im_about=findViewById(R.id.im_about);
        im_intensive_care =findViewById(R.id.im_inensive_care);
        im_xray=findViewById(R.id.im_xray);
        im_ambulance =findViewById(R.id.im_ambulance);
        im_ambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // Intent intent=new Intent(Home.this,Ambulance.class) ;
                // startActivity(intent);
            }
        });
        im_xray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intent=new Intent(Home.this,xray.class) ;
                // startActivity(intent);
            }
        });
        im_intensive_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intent=new Intent(Home.this,IntensiveCare.class) ;
                // startActivity(intent);
            }
        });
        im_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intent=new Intent(Home.this,About.class) ;
                // startActivity(intent);
            }
        });
    }
}