package com.example.myapplication.ui.crew;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;

public class CrewFragment extends Fragment {

    private CrewViewModel crewViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        crewViewModel =
                ViewModelProviders.of(this).get(CrewViewModel.class);
        View root = inflater.inflate(R.layout.fragment_crew, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        crewViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

}
