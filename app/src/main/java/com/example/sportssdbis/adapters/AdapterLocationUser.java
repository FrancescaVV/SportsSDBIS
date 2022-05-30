package com.example.sportssdbis.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportssdbis.databinding.RowLocationBinding;
import com.example.sportssdbis.databinding.RowLocationUserBinding;
import com.example.sportssdbis.filters.FilterLocationUser;
import com.example.sportssdbis.models.ModelLocation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterLocationUser extends RecyclerView.Adapter<AdapterLocationUser.HolderLocation> implements Filterable {


    private Context context;
    public ArrayList<ModelLocation> locationArrayList, filterList;

    //view binding
    private RowLocationUserBinding binding;

    private FilterLocationUser filter;

    public AdapterLocationUser(Context context, ArrayList<ModelLocation> locationArrayList){
        this.context = context;
        this.locationArrayList = locationArrayList;
        this.filterList = locationArrayList;
    }

    @NonNull
    @Override
    public AdapterLocationUser.HolderLocation onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //bind row_category.xml

        binding = RowLocationUserBinding.inflate(LayoutInflater.from(context), parent, false);
        return new AdapterLocationUser.HolderLocation(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterLocationUser.HolderLocation holder, int position) {

        //get data
        ModelLocation model = locationArrayList.get(position);
        String id = model.getId();
        String title = model.getTitle();
        String location = model.getLocation();
        String schedule = model.getSchedule();
        String description = model.getDescription();
        String uid = model.getUid();
        long timestamp = model.getTimestamp();

        //set data

        holder.titleTv.setText(title);
        holder.descriptionTv.setText(description);
        holder.scheduleTv.setText(schedule);
        holder.locationTv.setText(location);

    }


    @Override
    public int getItemCount() {
        return locationArrayList.size() ;
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new FilterLocationUser(filterList, this);
        }
        return filter;
    }

    class HolderLocation extends RecyclerView.ViewHolder{
        TextView titleTv, descriptionTv, locationTv, scheduleTv;
        ImageButton bookBtn;
        public HolderLocation(@NonNull View itemView){
            super(itemView);

            //init ui views
            titleTv = binding.titleTv;
            locationTv = binding.locationTv;
            scheduleTv = binding.scheduleTv;
            descriptionTv = binding.descriptionTv;
            bookBtn = binding.bookBtn;
        }
    }
}
