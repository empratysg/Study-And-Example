package com.genie.study.texttimer;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.genie.study.R;

public class MyTextTimerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_text_clock);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.frame) == null) {
            fragmentManager.beginTransaction().replace(R.id.frame, new TimerTextViewFragment()).commit();
        }
    }
}
