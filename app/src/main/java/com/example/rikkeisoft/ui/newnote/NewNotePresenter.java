package com.example.rikkeisoft.ui.newnote;

import com.example.rikkeisoft.data.model.Note;

import java.util.List;

public interface NewNotePresenter {
    void insertNote(Note note);

    void addImageNote(Note note, List<String> uRlImage);
}
