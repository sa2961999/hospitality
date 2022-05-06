package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {
private FirebaseAuth Auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        //initialize firebase auth
        Auth=FirebaseAuth.getInstance();
        if (Auth.getCurrentUser()!=null){
            finish();
            return;
        }
        Button bregister=findViewById(R.id.bregister);
        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }




            private void registerUser() {
                EditText etfname=findViewById(R.id.etfname);
                EditText etlname=findViewById(R.id.etlname);
                EditText etpassword=findViewById(R.id.etpassword);
                EditText etaddress=findViewById(R.id.etaddress);
                EditText etmobile=findViewById(R.id.etmobile);

                String fname=etfname.getText().toString();
                String lname=etlname.getText().toString();
                String password=etpassword.getText().toString();
                String address=etaddress.getText().toString();
                String mobile=etmobile.getText().toString();
                if (fname.isEmpty()||lname.isEmpty()||password.isEmpty()||address.isEmpty()||mobile.isEmpty()){
                    Toast.makeText(register.this,"please fill all fields",Toast.LENGTH_LONG).show();
                    return;
                }
                Auth.createUserWithEmailAndPassword(mobile, password)
                        .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                  user  user =new user(fname, lname, address, mobile);
                                    FirebaseDatabase.getInstance().getReference("USERS")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                           showMainActivity();
                                        }


                                    });
                                } else {
                                  Toast.makeText(register.this,"Authantication failed",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
        TextView tvsignin=findViewById(R.id.tvsignin);
        tvsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(register.this,login.class);
                startActivity(intent);
            }
        });
    } private void showMainActivity() {
    }
}