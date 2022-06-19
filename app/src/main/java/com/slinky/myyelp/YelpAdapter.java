package com.slinky.myyelp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.slinky.myyelp.databinding.ListItemBinding;
import com.slinky.myyelp.yelp_api.YelpResponse;

import java.util.List;
import java.util.Objects;

public class YelpAdapter extends RecyclerView.Adapter<YelpAdapter.YelpViewHolder> {
    private List<YelpResponse.YelpBusiness> businesses;
    YelpRepo yelpRepo;

    public YelpAdapter(Context context) {
        yelpRepo = YelpRepo.getInstance(context);
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
        onBindSetElements(holder, position);
        //        holder.binding.setYelpBusiness(businesses.get(position));
        // gives me error in yelpResponse.java in binding implementation class for yelpBusiness

        holder.binding.getRoot().setOnClickListener(v -> {
            yelpRepo.askUserIfAddToDatabase(businesses.get(position));
        });
    }

    static class YelpViewHolder extends RecyclerView.ViewHolder {
        private final ListItemBinding binding;

        public YelpViewHolder(@NonNull ListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    protected void onBindSetElements(@NonNull YelpAdapter.YelpViewHolder holder, int position) {
        ListItemBinding bind = holder.binding;
        bind.addressTV.setText(businesses.get(position).location.toString());
        bind.nameTV.setText(businesses.get(position).name);
        bind.ratingRB.setRating(businesses.get(position).rating);
        if (!Objects.equals(businesses.get(position).displayPhone, "")) { // if displayPhone is not empty
            bind.phoneTV.setText(businesses.get(position).displayPhone);
        } else {
            bind.phoneTV.setText(R.string.noPhone);
        }
        bind.priceTV.setText(businesses.get(position).price);
        bind.categoryTV.setText(businesses.get(position).categoryToString());
        Glide.with(bind.getRoot().getContext())
                .load(businesses.get(position).imageUrl)
                .into(bind.restaurantIV);
    }

}
