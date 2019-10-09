package com.example.carmerapreview;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class MyApplicadtion extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
