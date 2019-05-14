package com.example.rikkeisoft.ui.newnote;


import com.example.rikkeisoft.data.model.Note;
import com.example.rikkeisoft.data.repository.NoteRepository;

import java.util.List;

public class NewNotePresenterImp implements NewNotePresenter {

    private NewNoteView mView;
    private NoteRepository noteRepository;

    public NewNotePresenterImp(NewNoteView mView) {
        this.mView = mView;
        noteRepository = new NoteRepository();
    }

    @Override
    public void insertNote(Note note) {
        if (note != null) {
            noteRepository.insertNote(note);
            mView.backMenu();
        }
    }

    @Override
    public void addImageNote(Note note, List<String> uRlImage) {

    }
}
