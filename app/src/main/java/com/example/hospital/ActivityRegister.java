package com.example.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ActivityRegister extends AppCompatActivity {
    private FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //initialize firebase auth
        Auth = FirebaseAuth.getInstance();
        if (Auth.getCurrentUser() != null) {
            Auth.signOut();
        }
        Button bregister = findViewById(R.id.bregister);
        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        TextView tvsignin = findViewById(R.id.tvsignin);
        tvsignin.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
            startActivity(intent);
        });
    }

    private void registerUser() {
        EditText etfname = findViewById(R.id.etfname);
        EditText etlname = findViewById(R.id.etlname);
        EditText etpassword = findViewById(R.id.etpassword);
        EditText etaddress = findViewById(R.id.etaddress);
        EditText etmobile = findViewById(R.id.etmobile);

        String fname = etfname.getText().toString();
        String lname = etlname.getText().toString();
        String password = etpassword.getText().toString();
        String address = etaddress.getText().toString();
        String mobile = etmobile.getText().toString();
        if (fname.isEmpty() || lname.isEmpty() || password.isEmpty() || address.isEmpty() || mobile.isEmpty()) {
            Toast.makeText(ActivityRegister.this, "please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }
        Auth.createUserWithEmailAndPassword(mobile, password)
                .addOnCompleteListener(ActivityRegister.this, task -> {
                    if (task.isSuccessful()) {
                        user user = new user(fname, lname, mobile, address);
                        FirebaseDatabase.getInstance("https://hospital-c4cf6-default-rtdb.firebaseio.com").getReference().child("USERS")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(user).addOnCompleteListener(task1 -> {
                                    Intent intent = new Intent(ActivityRegister.this, ActivityHome.class);
                                    startActivity(intent);
                                    finish();
                                });
                    } else {
                        Toast.makeText(ActivityRegister.this, "Authantication failed", Toast.LENGTH_LONG).show();
                    }
                });
    }
}