package com.atzandroid.weektasks;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        startActivity(new Intent(SplashActivity.this, KeypadActivity.class));
        startActivity(new Intent(SplashActivity.this, TestActivity.class));
        finish();
    }
}