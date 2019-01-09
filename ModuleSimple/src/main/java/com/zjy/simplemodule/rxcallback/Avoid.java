package com.zjy.simplemodule.rxcallback;

import android.content.Intent;

public class Avoid {

    private boolean ok;
    private int resultCode;
    private Intent intent;

    public Avoid(boolean ok, int resultCode, Intent intent) {
        this.ok = ok;
        this.resultCode = resultCode;
        this.intent = intent;
    }

    public int getResultCode() {
        return resultCode;
    }

    public boolean isOk() {
        return ok;
    }

    public Intent getIntent() {
        return intent;
    }
}
