package com.example.hospital;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.hospital.databinding.ActivityRegisterBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth Auth;
    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        //initialize firebase auth
        Auth = FirebaseAuth.getInstance();
        if (Auth.getCurrentUser() != null) {
            Auth.signOut();
        }

        binding.bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        binding.tvsignin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
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
            Toast.makeText(RegisterActivity.this, "please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }
        Auth.createUserWithEmailAndPassword(mobile, password)
                .addOnCompleteListener(RegisterActivity.this, task -> {
                    if (task.isSuccessful()) {
                        user user = new user(fname, lname, mobile, address);
                        FirebaseDatabase.getInstance("https://hospital-c4cf6-default-rtdb.firebaseio.com").getReference().child("USERS")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(user).addOnCompleteListener(task1 -> {
                                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                });
                    } else {
                        Snackbar snackbar = Snackbar.make(binding.getRoot(), Objects.requireNonNull(task.getException().getMessage()), Snackbar.LENGTH_LONG);
                        snackbar.setTextColor(getResources().getColor(R.color.red));
                        snackbar.show();
                    }
                });
    }
}