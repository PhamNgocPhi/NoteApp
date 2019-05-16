package com.example.rikkeisoft.ui.detail;

import com.example.rikkeisoft.data.model.Note;

import java.util.Date;
import java.util.List;

public interface DetailPresenter {
    List<Note> getAllNote();

    void updateNote(Note note);

    void deleteNote(int noteId);
}
