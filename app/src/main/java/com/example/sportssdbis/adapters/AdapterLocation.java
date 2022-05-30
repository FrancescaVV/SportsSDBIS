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

import com.example.sportssdbis.filters.FilterLocation;
import com.example.sportssdbis.models.ModelLocation;
import com.example.sportssdbis.databinding.RowCategoryBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterLocation extends RecyclerView.Adapter<AdapterLocation.HolderLocation> implements Filterable {

    private Context context;
    public ArrayList<ModelLocation> locationArrayList, filterList;

    //view binding
    private RowCategoryBinding binding;

    private FilterLocation filter;

    public AdapterLocation(Context context, ArrayList<ModelLocation> locationArrayList){
        this.context = context;
        this.locationArrayList = locationArrayList;
        this.filterList = locationArrayList;
    }

    @NonNull
    @Override
    public HolderLocation onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //bind row_category.xml

        binding = RowCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderLocation(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderLocation holder, int position) {

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
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //alert to confirm
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to DELETE this location?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(context, "Deleting...", Toast.LENGTH_SHORT).show();
                                deleteLocation(model, holder);
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

        /*
        //item click, go to item page
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemListAdminActivity.class);
                intent.putExtra("categoryId", id);
                intent.putExtra("categoryTitle", category);
                context.startActivity(intent);
            }
        });

         */
    }

    private void deleteLocation(ModelLocation model, HolderLocation holder) {
        String id = model.getId();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
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
        return locationArrayList.size() ;
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new FilterLocation(filterList, this);
        }
        return filter;
    }

    class HolderLocation extends RecyclerView.ViewHolder{
        TextView titleTv, descriptionTv, locationTv, scheduleTv;
        ImageButton deleteBtn;
        public HolderLocation(@NonNull View itemView){
            super(itemView);

            //init ui views
            titleTv = binding.titleTv;
            locationTv = binding.locationTv;
            scheduleTv = binding.scheduleTv;
            descriptionTv = binding.descriptionTv;
            deleteBtn = binding.deleteBtn;
        }
    }


}
