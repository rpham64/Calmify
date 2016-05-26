package com.rpham64.android.calmify.controller;

import android.support.v4.app.Fragment;

public class CalmifyActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return CalmifyFragment.newInstance();
    }

}
