package com.genie.study.texttimer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.genie.study.R;

/**
 * Created by Genie on 7/8/2017.
 */

public class TimerTextViewFragment extends Fragment implements View.OnClickListener {
    private TimerTextView timerTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        timerTextView = view.findViewById(R.id.timer);
        view.findViewById(R.id.change_fragment).setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View view) {
        getFragmentManager().beginTransaction().replace(R.id.frame, new AnotherFragment()).addToBackStack(null).commit();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        timerTextView.setSourceTime("720000");
    }
}
