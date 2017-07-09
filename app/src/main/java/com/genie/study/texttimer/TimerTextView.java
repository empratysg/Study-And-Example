package com.genie.study.texttimer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * Created by Genie on 7/8/2017.
 * Text view count time with input
 */

public class TimerTextView extends AppCompatTextView {
    private static final String TAG = "TimerTextView";
    private static final int ONE_SECOND_MILLS = 1000;
    private static final int MINUTE_SECOND = 60;
    private static final int HOUR_MINUTE = 60;
    private static final int HOUR_SECOND = 3600;
    private static final String END_OF_TIMER = "00:00:00";
    private static final String FORMAT_TIME_INPUT_DEFAULT = "hhmmss";
    private static final String FORMAT_TIME_OUTPUT_DEFAULT = "%02d:%02d:%02d";
    private String sourceTime;
    private int hours, minutes, seconds;
    private long lastTimeChange;

    public TimerTextView(Context context) {
        super(context);
    }

    public TimerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private final Runnable mTicker = new Runnable() {
        public void run() {
            onTimeChanged();
            long now = SystemClock.uptimeMillis();
            long next = now + ONE_SECOND_MILLS;

            Handler handler = getHandler();
            handler.removeCallbacks(mTicker);
            handler.postAtTime(mTicker, next);
        }
    };

    @SuppressLint("DefaultLocale")
    private void onTimeChanged() {
        long time = SystemClock.uptimeMillis();
        int remainTime = remainTimeToSeconds();
        int secondPast;
        if (lastTimeChange == 0) {
            secondPast = 1;
        } else {
            long offset = time - lastTimeChange;
            offset = offset - (offset % 1000);
            secondPast = (int) (offset / 1000);
        }
        lastTimeChange = time;
        if (remainTime >= secondPast) {
            remainTime -= secondPast;
            hours = hourFromTime(remainTime);
            minutes = minuteFromTime(remainTime);
            seconds = secondFromTime(remainTime);
        } else {
            sourceTime = null;
            hours = 0;
            minutes = 0;
            seconds = 0;
            Handler handler = getHandler();
            if (handler != null)
                handler.removeCallbacks(mTicker);
        }
        setText(String.format(FORMAT_TIME_OUTPUT_DEFAULT, hours, minutes, seconds));
    }

    private int remainTimeToSeconds() {
        return (hours * HOUR_SECOND) + (minutes * MINUTE_SECOND) + seconds;
    }

    private int hourFromTime(int seconds) {
        int totalHourInSecond = seconds - (seconds % HOUR_SECOND);
        return totalHourInSecond / HOUR_SECOND;
    }

    private int minuteFromTime(int seconds) {
        int totalMinuteInSecond = seconds - (seconds % MINUTE_SECOND);
        int totalMinute = totalMinuteInSecond / MINUTE_SECOND;
        return totalMinute % HOUR_MINUTE;
    }

    private int secondFromTime(int seconds) {
        return seconds % MINUTE_SECOND;
    }

    /**
     * @param source     String Source Time
     * @param resetTimer if true timer will always change with source input
     *                   otherwise check if source time is change or not for reset timer
     */
    public void setSourceTime(String source, boolean resetTimer) {
        if (TextUtils.isEmpty(source)) return;
        if (resetTimer || TextUtils.isEmpty(sourceTime) || !sourceTime.equals(source)) {
            sourceTime = source;
            getHourMinuteSecondFromSource();
        }
        Handler handler = getHandler();
        if (handler != null) {
            handler.removeCallbacks(mTicker);
            handler.postDelayed(mTicker, 1000);
        }
    }

    public void setSourceTime(String source) {
        setSourceTime(source, true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (TextUtils.isEmpty(sourceTime)) return;
        Handler handler = getHandler();
        handler.removeCallbacks(mTicker);
        handler.postDelayed(mTicker, 1000);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (getHandler() != null)
            getHandler().removeCallbacks(mTicker);
    }

    /**
     * FUTURE
     */
    @SuppressLint("DefaultLocale")
    private void getHourMinuteSecondFromSource() {
        hours = Integer.parseInt(sourceTime.substring(0, 2));
        minutes = Integer.parseInt(sourceTime.substring(2, 4));
        if (minutes >= HOUR_MINUTE)
            minutes = HOUR_MINUTE - 1;
        seconds = Integer.parseInt(sourceTime.substring(4));
        if (seconds >= MINUTE_SECOND)
            seconds = MINUTE_SECOND - 1;
        lastTimeChange = 0;
        setText(String.format(FORMAT_TIME_OUTPUT_DEFAULT, hours, minutes, seconds));
    }

    @Override
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.sourceTime = sourceTime;
        savedState.hours = hours;
        savedState.minutes = minutes;
        savedState.seconds = seconds;
        savedState.lastTimeChange = lastTimeChange;
        return savedState;
    }

    @Override
    @SuppressLint("DefaultLocale")
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.sourceTime = savedState.sourceTime;
        this.hours = savedState.hours;
        this.minutes = savedState.minutes;
        this.seconds = savedState.seconds;
        this.lastTimeChange = savedState.lastTimeChange;
        onTimeChanged();
        if (TextUtils.isEmpty(sourceTime)) return;
        Handler handler = getHandler();
        handler.removeCallbacks(mTicker);
        handler.postDelayed(mTicker, 1000);
    }

    private static class SavedState extends BaseSavedState {
        private String sourceTime;
        private int hours, minutes, seconds;
        private long lastTimeChange;

        SavedState(Parcel source) {
            super(source);
            this.sourceTime = source.readString();
            this.hours = source.readInt();
            this.minutes = source.readInt();
            this.seconds = source.readInt();
            this.lastTimeChange = source.readLong();
        }

        @TargetApi(Build.VERSION_CODES.N)
        SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            this.sourceTime = source.readString();
            this.hours = source.readInt();
            this.minutes = source.readInt();
            this.seconds = source.readInt();
            this.lastTimeChange = source.readLong();
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(this.sourceTime);
            out.writeInt(this.hours);
            out.writeInt(this.minutes);
            out.writeInt(this.seconds);
            out.writeLong(this.lastTimeChange);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
