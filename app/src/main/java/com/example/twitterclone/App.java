package com.example.twitterclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("8Z1mQtznIi9whiIjg02EqRudfOnr3eIXJfKAf5Cc")
                .clientKey("EXS2v6K1gLPTrn84tw2a237M9YPcBvUm6xuceabb")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
