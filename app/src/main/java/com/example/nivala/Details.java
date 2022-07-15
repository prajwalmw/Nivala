package com.example.nivala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.nivala.databinding.ActivityDetailsBinding;

import java.io.File;
import java.io.IOException;

public class Details extends AppCompatActivity {
    private ActivityDetailsBinding binding;
    private Intent intent;
    private String imageFileName = "";
    private File storageDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intent = getIntent();
        if (intent != null)
            imageFileName = intent.getStringExtra("imageFilename");
        storageDir = this.getExternalFilesDir("Pictures");  //external sd card
        storageDir = new File(storageDir, imageFileName);
        if (!storageDir.exists())
            storageDir.mkdirs();

        Log.v("Image", "Image_details: " + imageFileName);
        Log.v("Image", "Image_File_detials: " + storageDir.getAbsolutePath());

        Glide.with(this)
                .load(storageDir)
                .centerCrop()
                .into(binding.capturedImageView);
    }
}