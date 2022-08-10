package com.example.nivala.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nivala.R;

public class DonateImageAdapter extends RecyclerView.Adapter<DonateImageAdapter.DonateViewHolder> {


    @NonNull
    @Override
    public DonateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.donate_image_listitem, parent, false);
        return new DonateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonateViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class DonateViewHolder extends RecyclerView.ViewHolder {

        public DonateViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
