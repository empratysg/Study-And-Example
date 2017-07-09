package com.genie.study.texttimer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.genie.study.R;

/**
 * Created by Genie on 7/8/2017.
 */

public class TimerTextViewFragment extends Fragment implements View.OnClickListener {
    private static final String EXTRA_INPUT_SOURCE = "extra_input_source";
    private TimerTextView timerTextView;
    private EditText input;
    private String inputSource;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            inputSource = savedInstanceState.getString(EXTRA_INPUT_SOURCE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_INPUT_SOURCE, inputSource);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        timerTextView = view.findViewById(R.id.timer);
        input = view.findViewById(R.id.input_time);

        view.findViewById(R.id.change_fragment).setOnClickListener(this);
        view.findViewById(R.id.set_input).setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_fragment:
                getFragmentManager().beginTransaction().replace(R.id.frame, new AnotherFragment()).addToBackStack(null).commit();
                break;
            case R.id.set_input:
                String source = input.getText().toString();
                if (!TextUtils.isEmpty(source.trim())) {
                    inputSource = source;
                    timerTextView.setSourceTime(source);
                }
                break;
        }
    }
}
