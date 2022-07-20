package com.example.nivala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.nivala.databinding.ActivityDetailsBinding;
import com.example.nivala.model.GiveDataModel;

import java.io.File;
import java.io.IOException;

public class Details extends AppCompatActivity {
    private ActivityDetailsBinding binding;
    private Intent intent;
    private String imageFileName = "";
    private File storageDir;
    private GiveDataModel model;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intent = getIntent();
        if (intent != null) {
            imageFileName = intent.getStringExtra("imageFilename");
            imageUri = Uri.parse(intent.getStringExtra("imageUri"));
        }
        storageDir = this.getExternalFilesDir("Pictures");  //external sd card
        storageDir = new File(storageDir, imageFileName);
        if (!storageDir.exists())
            storageDir.mkdirs();

        Log.v("Image", "Image_details: " + imageFileName);
        Log.v("Image", "Image_File_detials: " + storageDir.getAbsolutePath());

        binding.capturedImageView.setImageURI(imageUri); // here image will be displayed in the imageview
/*
        Glide.with(this)
                .load(storageDir)
                .centerCrop()
                .into(binding.capturedImageView);
*/

        binding.submitBtn.setOnClickListener(v -> {
            model = new GiveDataModel();
            model.setFoodItem(binding.titleEditText.getText().toString());
            model.setQuantity(binding.quantityEt.getText().toString());

            int position = binding.radioGroup.getCheckedRadioButtonId();
            if (position == 0)
                model.setFoodType("HomeMade");
            else if (position == 0)
                model.setFoodType("Packaged");
            model.setExpiry(binding.expiryEditText.getText().toString());
            model.setPickupAddress(binding.addressEditText.getText().toString());
            model.setPickupDate(binding.pickupDateEditText.getText().toString());
            model.setPickupTime(binding.pickupTimeEditText.getText().toString());
            model.setPhone(binding.phoneEditText.getText().toString());
            model.setPolicy(binding.checkBox.getText().toString());

            Intent intent = new Intent(Details.this, MainActivity.class);
            startActivity(intent);
        });
    }
}