package com.android.makeyousmile.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Food for one (person) suffices two, and food for" +
                "two (person) suffices four person and food four person suffices eight person.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}