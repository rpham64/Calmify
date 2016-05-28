package com.rpham64.android.calmify.controller;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.View;

public class CalmifyActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return CalmifyFragment.newInstance();
    }

    /**
     * Immersive Full-screen Mode
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        View decorView = getWindow().getDecorView();

        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }
}
