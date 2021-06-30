package com.example.dripirrigationcontrol.ui.controlling;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ControllingViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public ControllingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Controlling fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
