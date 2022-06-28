package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;
import com.example.hospital.databinding.ActivityRaysBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;
import java.util.HashMap;

public class RaysActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ActivityRaysBinding binding;
    private String TYPE = "BRAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRaysBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.ivBrainRay.setOnClickListener(v -> {
            showDatePickerDialog();
            TYPE = "BRAIN";
        });

        binding.ivHeartRay.setOnClickListener(v -> {
            showDatePickerDialog();
            TYPE = "HEART";
        });

        binding.ivBonesRay.setOnClickListener(v -> {
            showDatePickerDialog();
            TYPE = "BONES";
        });


    }

    //Showing DatePickerDialog
    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    //Putting the date that the user choice on DatePickerDialog on RealtimeDatabase
    private void sendDate(int year, int month, int day) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("Rays").child(TYPE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String, Integer> map = new HashMap();
                map.put("year", year);
                map.put("month", month);
                map.put("day", day);

                dataSnapshot.child(String.valueOf(dataSnapshot.getChildrenCount())).getRef().setValue(map);

                Toast.makeText(RaysActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RaysActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        sendDate(year, month, dayOfMonth);
    }
}