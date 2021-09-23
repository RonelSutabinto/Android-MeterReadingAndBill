package com.androidapp.mytools.objectmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Handler;

public class ProgressDialogMaker {
    public static Handler progressHandler;

    public static ProgressDialog myProgressBar(Activity activity, String title, String message, int max){
        progressHandler = new Handler();
        ProgressDialog barProgressBar = new android.app.ProgressDialog(activity);
        barProgressBar.setTitle(title);
        barProgressBar.setMessage(message);
        barProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        barProgressBar.setProgress(0);
        barProgressBar.setMax(max);
        barProgressBar.setCancelable(false);
        //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        return barProgressBar;
    }

    public static Runnable increaseProgress(final ProgressDialog myProgressDialog){
        Runnable myDialogProgress = new Runnable() {
            @Override
            public void run() {
                myProgressDialog.incrementProgressBy(1);
            }
        };
        return myDialogProgress;
    }
}
