package com.genie.study.progressbar;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.genie.study.R;

public class MyProgressBarActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private ProgressBar progressBarBasic, progressBarAdvance;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_progress_bar);
        progressBarBasic = (ProgressBar) findViewById(R.id.progress_bar_basic);
        progressBarAdvance = (ProgressBar) findViewById(R.id.progress_bar_advance);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);

        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (i == progressBarBasic.getMax()) {
            progressBarBasic.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.bg_progress_bar_done));
        } else if (progressBarBasic.getProgress() == progressBarBasic.getMax() && i < progressBarBasic.getProgress()) {
            progressBarBasic.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.bg_progress_bar_basic));
        }
        if (i == progressBarAdvance.getMax()) {
            progressBarAdvance.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.bg_progress_bar_done));
        } else if (progressBarAdvance.getProgress() == progressBarAdvance.getMax() && i < progressBarAdvance.getProgress()) {
            progressBarAdvance.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.bg_progress_bar_advance));
        }
        progressBarBasic.setProgress(i);
        progressBarAdvance.setProgress(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
