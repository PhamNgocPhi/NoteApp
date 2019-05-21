package com.example.rikkeisoft.ui.main;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rikkeisoft.R;
import com.example.rikkeisoft.ui.NavigationManager;
import com.example.rikkeisoft.ui.custom.NoteToolbar;
import com.example.rikkeisoft.ui.menu.MenuFragment;
import com.example.rikkeisoft.ui.splash.SplashFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    private final long CLICK_TIME_INTERVAL = 600;

    private NavigationManager navigationManager;

    private Unbinder unbinder;
    private float rawX, rawY;
    private View focusedViewOnActionDown;
    private boolean touchWasInsideFocusedView, hasMove;

    public long lastClickTime = System.currentTimeMillis();

    @BindView(R.id.toolbar)
    NoteToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        navigationManager = new NavigationManager(this.getSupportFragmentManager(), R.id.rlMain);
        toolbar.setVisibility(View.GONE);
        navigationManager.openNoAddToBackStack(SplashFragment.class, null);
    }

    @Override
    public void onBackPressed() {
        if (navigationManager != null && navigationManager.getCurrentFragment() != null) {
            if (navigationManager.getCurrentFragment().onBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    public NavigationManager getNavigationManager() {
        return navigationManager;
    }

    // hide key board when click out side
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                rawX = ev.getRawX();
                rawY = ev.getRawY();
                hasMove = false;
                focusedViewOnActionDown = getCurrentFocus();
                if (focusedViewOnActionDown != null) {
                    final Rect rect = new Rect();
                    final int[] coordinates = new int[2];

                    focusedViewOnActionDown.getLocationOnScreen(coordinates);

                    rect.set(coordinates[0], coordinates[1],
                            coordinates[0] + focusedViewOnActionDown.getWidth(),
                            coordinates[1] + focusedViewOnActionDown.getHeight());

                    final int x = (int) ev.getX();
                    final int y = (int) ev.getY();

                    touchWasInsideFocusedView = rect.contains(x, y);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (!hasMove) {
                    float delta = (float) Math.hypot(rawX - ev.getRawX(), rawY - ev.getRawY());
                    hasMove = delta > 6f;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (focusedViewOnActionDown != null) {
                    final boolean consumed = super.dispatchTouchEvent(ev);
                    final View currentFocus = getCurrentFocus();
                    if (hasMove) {
                        return consumed;
                    }
                    if (focusedViewOnActionDown.equals(currentFocus)) {
                        if (touchWasInsideFocusedView) {
                            return consumed;
                        }
                    } else if (currentFocus instanceof EditText) {
                        return consumed;
                    }

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(focusedViewOnActionDown.getWindowToken(), 0);
                    return consumed;
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean isDuplicateClick() {
        long now = System.currentTimeMillis();
        if (now - lastClickTime < CLICK_TIME_INTERVAL) {
            lastClickTime = now;
            return true;
        } else {
            lastClickTime = now;
            return false;
        }
    }

    public NoteToolbar getToolbar() {
        return toolbar;
    }
}
