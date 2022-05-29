package com.example.sportssdbis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.sportssdbis.databinding.ActivityItemAddBinding;
import com.example.sportssdbis.models.ModelCategory;
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

public class ItemAddActivity extends AppCompatActivity {

    //setup view binding
    private ActivityItemAddBinding binding;

    private FirebaseAuth firebaseAuth;

    private ArrayList<ModelCategory> categoryArrayList;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        loadCategories();
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
        binding.categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryPickDialog();
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
        title = binding.titleEt.getText().toString().trim();
        location = binding.locationEt.getText().toString().trim();
        description = binding.descriptionEt.getText().toString().trim();
        category = binding.categoryTv.getText().toString().trim();
        uploadToDb();
    }
    private String title = "", location = "", description="", category="";
    private void uploadToDb() {
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        long timestamp = System.currentTimeMillis();

        String uid = firebaseAuth.getUid();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("id", timestamp);
        hashMap.put("title", title);
        hashMap.put("location", location);
        hashMap.put("description", description);
        hashMap.put("category", category);
        hashMap.put("timestamp", timestamp);

        //set data to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Items");
        ref.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //data added to db
                        progressDialog.dismiss();
                        Toast.makeText(ItemAddActivity.this, "Item added.", Toast.LENGTH_SHORT).show();
                        //start user dashboard
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        //failed adding data to db
                        progressDialog.dismiss();
                        Toast.makeText(ItemAddActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void loadCategories(){
       // log.d(TAG, "loadCategories: Loading categories...");
        categoryArrayList = new ArrayList<>();
        //get all categories from db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void categoryPickDialog() {

        String[] categoriesArray = new String[categoryArrayList.size()];
        for(int i=0; i<categoryArrayList.size(); i++){
            categoriesArray[i] = categoryArrayList.get(i).getCategory();

        }
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick category")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String category = categoriesArray[which];
                        binding.categoryTv.setText(category);
                    }
                })
                .show();
    }
}