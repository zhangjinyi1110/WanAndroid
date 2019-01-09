package com.zjy.simplemodule.rxcallback;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import io.reactivex.subjects.PublishSubject;

import static android.app.Activity.RESULT_OK;

public class RxFragment extends Fragment {

    private final int REQUEST_CODE = 0x55;
    private PublishSubject<ArrayList<Permission>> subject;
    private PublishSubject<Avoid> avoidSubject;

    public void requestPermissions(PublishSubject<ArrayList<Permission>> subject, String[] permissions) {
        this.subject = subject;
        requestPermissions(permissions, REQUEST_CODE);
    }

    public void avoid(PublishSubject<Avoid> subject, Intent intent) {
        startActivityForResult(intent, REQUEST_CODE);
        avoidSubject = subject;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (avoidSubject == null) {
            return;
        }
        avoidSubject.onNext(new Avoid(resultCode == RESULT_OK, resultCode, data));
        avoidSubject.onComplete();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (subject == null) {
            return;
        }
        ArrayList<Permission> permissionList = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            Permission result = new Permission(grantResults[i] == PackageManager.PERMISSION_GRANTED, permissions[i]);
            permissionList.add(result);
        }
        subject.onNext(permissionList);
        subject.onComplete();
    }
}
