package com.example.myapplication.ui.crew;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CrewViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CrewViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Crew behind HKR Run");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
