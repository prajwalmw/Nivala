package com.example.nivala.ui.take;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nivala.databinding.FragmentTakeBinding;
import com.example.nivala.model.GiveDataModel;
import com.example.nivala.adapter.TakeAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

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

        database = FirebaseDatabase.getInstance();
        giveDataModels = new ArrayList<>();
        binding.recyclerviewTake.setLayoutManager(new LinearLayoutManager(getActivity(),
                RecyclerView.VERTICAL, false));
        adapter = new TakeAdapter(getActivity(), giveDataModels);
        binding.recyclerviewTake.setAdapter(adapter);

        database.getReference().child("Food-Post")
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    GiveDataModel data = snapshot.getValue(GiveDataModel.class);
                                    giveDataModels.add(0, data);
                                    Log.v("hel", "hel: " + giveDataModels.get(0).getFoodItem());
                                    adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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
       // adapter.notifyDataSetChanged();
    }
}