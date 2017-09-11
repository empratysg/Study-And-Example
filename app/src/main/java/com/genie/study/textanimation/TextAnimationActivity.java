package com.genie.study.textanimation;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.genie.study.R;

public class TextAnimationActivity extends AppCompatActivity implements AnimationView.onTickListener {

    private AnimationView animationView;
    private TextView chronometer, timeCalculated;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_animation);
        handler = new Handler();

        animationView = (AnimationView) findViewById(R.id.animation_view);
        chronometer = (TextView) findViewById(R.id.idddd);
        timeCalculated = (TextView) findViewById(R.id.timeCalculated);

        animationView.setOnTickListener(this);

        animationView.setText("This is a text very very long, 1234567890 0987654321 --------- =========");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animationView.startAnimation();
                timeCalculated.setText("Calculated: " + animationView.getTotalTimeAnmation());
            }
        }, 1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onTick(int tick) {
        chronometer.setText("" + tick);
    }

    public void showToast(View view) {
        Toast.makeText(this, chronometer.getText(), Toast.LENGTH_SHORT).show();
    }
}
