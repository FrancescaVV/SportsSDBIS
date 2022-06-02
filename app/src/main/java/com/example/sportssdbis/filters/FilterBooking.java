package com.example.sportssdbis.filters;

import android.widget.Filter;

import com.example.sportssdbis.adapters.AdapterBooking;
import com.example.sportssdbis.models.ModelBooking;
import com.example.sportssdbis.models.ModelLocation;

import java.util.ArrayList;

public class FilterBooking extends Filter{

    ArrayList<ModelBooking> filterList;

    AdapterBooking adapterBooking;

    public FilterBooking(ArrayList<ModelBooking> filterList, AdapterBooking adapterBooking) {
        this.filterList = filterList;
        this.adapterBooking = adapterBooking;
    }


    @Override
    protected Filter.FilterResults performFiltering(CharSequence constraint) {
        Filter.FilterResults results = new Filter.FilterResults();
        if(constraint != null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();
            ArrayList<ModelBooking> filteredModels = new ArrayList<>();

            for(int i = 0; i< filterList.size(); i++){
                if(filterList.get(i).getLocation().toUpperCase().contains(constraint)){
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
        adapterBooking.bookingArrayList = (ArrayList<ModelBooking>)results.values;
        adapterBooking.notifyDataSetChanged();
    }
}
