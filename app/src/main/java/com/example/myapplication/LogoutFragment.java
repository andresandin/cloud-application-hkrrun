package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.data.ClientInformation;

public class LogoutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_logout, container, false);

        //Clear token and return to login fragment!
        ClientInformation.getInstance().setToken("");

        //Return to login scene!
        Intent intent = new Intent(getContext(), LogInFragment.class);

        startActivity(intent);

        return v;
    }



}
