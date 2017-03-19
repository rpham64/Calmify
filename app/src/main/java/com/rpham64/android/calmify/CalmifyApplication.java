package com.rpham64.android.calmify;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Rudolf on 3/18/2017.
 */

public class CalmifyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }
}
