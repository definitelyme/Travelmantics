package com.andela.brendan.travelmantics.Foundation;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class TravelmanticsApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /*Enable disk Persistence*/
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
