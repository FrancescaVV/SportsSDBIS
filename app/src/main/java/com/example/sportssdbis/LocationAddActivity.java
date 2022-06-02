package com.example.sportssdbis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.sportssdbis.databinding.ActivityLocationAddBinding;

import java.util.HashMap;

public class LocationAddActivity extends AppCompatActivity {

    //view binding
    private ActivityLocationAddBinding binding;

    private String title = "", location = "", schedule = "", description = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //handle click, go back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.selectLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGeolocation();
            }
        });
    }

    private void validateData() {
        title = binding.categoryEt.getText().toString().trim();
        schedule = binding.categoryScheduleEt.getText().toString().trim();
        description = binding.categoryDescEt.getText().toString().trim();

        //not empty
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(schedule) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectGeolocation() {
        long timestamp = System.currentTimeMillis();

        HashMap<String, Object> locationData = new HashMap<>();
        locationData.put("id", "" + timestamp);
        locationData.put("title", "" + title);
        locationData.put("timestamp", timestamp);
        locationData.put("schedule", schedule);
        locationData.put("description", description);
        Intent intent = new Intent(this, SelectGeolocation.class);

        intent.putExtra("location", locationData);
        startActivity(intent);
    }

}