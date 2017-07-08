package com.genie.study;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.genie.study.progressbar.MyProgressBarActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openMyProgressBar(View view) {
        startActivity(new Intent(this, MyProgressBarActivity.class));
    }
}
