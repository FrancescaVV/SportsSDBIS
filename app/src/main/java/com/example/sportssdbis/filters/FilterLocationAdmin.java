package com.example.sportssdbis.filters;

import android.widget.Filter;

import com.example.sportssdbis.adapters.AdapterLocationAdmin;
import com.example.sportssdbis.models.ModelLocation;

import java.util.ArrayList;

public class FilterLocationAdmin extends Filter {

    ArrayList<ModelLocation> filterList;

    AdapterLocationAdmin adapterLocationAdmin;

    public FilterLocationAdmin(ArrayList<ModelLocation> filterList, AdapterLocationAdmin adapterLocationAdmin) {
        this.filterList = filterList;
        this.adapterLocationAdmin = adapterLocationAdmin;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if(constraint != null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();
            ArrayList<ModelLocation> filteredModels = new ArrayList<>();

            for(int i = 0; i< filterList.size(); i++){
                if(filterList.get(i).getTitle().toUpperCase().contains(constraint)){
                    filteredModels.add(filterList.get(i));
                }
            }

            results.count = filteredModels.size();
            results.values = filteredModels;
        }
        else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapterLocationAdmin.locationArrayList = (ArrayList<ModelLocation>)results.values;
        adapterLocationAdmin.notifyDataSetChanged();
    }
}
