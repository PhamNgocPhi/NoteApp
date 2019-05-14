package com.example.rikkeisoft.ui.menu;

import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rikkeisoft.R;
import com.example.rikkeisoft.data.model.Note;
import com.example.rikkeisoft.ui.adapter.NoteAdapter;
import com.example.rikkeisoft.ui.base.BaseFragment;
import com.example.rikkeisoft.ui.newnote.NewNoteFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MenuFragment extends BaseFragment implements MenuView {

    private NoteAdapter noteAdapter;
    private List<Note> notes;
    private MenuPresenterImp imp;

    @BindView(R.id.rcNote)
    RecyclerView rcNote;

    public MenuFragment() {
        imp = new MenuPresenterImp(this);
        notes = new ArrayList<>();
        noteAdapter = new NoteAdapter();



    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_menu;
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    @Override
    protected void initView() {
        getToolbar().setVisibility(View.VISIBLE);
        notes = imp.getAllNote();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rcNote.setLayoutManager(gridLayoutManager);
        rcNote.setAdapter(noteAdapter);
        noteAdapter.setNotes(notes);
        openNewNote();

    }

    private void openNewNote() {
        getToolbar().onClickAdd(v -> {
            getNavigationManager().open(NewNoteFragment.class, null);
        });
    }

}
