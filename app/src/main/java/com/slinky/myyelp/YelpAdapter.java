package com.slinky.myyelp;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.slinky.myyelp.yelp_api.YelpResponse;

import java.util.List;

public class YelpAdapter extends RecyclerView.Adapter<YelpAdapter.YelpViewHolder> {
    private List<YelpResponse.YelpBusiness> businesses;

    public YelpAdapter(List<YelpResponse.YelpBusiness> businesses) {
        this.businesses = businesses;
    }

    @Override
    public int getItemCount() {
        return businesses != null ? businesses.size() : 0;
    }

    @NonNull
    @Override
    public YelpAdapter.YelpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull YelpAdapter.YelpViewHolder holder, int position) {

    }

    static class YelpViewHolder extends RecyclerView.ViewHolder {
        public YelpViewHolder(@NonNull ViewGroup parent) {
            super(parent);
        }
    }

}
