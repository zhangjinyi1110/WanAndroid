package com.example.tiantian.myapplication.viewmodel.photo;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tiantian.myapplication.base.BaseViewModel;
import com.example.tiantian.myapplication.data.photo.Photo;
import com.example.tiantian.myapplication.repository.AlbumRepository;
import com.example.tiantian.myapplication.utils.CallBack;

import java.util.List;

public class AlbumViewModel extends BaseViewModel<AlbumRepository> {

    private MutableLiveData<List<Photo>> photoList;

    public AlbumViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<Photo>> getPhotoList() {
        if(photoList==null){
            photoList = new MutableLiveData<>();
        }
        return photoList;
    }

    public void getPhotoList(int page, int size) {
        repository.getPhotoList(page, size, new CallBack<List<Photo>>() {
            @Override
            public void onSuccess(List<Photo> photos) {
                photoList.setValue(photos);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public void cursorClose() {
        repository.cursorClose();
    }

    public int getCursorCount() {
        return repository.getCursorCount();
    }

}
