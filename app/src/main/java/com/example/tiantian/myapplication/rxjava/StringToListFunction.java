package com.example.tiantian.myapplication.rxjava;

import com.example.tiantian.myapplication.base.Contracts;

import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.functions.Function;

public class StringToListFunction implements Function<String, ArrayList<String>> {

    private String interval;

    public StringToListFunction() {
        this(Contracts.INTERVAL);
    }

    public StringToListFunction(String interval) {
        this.interval = interval;
    }

    @Override
    public ArrayList<String> apply(String s) throws Exception {
        if (s.equals(""))
            return new ArrayList<>();
        String[] string = s.split(interval);
        return new ArrayList<>(Arrays.asList(string));
    }
}
