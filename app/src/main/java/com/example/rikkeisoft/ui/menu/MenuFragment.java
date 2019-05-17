package com.example.rikkeisoft.ui.menu;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rikkeisoft.R;
import com.example.rikkeisoft.data.model.Note;
import com.example.rikkeisoft.ui.adapter.NoteAdapter;
import com.example.rikkeisoft.ui.base.BaseFragment;
import com.example.rikkeisoft.ui.detail.DetailFragment;
import com.example.rikkeisoft.ui.newnote.NewNoteFragment;
import com.example.rikkeisoft.util.Define;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MenuFragment extends BaseFragment implements MenuView,NoteAdapter.NoteOnclickListener {

    private NoteAdapter noteAdapter;
    private MenuPresenterImp menuPresenterImp;

    @BindView(R.id.rcNote)
    RecyclerView rcNote;

    public MenuFragment() {
        menuPresenterImp = new MenuPresenterImp(this);
        noteAdapter = new NoteAdapter();
        noteAdapter.setNoteOnclickListener(this);
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_menu;
    }

    @Override
    public boolean onBackPressed() {
        if (!getNavigationManager().navigateBack(null) && getActivity() != null) {
            getActivity().moveTaskToBack(true);
        }
        return false;
    }

    @Override
    protected void initView() {
        initToolbar();
        List<Note> notes = menuPresenterImp.getAllNote();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rcNote.setLayoutManager(gridLayoutManager);
        rcNote.setAdapter(noteAdapter);
        noteAdapter.setNotes(notes);


    }

    private void initToolbar(){
        getToolbar().setVisibility(View.VISIBLE);
        getToolbar().setVisibilityIvBack()
                .setVisibilityIvBack()
                .setVisibilityIvCamera()
                .setVisibilityIvColor()
                .clearTitle()
                .setVisibilityIvSave();
        getToolbar().onClickAdd(v -> {
            getNavigationManager().open(NewNoteFragment.class, null);
        });

    }


    @Override
    public void onClickItem(int noteId) {
        Bundle bundle = new Bundle();
        bundle.putInt(Define.ID,noteId);
        getNavigationManager().open(DetailFragment.class, bundle);
    }
}
