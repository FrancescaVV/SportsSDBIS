package com.example.sportssdbis.filters;

import android.widget.Filter;

import com.example.sportssdbis.adapters.AdapterLocationUser;
import com.example.sportssdbis.models.ModelLocation;

import java.util.ArrayList;

public class FilterLocationUser extends Filter{

    ArrayList<ModelLocation> filterList;

    AdapterLocationUser adapterLocation;

    public FilterLocationUser(ArrayList<ModelLocation> filterList, AdapterLocationUser adapterLocation) {
        this.filterList = filterList;
        this.adapterLocation = adapterLocation;
    }


    @Override
    protected Filter.FilterResults performFiltering(CharSequence constraint) {
        Filter.FilterResults results = new Filter.FilterResults();
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
    protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
        adapterLocation.locationArrayList = (ArrayList<ModelLocation>)results.values;
        adapterLocation.notifyDataSetChanged();
    }
}
