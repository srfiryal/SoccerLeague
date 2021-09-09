package com.example.soccerleague.utils;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("favorites.db")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()// if migration needed then this method will remove the existing database and will create new database
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

}
