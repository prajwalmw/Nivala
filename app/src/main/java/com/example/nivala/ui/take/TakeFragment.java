package com.example.nivala.ui.take;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nivala.databinding.FragmentTakeBinding;
import com.example.nivala.model.GiveDataModel;
import com.example.nivala.ui.take.adapter.TakeAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TakeFragment extends Fragment {
    private FragmentTakeBinding binding;
    TakeAdapter adapter;
    GiveDataModel model;
    FirebaseDatabase database;
    List<GiveDataModel> giveDataModels;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentTakeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

       // final TextView textView = binding.textHome;
        //  homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        database = FirebaseDatabase.getInstance();
        giveDataModels = new ArrayList<>();
        binding.recyclerviewTake.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        adapter = new TakeAdapter(getActivity(), giveDataModels);
        binding.recyclerviewTake.setAdapter(adapter);

        database.getReference().child("Food-Post")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            GiveDataModel data = postSnapshot.getValue(GiveDataModel.class);
                            giveDataModels.add(data);
                            Collections.reverse(giveDataModels);
                            Log.v("hel", "hel: " + giveDataModels.get(0).getFoodItem());
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}