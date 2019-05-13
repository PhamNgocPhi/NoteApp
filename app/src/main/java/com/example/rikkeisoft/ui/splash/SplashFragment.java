package com.example.rikkeisoft.ui.splash;


import android.os.Handler;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.rikkeisoft.R;
import com.example.rikkeisoft.ui.base.BaseFragment;
import com.example.rikkeisoft.ui.main.MainActivity;
import com.example.rikkeisoft.ui.menu.MenuFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashFragment extends BaseFragment implements SplashView {

    private SplashPresenterImp presenter;

    public SplashFragment() {
        // Required empty public constructor
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_splash_home;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    protected void initView() {
        presenter = new SplashPresenterImp(this);
        getToolbar().setVisibility(View.GONE);

        Handler handler = new Handler();
        handler.postDelayed(() ->
                getNavigationManager().openAsRoot(MenuFragment.class, null), 2000);
    }

}
