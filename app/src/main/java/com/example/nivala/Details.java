package com.example.nivala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.nivala.databinding.ActivityDetailsBinding;
import com.example.nivala.model.GiveDataModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

        binding.pickupDateEditText.setOnClickListener(v -> {    // Pickup Date
            datePickerDialog("Select pickup date", binding.pickupDateEditText);
        });

        binding.pickupTimeEditText.setOnClickListener(v -> {    // Time Picker
            timePickerDialog("Select pickup time", binding.pickupTimeEditText);
        });

        binding.minus.setOnClickListener(v -> {
            String quantity = binding.quantityEt.getText().toString();
            int value = Integer.valueOf(quantity) - 10;
            if(value > 10 && value < 500)
                binding.quantityEt.setText(String.valueOf(value));
        });
        binding.add.setOnClickListener(v -> {
            String quantity = binding.quantityEt.getText().toString();
            int value = Integer.valueOf(quantity) + 10;
            if(value > 10 && value < 500)
            binding.quantityEt.setText(String.valueOf(value));
        });


        binding.expiryEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioButtonID = binding.radioGroup.getCheckedRadioButtonId();
                if (radioButtonID == binding.homemade.getId()) {
                    timePickerDialog("Select time of expiry", binding.expiryEditText);
                }
                else if (radioButtonID == binding.packaged.getId()) {
                    datePickerDialog("Select date of expiry", binding.expiryEditText);
                }
            }
        });

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
            model.setState(binding.stateEditText.getText().toString());
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

    private void datePickerDialog(String title, EditText editText) {
        Calendar calendar = Calendar.getInstance();
        MaterialDatePicker datePicker = MaterialDatePicker.Builder
                .datePicker()
                .setTitleText(title)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(new CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointForward.now()) // dates selectable only from current date so as to avoid past dates selection.
                        .build())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            calendar.setTimeInMillis((Long) selection);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy");
            String date = simpleDateFormat.format(calendar.getTime());
            editText.setText(date);
            Log.v("Image", "date: " + date);
        });

        datePicker.show(getSupportFragmentManager(), datePicker.toString());
    }


    private void timePickerDialog(String title, EditText editText) {
        Calendar c = Calendar.getInstance();
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTitleText(title)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(c.getTime().getHours())
                .setMinute(c.getTime().getMinutes())
                .build();

        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText(timePicker.getHour() + ":" + timePicker.getMinute());
            }
        });

        timePicker.show(getSupportFragmentManager(), timePicker.toString());
    }

}