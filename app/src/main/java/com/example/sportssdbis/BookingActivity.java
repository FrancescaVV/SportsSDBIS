package com.example.sportssdbis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.sportssdbis.databinding.ActivityBookingBinding;
import com.example.sportssdbis.models.ModelLocation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BookingActivity extends AppCompatActivity {


    //setup view binding
    private ActivityBookingBinding binding;

    private FirebaseAuth firebaseAuth;

    private ArrayList<ModelLocation> locationArrayList;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        loadLocations();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        //go to previous activity
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //pick category
        binding.locationTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationPickDialog();
            }
        });

        //click upload
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void validateData() {
        //to add validation here
        date = binding.dateEt.getText().toString().trim();
        hour = binding.hourEt.getText().toString().trim();
        location = binding.locationTv.getText().toString().trim();
        uploadToDb();
    }
    private String date = "", hour = "", location="";
    private void uploadToDb() {
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        long timestamp = System.currentTimeMillis();

        //String uid = firebaseAuth.getUid();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", ""+firebaseAuth.getUid());
        hashMap.put("id", ""+timestamp);
        hashMap.put("location", location);
        hashMap.put("date", date);
        hashMap.put("hour", hour);
        hashMap.put("timestamp", timestamp);

        //set data to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Bookings");
        //the id is timestamp!!!
        ref.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //data added to db
                        progressDialog.dismiss();
                        Toast.makeText(BookingActivity.this, "Booking confirmed!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        //failed adding data to db
                        progressDialog.dismiss();
                        Toast.makeText(BookingActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void loadLocations(){
        locationArrayList = new ArrayList<>();
        //get all categories from db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Locations");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clear
                locationArrayList.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    //get data
                    ModelLocation model = ds.getValue(ModelLocation.class);

                    //add to array list
                    locationArrayList.add(model);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void locationPickDialog() {

        String[] locationsArray = new String[locationArrayList.size()];
        for(int i = 0; i< locationArrayList.size(); i++){
            locationsArray[i] = locationArrayList.get(i).getTitle();

        }
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick location")
                .setItems(locationsArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String location = locationsArray[which];
                        binding.locationTv.setText(location);
                    }
                })
                .show();
    }
}