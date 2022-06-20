package com.slinky.myyelp.logic;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.slinky.myyelp.R;
import com.slinky.myyelp.database.DatabaseLogic;
import com.slinky.myyelp.database.Delete;
import com.slinky.myyelp.database.Insert;
import com.slinky.myyelp.database.LocalYelpDatabase;
import com.slinky.myyelp.databinding.ListItemBinding;
import com.slinky.myyelp.yelp_api.YelpResponse;

import java.util.List;
import java.util.Objects;

public class YelpAdapter extends RecyclerView.Adapter<YelpAdapter.YelpViewHolder> {
    private static final String TAG = YelpAdapter.class.getSimpleName();
    private List<YelpResponse.YelpBusiness> businesses;
    private boolean isFavorites;
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

    public void setIsFavorites(boolean favorites) {
        isFavorites = favorites;
    }

    @NonNull
    @Override
    public YelpAdapter.YelpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.list_item, parent, false);
        return new YelpViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull YelpAdapter.YelpViewHolder holder, int position) {
        if (!isFavorites) {
            onBindSetElements(holder, position);
        } else {
            onBindSetElementsFavorites(holder, position);
        }
        holder.binding.getRoot().setOnClickListener(v -> listenerLogic(holder, position));
    }

    /**
     * listenerLogic
     */
    private void listenerLogic(YelpAdapter.YelpViewHolder holder, int position ) {
        Log.d(TAG, "listenerLogic: insert ");
        yelpRepo.setContext(holder.binding.getRoot().getContext()); // to solve the context problem

        LocalYelpDatabase database = LocalYelpDatabase.getInstance(holder.binding.getRoot().getContext());
        AlertDialog.Builder builder; // to solve the context problem
        DatabaseLogic dbl = new Insert(database);

        builder = yelpRepo.askUserIfAddToDatabase(businesses.get(position), dbl);

        if (!businesses.get(position).isFavorite) {
            builder.show();
        } else {
            favoriteListenerLogic(position, database);
        }
    }

    private void favoriteListenerLogic(int position, LocalYelpDatabase database) {
        Log.d(TAG, "favoriteListenerLogic: delete");

        AlertDialog.Builder builder;
        DatabaseLogic dbl = new Delete(database);

        builder = yelpRepo.askRemoveFromDB(this, businesses.get(position), position, dbl);
        builder.show();


    }

    /**
     * class to bind the data to the view
     */
    static class YelpViewHolder extends RecyclerView.ViewHolder {
        private final ListItemBinding binding;

        public YelpViewHolder(@NonNull ListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    /**
     * This method is used to set the elements of the view holder
     * @param holder the view holder
     * @param position the position of the element in the list
     */
    protected void onBindSetElements(@NonNull YelpAdapter.YelpViewHolder holder, int position) {
        ListItemBinding bind = holder.binding;
        bind.addressTV.setText(businesses.get(position).location.toString());
        bind.categoryTV.setText(businesses.get(position).categoryToString());
        onBindSetCombinedLogic(holder, position);
    }

    /**
     * This method is used to set the favorite logic for the list items
     * needed to set custom logic for the favorites list.
     * @param holder the view holder
     * @param position the position of the element in the list
     */
    protected void onBindSetElementsFavorites(@NonNull YelpAdapter.YelpViewHolder holder, int position) {
        ListItemBinding bind = holder.binding;
        bind.addressTV.setText(businesses.get(position).location.customAddress);
        bind.categoryTV.setText(businesses.get(position).customCategory);
        onBindSetCombinedLogic(holder, position);
    }

    /**
     * This method is used to set the favorite logic for the recycler view
     * @param holder the view holder
     * @param position the position of the element in the list
     */
    public void onBindSetCombinedLogic(@NonNull YelpAdapter.YelpViewHolder holder, int position) {
        ListItemBinding bind = holder.binding;
        bind.nameTV.setText(businesses.get(position).name);
        bind.ratingRB.setRating(businesses.get(position).rating);

        if (!Objects.equals(businesses.get(position).displayPhone, "")) { // if displayPhone is not empty
            bind.phoneTV.setText(businesses.get(position).displayPhone);
        } else {
            bind.phoneTV.setText(R.string.noPhone);
        }

        bind.priceTV.setText(businesses.get(position).price);
        Glide.with(holder.itemView)
                .load(businesses.get(position).imageUrl)
                .into(bind.restaurantIV);
    }

}
