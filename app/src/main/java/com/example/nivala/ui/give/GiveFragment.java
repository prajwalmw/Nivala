package com.example.nivala.ui.give;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.impl.PreviewConfig;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.example.nivala.Details;
import com.example.nivala.databinding.FragmentGiveBinding;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GiveFragment extends Fragment {
    private static final int REQUEST_CODE_PERMISSIONS = 100;
    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private FragmentGiveBinding binding;
    ImageCapture imageCapture = null;
    private Executor executor = Executors.newSingleThreadExecutor();
    private String imageFileName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        NotificationsViewModel notificationsViewModel =
//                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentGiveBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (checkPermission()) {
            startCamera();
        } else {
            requestPermission();
        }

        binding.captureBtn.setOnClickListener(v -> takePhoto());

        // final TextView textView = binding.textNotifications;
        //    notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }

        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this.getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                } else {
                    Toast.makeText(this.getActivity(), "Camera Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this.getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void takePhoto() {
        ImageCapture.OutputFileOptions outputFileOptions = null;
        try {
            outputFileOptions = new ImageCapture.OutputFileOptions.Builder(createImageFile()).build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Image Saved successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(GiveFragment.this.getActivity(), Details.class);
                        intent.putExtra("imageFilename", imageFileName);
                        startActivity(intent);
                        Log.v("Image", "Image: " + imageFileName);
                    }
                });
            }

            @Override
            public void onError(@NonNull ImageCaptureException error) {
                error.printStackTrace();
            }
        });
    }

    private void startCamera() {

        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this.getActivity());

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {

                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);

                } catch (ExecutionException | InterruptedException e) {
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                }
            }
        }, ContextCompat.getMainExecutor(this.getActivity()));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .build();

        ImageCapture.Builder builder = new ImageCapture.Builder();

        imageCapture = builder
                .setTargetRotation(this.getActivity().getWindowManager().getDefaultDisplay().getRotation())
                .build();
        preview.setSurfaceProvider(binding.mpreview.getSurfaceProvider());
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageAnalysis, imageCapture);

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String mCurrentPhotoPath = "";
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(new Date());
        imageFileName = timeStamp + ".jpg";

        File storageDir = this.getActivity().getExternalFilesDir("Pictures");  //external sd card
        storageDir = new File(storageDir, imageFileName);

        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                Log.e("noDir", "No directory created ");
            }
            storageDir.mkdirs();
        }

        storageDir.createNewFile();

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = storageDir.getAbsolutePath();
        Log.v("Image", "Image_File: " + mCurrentPhotoPath);
        return storageDir;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}