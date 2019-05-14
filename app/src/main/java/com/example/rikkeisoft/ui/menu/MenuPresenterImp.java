package com.example.rikkeisoft.ui.menu;

import com.example.rikkeisoft.data.model.Note;
import com.example.rikkeisoft.data.repository.NoteRepository;

import java.util.List;

public class MenuPresenterImp implements MenuPresenter {
    private MenuView mView;
    private NoteRepository noteRepository;

    public MenuPresenterImp(MenuView mView) {
        this.mView = mView;
        noteRepository = new NoteRepository();
    }

    @Override
    public List<Note> getAllNote() {
        List<Note> notes = noteRepository.getNotes();
        return notes;
    }
}
