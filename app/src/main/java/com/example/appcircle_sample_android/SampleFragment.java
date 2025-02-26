package com.example.appcircle_sample_android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SampleFragment extends Fragment {
    MainActivity activity;
    Button nextPageBtn;

    public SampleFragment() {
        // Required empty public constructor
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sample, container, false);

        TextView version = view.findViewById(R.id.textView4);
        String packageName = "v" + activity.getAppVersionName();
        version.setText(packageName);

        activity.showHomeItem(false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
