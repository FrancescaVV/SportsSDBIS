package com.example.sportssdbis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.sportssdbis.adapters.AdapterCategory;
import com.example.sportssdbis.databinding.ActivityDashboardAdminBinding;
import com.example.sportssdbis.models.ModelCategory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardAdminActivity extends AppCompatActivity {

    //view binding
    private ActivityDashboardAdminBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    private ArrayList<ModelCategory> categoryArrayList;
    private AdapterCategory adapterCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        loadCategories();

        //edit text search
        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                try {
                    adapterCategory.getFilter().filter(s);
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

        //handle click, start adding categories
        binding.addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardAdminActivity.this, CategoryAddActivity.class));
            }
        });

        //start add item info screen
        binding.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardAdminActivity.this, ItemAddActivity.class));
            }
        });
    }

    private void loadCategories(){
        categoryArrayList = new ArrayList<>();
        //get all categories from db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clear
                categoryArrayList.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    //get data
                    ModelCategory model = ds.getValue(ModelCategory.class);

                    //add to array list
                    categoryArrayList.add(model);
                }

                adapterCategory = new AdapterCategory(DashboardAdminActivity.this, categoryArrayList);

                binding.categoriesRv.setAdapter(adapterCategory);
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