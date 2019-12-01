package com.android.makeyousmile.ui.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.makeyousmile.R;
import com.android.makeyousmile.ui.Utility.Utils;

public class Splash extends AppCompatActivity {
    public static final int SPLASH_TIMER = 3 * 1000;
    private static final String TAG = MainActivity.class.getName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
            startSplashTimer();
    }

    private void startSplashTimer() {
        Thread timerSplash = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(SPLASH_TIMER);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {

                        if (Utils.getInstance().getBoolean("isLoggedIn",Splash.this))
                            startMainActivity();
                        else
                            startLoginActivity();
                }
            }
        };
        timerSplash.start();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(Splash.this, Login.class);
        startActivity(intent);
        finish();

    }
    private void startMainActivity() {
        Intent intent = new Intent(Splash.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}
