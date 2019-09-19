package com.example.livedatademo;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UserViewModel extends ViewModel {

    private final String param;
    private MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public UserViewModel(String param) {
        this.param = param;
    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public static class Factory implements ViewModelProvider.Factory {

        private String param;

        public Factory(String param) {
            this.param = param;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new UserViewModel(param);
        }
    }

    public String getParam() {
        return param;
    }
}
