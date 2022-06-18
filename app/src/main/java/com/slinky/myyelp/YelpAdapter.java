package com.slinky.myyelp;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.slinky.myyelp.databinding.ListItemBinding;
import com.slinky.myyelp.yelp_api.YelpResponse;

import java.util.List;
//TODO implement it like they did in the repo
public class YelpAdapter extends RecyclerView.Adapter<YelpAdapter.YelpViewHolder> {
    private List<YelpResponse.YelpBusiness> businesses;

    public YelpAdapter() {
    }
   public void setBusinessesList(List<YelpResponse.YelpBusiness> businesses) {
        this.businesses = businesses;
    }

    @Override
    public int getItemCount() {
        return businesses != null ? businesses.size() : 0;
    }

    @NonNull
    @Override
    public YelpAdapter.YelpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // implement view holder here to bind data to the view
        ListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.list_item, parent, false);
        return new YelpViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull YelpAdapter.YelpViewHolder holder, int position) {
        holder.binding.setRestaurant(businesses.get(position));
        holder.binding.setLocation(businesses.get(position).location);
        // set image from imageUrl with Glide
        Glide.with(holder.binding.getRoot().getContext())
                .load(businesses.get(position).imageUrl)
                .into(holder.binding.imageView3);

        holder.binding.getRoot().setOnClickListener(v -> {
            // TODO implement SQLite database to save the object into favorite list
            Toast.makeText(v.getContext(), "Clicked", Toast.LENGTH_SHORT).show();
        });
    }

    static class YelpViewHolder extends RecyclerView.ViewHolder {
        private ListItemBinding binding;

        public YelpViewHolder(@NonNull ListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
