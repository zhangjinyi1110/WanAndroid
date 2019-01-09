package com.example.tiantian.myapplication.repository;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.tiantian.myapplication.base.BaseRepository;
import com.example.tiantian.myapplication.data.photo.Photo;
import com.example.tiantian.myapplication.utils.CallBack;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class AlbumRepository extends BaseRepository {

    private Cursor cursor;

    public void getPhotoList(int page, final int size, final CallBack<List<Photo>> callBack) {
        if (cursor == null) {
            cursor = context.getContentResolver()
                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null
                            , null, null, null);
        }
        int offset = page * size;
        Flowable.just(offset)
                .subscribeOn(Schedulers.io())
                .map(new Function<Integer, List<Photo>>() {
                    @Override
                    public List<Photo> apply(Integer integer) throws Exception {
                        List<Photo> photos = new ArrayList<>();
                        cursor.moveToFirst();
                        if (!cursor.move(integer - size)) {
                            return photos;
                        }
                        while (cursor.moveToNext() && photos.size() < size) {
                            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.TITLE));
                            byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                            String path = new String(data, 0, data.length - 1);
                            long date = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN));
                            @SuppressLint("SimpleDateFormat")
                            String time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(date));
                            long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE));
                            Photo photo = new Photo();
                            photo.setName(name);
                            photo.setPath(path);
                            photo.setSize(cursor.getPosition());
                            photo.setTime(time);
                            photos.add(photo);
                        }
                        return photos;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Photo>>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(List<Photo> photos) {
                        callBack.onSuccess(photos);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, "onError: " + t.toString());
                        Toast.makeText(getCurr(), t.toString(), Toast.LENGTH_SHORT).show();
                        callBack.onFailure(t);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void cursorClose() {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    public int getCursorCount() {
        return cursor == null ? 0 : cursor.getCount();
    }
}
