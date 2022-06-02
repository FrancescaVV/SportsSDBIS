package com.example.sportssdbis.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.sportssdbis.BookingActivity;
import com.example.sportssdbis.databinding.RowBookingBinding;
import com.example.sportssdbis.databinding.RowLocationUserBinding;
import com.example.sportssdbis.filters.FilterBooking;
import com.example.sportssdbis.filters.FilterLocationUser;
import com.example.sportssdbis.models.ModelBooking;
import com.example.sportssdbis.models.ModelLocation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterBooking extends RecyclerView.Adapter<AdapterBooking.HolderBooking> implements Filterable {

    private Context context;
    public ArrayList<ModelBooking> bookingArrayList, filterList;

    //view binding
    private RowBookingBinding binding;

    private FilterBooking filter;

    public AdapterBooking(Context context, ArrayList<ModelBooking> bookingArrayList){
        this.context = context;
        this.bookingArrayList = bookingArrayList;
        this.filterList = bookingArrayList;
    }

    @NonNull
    @Override
    public AdapterBooking.HolderBooking onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //bind row_category.xml

        binding = RowBookingBinding.inflate(LayoutInflater.from(context), parent, false);
        return new AdapterBooking.HolderBooking(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBooking.HolderBooking holder, int position) {

        //get data
        ModelBooking model = bookingArrayList.get(position);
        String id = model.getId();
        String time = model.getTime();
        String location = model.getLocation();
        String date = model.getDate();
        String uid = model.getUid();
        long timestamp = model.getTimestamp();

        //set data

        holder.timeTv.setText(time);
        holder.dateTv.setText(date);
        holder.bookingTv.setText(location);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //alert to confirm
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to DELETE this booking?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(context, "Deleting...", Toast.LENGTH_SHORT).show();
                                deleteBooking(model, holder);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

    }

    private void deleteBooking(ModelBooking model, AdapterBooking.HolderBooking holder) {
        String id = model.getId();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Bookings");
        ref.child(id)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Deleted successfully.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public int getItemCount() {
        return bookingArrayList.size() ;
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new FilterBooking(filterList, this);
        }
        return filter;
    }

    class HolderBooking extends RecyclerView.ViewHolder{
        TextView timeTv, dateTv, bookingTv;
        ImageButton backBtn;
        ImageButton deleteBtn;
        public HolderBooking(@NonNull View itemView){
            super(itemView);

            //init ui views
            timeTv = binding.timeTv;
            bookingTv = binding.bookingTv;
            dateTv = binding.dateTv;
            deleteBtn = binding.deleteBtn;
        }
    }
}
