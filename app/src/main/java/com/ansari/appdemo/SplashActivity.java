package com.ansari.appdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME = 2500; // 2.5 seconds

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Splash screen ka XML
        setContentView(R.layout.activity_splash);

        // Handler initialize
        handler = new Handler(Looper.getMainLooper());

        // Splash ke baad LoginActivity par jump
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(
                        SplashActivity.this,
                        LoginActivity.class
                );

                startActivity(intent);

                // SplashActivity ko close kar do
                finish();
            }
        }, SPLASH_TIME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Handler callback remove
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}