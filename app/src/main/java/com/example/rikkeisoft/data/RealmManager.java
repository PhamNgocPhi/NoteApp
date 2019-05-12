package com.example.rikkeisoft.data;

import io.realm.Realm;

public class RealmManager {
    private static RealmManager sInstance;
    private Realm mRealm;

    public static RealmManager getInstance() {
        if (sInstance == null) {
            sInstance = new RealmManager();
        }
        return sInstance;
    }

    public Realm getmRealm() {
        mRealm = Realm.getDefaultInstance();
        return mRealm;
    }
}
