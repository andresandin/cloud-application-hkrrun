package com.example.myapplication.ui.location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;
import com.example.myapplication.ui.home.HomeViewModel;
import com.example.myapplication.ui.location.LocationViewModel;

public class LocationFragment extends Fragment {

    private LocationViewModel locationViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        View root = inflater.inflate(R.layout.location_main, container, false);
       // final TextView textView = root.findViewById(R.id.text_home);
        locationViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);

            }
        });
        return root;
    }
}
