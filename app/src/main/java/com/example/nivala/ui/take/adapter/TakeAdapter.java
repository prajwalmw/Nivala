package com.example.nivala.ui.take.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nivala.R;
import com.example.nivala.databinding.TakeListitemBinding;
import com.example.nivala.model.GiveDataModel;

import java.util.Collections;
import java.util.List;

/**
* Created by Prajwal Maruti Waingankar on 29-04-2022, 11:56
* Copyright (c) 2021 . All rights reserved.
* Email: prajwalwaingankar@gmail.com
* Github: prajwalmw
*/

public class TakeAdapter extends RecyclerView.Adapter<TakeAdapter.TakeViewHolder> {
    private TakeListitemBinding binding;
    private List<GiveDataModel> model;
    Context context;

    public TakeAdapter(FragmentActivity activity, List<GiveDataModel> model) {
        // do something
        this.model = model;
        this.context = activity;
    }

    @NonNull
    @Override
    public TakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.take_listitem, parent, false);
        return new TakeViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull TakeViewHolder holder, int position) {
        GiveDataModel giveDataModel = model.get(position);
        Glide.with(context)
                .load(giveDataModel.getImageUri())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.image);
        holder.address.setText(giveDataModel.getPickupAddress());
        holder.expiryDateTime.setText(giveDataModel.getExpiry());
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class TakeViewHolder extends RecyclerView.ViewHolder {
        TextView address, expiryDateTime;
        ImageView image;
        CardView cardviewPost;

        public TakeViewHolder(@NonNull View itemView) {
            super(itemView);

            address = itemView.findViewById(R.id.address);
            expiryDateTime = itemView.findViewById(R.id.expiryDateTime);
            image = itemView.findViewById(R.id.image);
            cardviewPost = itemView.findViewById(R.id.cardviewPost);
        }
    }
}
