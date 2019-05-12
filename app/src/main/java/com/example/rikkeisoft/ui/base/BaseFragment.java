package com.example.rikkeisoft.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rikkeisoft.ui.NavigationManager;
import com.example.rikkeisoft.ui.custom.NoteToolbar;
import com.example.rikkeisoft.ui.main.MainActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    private NavigationManager navigationManager;
    private Unbinder unbinder;
    private MainActivity activity;

    @LayoutRes
    protected abstract int layoutRes();

    /**
     * if return true, use super.onBackPressed()
     */
    public abstract boolean onBackPressed();

    protected abstract void initView();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutRes(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            handleReceivedData(bundle);
        }

        initView();
    }

    public void handleReceivedData(Bundle bundle) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
        navigationManager = activity.getNavigationManager();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    protected boolean isDuplicateClick() {
        return activity.isDuplicateClick();
    }

    protected MainActivity getMainActivity() {
        return activity;
    }

    protected NavigationManager getNavigationManager() {
        return navigationManager;
    }

    protected NoteToolbar getToolbar() {
        return activity.getToolbar();
    }
}
