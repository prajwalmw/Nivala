package com.example.nivala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.example.nivala.databinding.ActivityDetailsBinding;
import com.example.nivala.model.GiveDataModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Details extends AppCompatActivity {
    private ActivityDetailsBinding binding;
    private Intent intent;
    private String imageFileName = "";
    private File storageDir;
    private GiveDataModel model;
    private Uri imageUri;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private String timeStamp;
    private StorageReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        intent = getIntent();
        if (intent != null) {
            imageFileName = intent.getStringExtra("imageFilename");
            imageUri = Uri.parse(intent.getStringExtra("imageUri"));
            timeStamp = intent.getStringExtra("timeStamp");

            storage = FirebaseStorage.getInstance();
            reference = storage.getReference().child("Food").child(timeStamp+"");
        }

        storageDir = this.getExternalFilesDir("Pictures");  //external sd card
        storageDir = new File(storageDir, imageFileName);
        if (!storageDir.exists())
            storageDir.mkdirs();

        Log.v("Image", "Image_details: " + imageFileName);
        Log.v("Image", "Image_File_detials: " + storageDir.getAbsolutePath());

        binding.capturedImageView.setImageURI(imageUri); // here image will be displayed in the imageview

        binding.submitBtn.setOnClickListener(v -> {
            model = new GiveDataModel();
            model.setFoodItem(binding.titleEditText.getText().toString());
            model.setQuantity(binding.quantityEt.getText().toString());
            int radioButtonID = binding.radioGroup.getCheckedRadioButtonId();
            if (radioButtonID == binding.homemade.getId())
                model.setFoodType("HomeMade");
            else if (radioButtonID == binding.packaged.getId())
                model.setFoodType("Packaged");
            Log.e("details", "position: "+ model.getFoodType());
            model.setExpiry(binding.expiryEditText.getText().toString());
            model.setCity(binding.cityEditText.getText().toString());
            model.setPickupAddress(binding.addressEditText.getText().toString());
            model.setPickupDate(binding.pickupDateEditText.getText().toString());
            model.setPickupTime(binding.pickupTimeEditText.getText().toString());
            model.setPhone(binding.phoneEditText.getText().toString());
            model.setPolicy(binding.checkBox.getText().toString());

            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // do something
                    imageUri = uri;
                    model.setImageUri(String.valueOf(imageUri));
                    Log.v("ImageUri", "uri_downlaod: " + imageUri);
                    Log.v("ImageUri", "image_dbPush: " + imageUri);
                    database.getReference().child("Food-Post")
                            .child(timeStamp)
                            .setValue(model);
                }
            });

            Intent intent = new Intent(Details.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}