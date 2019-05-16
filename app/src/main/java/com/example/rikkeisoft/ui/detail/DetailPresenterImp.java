package com.example.rikkeisoft.ui.detail;

import com.example.rikkeisoft.data.model.Note;
import com.example.rikkeisoft.data.repository.NoteRepository;

import java.util.Date;
import java.util.List;

public class DetailPresenterImp implements DetailPresenter {
    private DetailView detailView;
    private NoteRepository noteRepository;

    public DetailPresenterImp(DetailView detailView) {
        this.detailView = detailView;
        this.noteRepository = new NoteRepository();
    }

    @Override
    public List<Note> getAllNote() {
        List<Note> notes = noteRepository.getNotes();
        return notes;
    }

    @Override
    public void updateNote(Note note) {
        noteRepository.updateNote(note);
        detailView.backMenu();

    }

    @Override
    public void deleteNote(int noteId) {
        noteRepository.deleteNote(noteId);
        detailView.backMenu();
    }
}
