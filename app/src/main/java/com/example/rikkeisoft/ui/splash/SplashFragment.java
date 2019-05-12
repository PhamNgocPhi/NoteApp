package com.example.rikkeisoft.ui.splash;


import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.rikkeisoft.R;
import com.example.rikkeisoft.ui.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashFragment extends BaseFragment implements SplashView {

    private SplashPresenter presenter;

    public SplashFragment() {
        // Required empty public constructor
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_splash;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    protected void initView() {
        presenter = new SplashPresenterImp(this);
        getToolbar().setVisibility(View.GONE);
    }

}
