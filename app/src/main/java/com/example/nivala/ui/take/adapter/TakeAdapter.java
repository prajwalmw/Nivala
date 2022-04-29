package com.example.nivala.ui.take.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nivala.R;
import com.example.nivala.databinding.TakeListitemBinding;

/**
* Created by Prajwal Maruti Waingankar on 29-04-2022, 11:56
* Copyright (c) 2021 . All rights reserved.
* Email: prajwalwaingankar@gmail.com
* Github: prajwalmw
*/

public class TakeAdapter extends RecyclerView.Adapter<TakeAdapter.TakeViewHolder> {
    TakeListitemBinding binding;

    public TakeAdapter() {
        // do something
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

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class TakeViewHolder extends RecyclerView.ViewHolder {

        public TakeViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
