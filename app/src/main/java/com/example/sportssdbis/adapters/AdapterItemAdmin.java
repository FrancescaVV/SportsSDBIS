package com.example.sportssdbis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportssdbis.databinding.RowItemAdminBinding;
import com.example.sportssdbis.models.ModelCategory;
import com.example.sportssdbis.models.ModelItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterItemAdmin extends RecyclerView.Adapter<AdapterItemAdmin.HolderItemAdmin>{

    private Context context;
    private ArrayList<ModelItem> itemArrayList;

    private RowItemAdminBinding binding;

    public AdapterItemAdmin(Context context, ArrayList<ModelItem> itemArrayList){
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public HolderItemAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowItemAdminBinding.inflate(LayoutInflater.from(context), parent, false);

        return new HolderItemAdmin(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderItemAdmin holder, int position) {
            //get data
        ModelItem model = itemArrayList.get(position);
        String title = model.getTitle();
        String location = model.getLocation();
        String description= model.getDescription();
        long timestamp = model.getTimestamp();

        //set data
        holder.titleTv.setText(title);
        holder.locationTv.setText(location);
        holder.descriptionTv.setText(description);

        //load category
        loadCategory(model, holder);
    }

    private void loadCategory(ModelItem model, HolderItemAdmin holder) {
        String category = model.getCategory();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(category)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get category
                        String category = ""+ snapshot.child("category").getValue();

                        //set category to text view
                        holder.categoryTv.setText(category);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    class HolderItemAdmin extends RecyclerView.ViewHolder{
        //ui views
        //ProgressBar progressBar;
        TextView titleTv, locationTv, descriptionTv, categoryTv;
        ImageButton moreBtn;

        public HolderItemAdmin(@NonNull View itemView){
            super(itemView);

            //init ui views
            //progressBar = binding.pro
            titleTv = binding.titleTv;
            locationTv = binding.locationTv;
            descriptionTv = binding.locationTv;
            categoryTv = binding.categoryTv;
            moreBtn = binding.moreBtn;
        }
    }

}
