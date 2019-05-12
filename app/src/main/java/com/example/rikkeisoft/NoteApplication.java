package com.example.rikkeisoft;

import android.app.Application;

import com.example.rikkeisoft.util.Define;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class NoteApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initRealm();
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Define.REALM_NAME)
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

    }
}
