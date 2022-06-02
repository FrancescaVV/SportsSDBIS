package com.example.sportssdbis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.sportssdbis.adapters.AdapterBooking;
import com.example.sportssdbis.adapters.AdapterLocationUser;
import com.example.sportssdbis.databinding.ActivityDashboardBookingBinding;
import com.example.sportssdbis.databinding.ActivityDashboardUserBinding;
import com.example.sportssdbis.models.ModelBooking;
import com.example.sportssdbis.models.ModelLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardBooking extends AppCompatActivity {

    //view binding
    private ActivityDashboardBookingBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    private ArrayList<ModelBooking> bookingArrayList;
    private AdapterBooking adapterBooking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        loadBookings();

        //edit text search
        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                try {
                    adapterBooking.getFilter().filter(s);
                }
                catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable e) {

            }
        });

        //logout
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });

        //handle click, back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void loadBookings(){
        bookingArrayList = new ArrayList<>();
        //get all categories from db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Bookings");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clear
                bookingArrayList.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    //get data
                    ModelBooking model = ds.getValue(ModelBooking.class);

                    //add to array list
                    bookingArrayList.add(model);
                }

                adapterBooking = new AdapterBooking(DashboardBooking.this, bookingArrayList);

                binding.categoriesRv.setAdapter(adapterBooking);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUser() {
        //get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            //not logged in, go to main screen
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else {
            //logged in, get info
            String email = firebaseUser.getEmail();
            //set in textview toolbar
            binding.subTitleTv.setText(email);
        }
    }
}