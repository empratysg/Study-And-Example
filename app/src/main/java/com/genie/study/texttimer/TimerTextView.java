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
import android.util.Log;

/**
 * Created by Genie on 7/8/2017.
 */

public class TimerTextView extends AppCompatTextView {
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
            long next = now + (1000 - now % 1000);

            Handler handler = getHandler();
            handler.removeCallbacks(mTicker);
            handler.postAtTime(mTicker, next);
        }
    };

    @SuppressLint("DefaultLocale")
    private void onTimeChanged() {
        long time = SystemClock.uptimeMillis();
        int secondPast;
        int minutePast = 0;
        int hourPast = 0;
        if (lastTimeChange == 0) {
            lastTimeChange = SystemClock.uptimeMillis();
            secondPast = 1;
        } else {
            long offset = time - lastTimeChange;
            offset = offset - (offset % 1000);
            secondPast = (int) (offset / 1000);
            lastTimeChange = time;
        }

        if (seconds >= secondPast) {
            seconds -= secondPast;
        } else {
            seconds = MINUTE_SECOND - (secondPast % MINUTE_SECOND);
            if (secondPast >= MINUTE_SECOND)
                minutePast = (secondPast - (secondPast % MINUTE_SECOND)) / MINUTE_SECOND;
            else
                minutePast = 1;
        }

        if (minutes >= minutePast) {
            minutes -= minutePast;
        } else {
            minutes = HOUR_MINUTE - (minutePast % HOUR_MINUTE);
            if (minutePast >= HOUR_MINUTE)
                hourPast = (minutePast - (minutePast % HOUR_MINUTE)) / HOUR_MINUTE;
            else
                hourPast = 1;
        }
        if (hours >= hourPast) {
            hours -= hourPast;
            setText(String.format(FORMAT_TIME_OUTPUT_DEFAULT, hours, minutes, seconds));
        } else {
            hours = 0;
            setText(END_OF_TIMER);
        }
    }

    public void setSourceTime(String source, boolean resetTimer) {
        if (resetTimer || sourceTime == null || !sourceTime.equals(source)) {
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
        setSourceTime(source, false);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
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
        seconds = Integer.parseInt(sourceTime.substring(4));
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
        setText(String.format(FORMAT_TIME_OUTPUT_DEFAULT, hours, minutes, seconds));
    }

    static class SavedState extends BaseSavedState {
        private String sourceTime;
        private int hours, minutes, seconds;
        private long lastTimeChange;

        public SavedState(Parcel source) {
            super(source);
            this.sourceTime = source.readString();
            this.hours = source.readInt();
            this.minutes = source.readInt();
            this.seconds = source.readInt();
            this.lastTimeChange = source.readLong();
        }

        @TargetApi(Build.VERSION_CODES.N)
        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            this.sourceTime = source.readString();
            this.hours = source.readInt();
            this.minutes = source.readInt();
            this.seconds = source.readInt();
            this.lastTimeChange = source.readLong();
        }

        public SavedState(Parcelable superState) {
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
