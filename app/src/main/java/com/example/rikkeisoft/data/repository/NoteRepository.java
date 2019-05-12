package com.example.rikkeisoft.data.repository;

import com.example.rikkeisoft.data.RealmManager;
import com.example.rikkeisoft.data.model.Note;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class NoteRepository {
    private Realm mRealm;

    public NoteRepository() {
        this.mRealm = RealmManager.getInstance().getmRealm();
    }


    public void insertNote(Note note) {
        mRealm.executeTransaction(realm -> {
            Number currentIdNum = realm.where(Note.class).max("id");
            int nextId;
            if (currentIdNum == null) {
                nextId = 0;
            } else {
                nextId = currentIdNum.intValue() + 1;
            }
            note.setId(nextId);
            realm.insertOrUpdate(note);
        });
    }

    public void deleteNote(int noteId) {
        mRealm.beginTransaction();
        mRealm.where(Note.class)
                .equalTo("id", noteId)
                .findAll()
                .deleteFirstFromRealm();
        mRealm.commitTransaction();
    }

    public void updateNote(Note note) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(note);
        mRealm.commitTransaction();
    }

    public List<Note> getNotes() {
        ArrayList<Note> notes = new ArrayList<>(mRealm.where(Note.class).findAll());
        return notes;
    }
}
