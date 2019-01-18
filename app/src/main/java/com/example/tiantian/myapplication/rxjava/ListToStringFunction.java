package com.example.tiantian.myapplication.rxjava;

import com.example.tiantian.myapplication.base.Contracts;

import java.util.ArrayList;

import io.reactivex.functions.Function;

public class ListToStringFunction implements Function<ArrayList<String>, String> {

    private String interval;

    public ListToStringFunction() {
        this(Contracts.INTERVAL);
    }

    public ListToStringFunction(String interval) {
        this.interval = interval;
    }

    @Override
    public String apply(ArrayList<String> strings) throws Exception {
        StringBuffer buffer = new StringBuffer();
        for (String s : strings) {
            buffer.append(s).append(interval);
        }
        return buffer.toString();
    }
}
