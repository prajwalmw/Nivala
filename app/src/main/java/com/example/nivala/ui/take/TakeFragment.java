package com.example.nivala.ui.take;

import android.os.Bundle;
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
import com.example.nivala.ui.take.adapter.TakeAdapter;

public class TakeFragment extends Fragment {
    private FragmentTakeBinding binding;
    TakeAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentTakeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

       // final TextView textView = binding.textHome;
        //  homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        binding.recyclerviewTake.setLayoutManager(new LinearLayoutManager(this.getActivity(), RecyclerView.VERTICAL, false));
        adapter = new TakeAdapter();
        binding.recyclerviewTake.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}