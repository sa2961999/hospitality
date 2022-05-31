package com.example.hospital;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.example.hospital.databinding.ActivityHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements CurrentLocationPicker.OnLocationPicked, DatePickerDialog.OnDateSetListener {

    private ActivityHomeBinding binding;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.ivAmbulance.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(
                        HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, 455
                );
            }

            new CurrentLocationPicker(HomeActivity.this, this);
        });

        binding.ivXray.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, RaysActivity.class)));

        binding.ivInensiveCare.setOnClickListener(view -> showDatePickerDialog());

        binding.ivAbout.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, AboutActivity.class)));
    }

    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void sendLocation(Double latitude, Double longitude) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("ambulance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() < 3) {
                    HashMap<String, Double> map = new HashMap();
                    map.put("latitude", latitude);
                    map.put("longitude", longitude);

                    dataSnapshot.child(String.valueOf(dataSnapshot.getChildrenCount())).getRef().setValue(map);


                    printMessage("Done");
                } else {
                    printMessage("not available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                printMessage("Error");
            }
        });
    }

    private void sendDate(int year, int month, int day) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("Intensive Care").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() < 3) {
                    HashMap<String, Integer> map = new HashMap();
                    map.put("year", year);
                    map.put("month", month);
                    map.put("day", day);

                    dataSnapshot.child(String.valueOf(dataSnapshot.getChildrenCount())).getRef().setValue(map);

                    printMessage("Done");
                } else {
                    printMessage("not available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                printMessage("Error");
            }
        });
    }

    private void printMessage(String message) {
        TextView tvMessage;
        LayoutInflater inflater = (LayoutInflater) getSystemService(HomeActivity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_message, null);
        tvMessage = layout.findViewById(R.id.message);

        if (toast != null) {
            toast.cancel();
        }

        tvMessage.setText(message);

        toast = new Toast(HomeActivity.this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public void locationPicked(Location location) {
        sendLocation(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        sendDate(year, month, dayOfMonth);
    }
}