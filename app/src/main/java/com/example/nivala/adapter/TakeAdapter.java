package com.example.nivala.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nivala.R;
import com.example.nivala.databinding.TakeListitemBinding;
import com.example.nivala.model.GiveDataModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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
        Log.v("uri", "image_uri: " + giveDataModel.getImageUri());
        Glide.with(context)
                .load(giveDataModel.getImageUri())
                .placeholder(R.drawable.blur_image)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.image);

        holder.address.setText(giveDataModel.getState().trim() + ", " + giveDataModel.getCity());
        holder.expiryDateTime.setText(giveDataModel.getExpiry());

        holder.cardviewPost.setOnClickListener( v-> {
            // clicking on this will open a Dialog with info.
            MaterialAlertDialogBuilder alertdialogBuilder = new MaterialAlertDialogBuilder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_info, null);
            alertdialogBuilder.setView(view);
            TextView fitem = view.findViewById(R.id.fooditem_value);
            TextView ftype = view.findViewById(R.id.foodType_value);
            TextView quan = view.findViewById(R.id.quan_value);
            TextView phone = view.findViewById(R.id.phone_value);
            TextView addr = view.findViewById(R.id.paddr_value);
            TextView date = view.findViewById(R.id.pdate_value);
            TextView time = view.findViewById(R.id.ptime_value);

            fitem.setText(giveDataModel.getFoodItem());
            ftype.setText(giveDataModel.getFoodType());
            quan.setText(giveDataModel.getQuantity());
            phone.setText(giveDataModel.getPhone());
            addr.setText(giveDataModel.getState() + ", " + giveDataModel.getCity() + ", " + giveDataModel.getPickupAddress());
            date.setText(giveDataModel.getPickupDate());
            time.setText(giveDataModel.getPickupTime());


//            alertdialogBuilder.setMessage(
//                    "Food Item(s): " + giveDataModel.getFoodItem() + "\n" +
//                    "Food Type: " + giveDataModel.getFoodType() + "\n" +
//                    "Quantity: " + giveDataModel.getQuantity() + "\n" +
//                    "Phone no: " + giveDataModel.getPhone() + "\n" +
//                    "Pickup Address: " + giveDataModel.getPickupAddress() + "\n" +
//                    "Pickup Date: " + giveDataModel.getPickupDate() + "\n" +
//                    "Pickup Time: " + giveDataModel.getPickupTime()
//                    ); // add extra info here.
            alertdialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
//            alertdialogBuilder.setNegativeButton(R.string.generic_no, null);
            AlertDialog alertDialog = alertdialogBuilder.create();
            alertDialog.show();
            Button positiveButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
            positiveButton.setTextColor(context.getColor(R.color.greenDark));
            negativeButton.setTextColor(context.getColor(R.color.greenDark));
        });
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
