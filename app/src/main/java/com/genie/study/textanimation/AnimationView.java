package com.genie.study.textanimation;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.genie.study.R;

/**
 * Created by huong on 9/7/2017.
 */

public class AnimationView extends FrameLayout implements Handler.Callback, Runnable {
    private static final int DELAY_PER_TICK = 17;
    private static final float RATIO_DISTANCE_PER_MILI = 3;
    private TextView textView;
    private View view;

    private Handler handler;
    private onTickListener onTickListener;

    private long totalTimeAnmation = 0;
    private float viewTranslateX = 0, textViewTranslateX = 0, baseViewTranslateX = 0, baseTextViewTranslateX = 0;
    private int widTextView = 0;
    private Thread thread = new Thread(this);

    public AnimationView(@NonNull Context context) {
        super(context);
        initView();
    }

    public AnimationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AnimationView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.layout_animation, this);
        int screenWid = getScreenWid();
        baseViewTranslateX = getResources().getDimensionPixelSize(R.dimen.button_dimen);
        baseTextViewTranslateX = screenWid + baseViewTranslateX;
        textView = findViewById(R.id.textview);
        view = findViewById(R.id.button);

        textView.setMinWidth(screenWid);
        textView.setTranslationX(baseTextViewTranslateX);
        widTextView = screenWid;
    }

    private int getScreenWid() {
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public void setText(String text) {
        textView.setText(text);
        totalTimeAnmation = 0;
    }

    public void startAnimation() {
        if (handler == null)
            handler = new Handler(this);
        calculateTotalTime();
        running = true;
        timeBase = SystemClock.elapsedRealtime();
        thread.start();
    }

    private void calculateTotalTime() {
        int screenWid = getScreenWid();
        widTextView = textView.getWidth();
        if (widTextView < screenWid) {
            widTextView = screenWid;
            textView.setWidth(widTextView);
        }
        totalTimeAnmation = (long) ((widTextView + screenWid + baseViewTranslateX) * RATIO_DISTANCE_PER_MILI);
        Log.e("TAGG", widTextView + " - " + totalTimeAnmation);
    }

    @Override
    public boolean handleMessage(Message message) {
        view.setTranslationX(viewTranslateX);
        textView.setTranslationX(textViewTranslateX);
        dispatchEndAnimation(message.arg1);
        return false;
    }

    private void dispatchEndAnimation(int tick) {
        if (onTickListener != null)
            onTickListener.onTick(tick);
    }

    public void setOnTickListener(onTickListener onTickListener) {
        this.onTickListener = onTickListener;
    }

    public long getTotalTimeAnmation() {
        return totalTimeAnmation;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (handler != null)
            handler.removeCallbacksAndMessages(null);
        handler = null;

        running = false;
        thread.interrupt();
    }

    private boolean running = false;
    private long timeBase = 0;

    @Override
    public void run() {
        while (running) {
            float timePast = (SystemClock.elapsedRealtime() - timeBase);
            float distance = timePast / RATIO_DISTANCE_PER_MILI;

            viewTranslateX = baseViewTranslateX - distance;
            textViewTranslateX = baseTextViewTranslateX - distance;

            if (viewTranslateX < 0)
                viewTranslateX = 0;
            if (textViewTranslateX < -widTextView) {
                textViewTranslateX = -widTextView;
                running = false;
            }
            Message msg = Message.obtain();
            msg.arg1 = (int) timePast;
            handler.sendMessage(msg);
            int delay = 0;
            if (totalTimeAnmation > timePast) {
                delay = totalTimeAnmation - timePast > DELAY_PER_TICK ? DELAY_PER_TICK : (int) (totalTimeAnmation - timePast);
            }

            SystemClock.sleep(delay);
        }
    }

    interface onTickListener {
        void onTick(int tick);
    }
}

