package com.example.sportssdbis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.sportssdbis.adapters.AdapterItemAdmin;
import com.example.sportssdbis.databinding.ActivityItemListAdminBinding;
import com.example.sportssdbis.models.ModelItem;

import java.util.ArrayList;

public class ItemListAdminActivity extends AppCompatActivity {

    private ActivityItemListAdminBinding binding;

    private ArrayList<ModelItem> itemArrayList;

    private AdapterItemAdmin adapterItemAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemListAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}