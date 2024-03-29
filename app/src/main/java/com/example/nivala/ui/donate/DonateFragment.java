package com.example.nivala.ui.donate;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nivala.R;
import com.example.nivala.adapter.DonateImageAdapter;
import com.example.nivala.databinding.FragmentDonateBinding;
import com.example.nivala.databinding.FragmentGiveBinding;
import com.example.nivala.ui.give.GiveFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
* Created by Prajwal Maruti Waingankar on 28-04-2022, 23:40
* Copyright (c) 2021 . All rights reserved.
* Email: prajwalwaingankar@gmail.com
* Github: prajwalmw
*/

public class DonateFragment extends Fragment implements PaymentResultListener {
FragmentDonateBinding binding;
    final int UPI_PAYMENT = 0;
    private RewardedAd rewardedAd;
   // private static final String AD_UNIT_ID = "ca-app-pub-6656140211699925/5848724218"; // live
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"; // test
    boolean isLoading;
    private DonateImageAdapter adapter;
    private Checkout checkout = new Checkout();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDonateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.gpayBtn.setOnClickListener(v -> payUsingUpi());
        binding.adsBtn.setOnClickListener(v -> loadRewardedAd());

        adapter = new DonateImageAdapter();
        binding.donateRv.setAdapter(adapter);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Checkout.preload(getActivity().getApplicationContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void payUsingUpi() {
  /*      String sAmount = "1";
        // rounding off the amount.
        int amount = Math.round(Float.parseFloat(sAmount) * 100);
        checkout.setKeyID("rzp_test_xh5plCqQSHlGTq");
        checkout.setImage(R.mipmap.ic_launcher_round);
        JSONObject object = new JSONObject();
        try {
            object.put("name", "Nivala");
            object.put("description", "Test payment");
//            object.put("theme.color", "#2E1E91");
            object.put("currency", "INR");
            object.put("amount", amount);
         //   object.put("order_id", "order_LIPr13T6U02OJa");
            object.put("prefill.contact", "7304154312");
            object.put("prefill.email", "prajwalwaingankar@gmail.com");
            checkout.open(DonateFragment.this.getActivity(), object);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        Uri uri =
                new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
                        .appendQueryParameter("pa", "7304154312@okbizaxis")  // virtual ID ** Need a merchant account
                        .appendQueryParameter("pn", "nivala" /*"your-merchant-name"*/)          // name
                        .appendQueryParameter("mc", "BCR2DN4TSCP6XIR2")          // optional // BCR2DN4T4DNLPADO // BCR2DN4TSCP6XIR2
                        .appendQueryParameter("tr", "134729489")     // optional -- can be random
                        .appendQueryParameter("tn",  "Thank you for the initiative")       // any note about payment
                        .appendQueryParameter("am", "1.0" /*"your-order-amount"*/)           // amount
                        .appendQueryParameter("cu", "INR").build();                // currency

        // Only for Google Pay...
     /*   String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
        int GOOGLE_PAY_REQUEST_CODE = 123;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);*/

        // For all UPI related apps...
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        if(null != chooser.resolveActivity(this.getActivity().getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(this.getActivity(),"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(this.getActivity())) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(this.getActivity(), "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(this.getActivity(), "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);
            }
            else {
                Toast.makeText(this.getActivity(), "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(this.getActivity(), "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    private void loadRewardedAd() {
        if (rewardedAd == null) {
            isLoading = true;
            Toast.makeText(getContext(), "Ad is about to play...", Toast.LENGTH_SHORT).show();
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(
                    this.getContext(),
                    AD_UNIT_ID,
                    adRequest,
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.
                            Log.d("GAT", loadAdError.getMessage());
                            rewardedAd = null;
                            DonateFragment.this.isLoading = false;
                          //  Toast.makeText(getContext(), "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            DonateFragment.this.rewardedAd = rewardedAd;
                            rewardedAd.show(DonateFragment.this.getActivity(), new OnUserEarnedRewardListener() {
                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                                }
                            });
                            Log.d("GAT", "onAdLoaded");
                            DonateFragment.this.isLoading = false;
                          //  Toast.makeText(getContext(), "onAdLoaded", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.v("Paymnt: ", "Success: " + s);
        Toast.makeText(getActivity(), "Hi Success: " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.v("Paymnt: ", "Error: " + s);
        Toast.makeText(getActivity(), "Hi Error: " + s, Toast.LENGTH_SHORT).show();
    }
}
