package com.example.rikkeisoft.ui.menu;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rikkeisoft.R;
import com.example.rikkeisoft.data.model.Note;
import com.example.rikkeisoft.ui.adapter.NoteAdapter;
import com.example.rikkeisoft.ui.base.BaseFragment;
import com.example.rikkeisoft.ui.custom.NoteToolbar;

import java.util.List;

import butterknife.BindView;

public class MenuFragment extends BaseFragment implements MenuView {

    private NoteAdapter noteAdapter;
    private List<Note> notes;
    private MenuPresenterImp menuPresenterImp;
    private NoteToolbar noteToolbar;

    @BindView(R.id.rcNote)
    RecyclerView rcNote;

    @Override
    protected int layoutRes() {
        return R.layout.fragment_menu;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    protected void initView() {
        menuPresenterImp = new MenuPresenterImp(this);
        getToolbar().setVisibility(View.VISIBLE);
        noteAdapter = new NoteAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rcNote.setLayoutManager(gridLayoutManager);
        rcNote.setAdapter(noteAdapter);

    }
}
