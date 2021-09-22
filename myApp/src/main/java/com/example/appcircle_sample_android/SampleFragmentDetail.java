package com.example.appcircle_sample_android;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class SampleFragmentDetail extends Fragment {
    MainActivity activity;

    public SampleFragmentDetail() {
        // Required empty public constructor
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sample_detail, container, false);

        activity.showHomeItem(true);
        TextView textView = view.findViewById(R.id.textView);
        String apiUrl = BuildConfig.APPCIRCLE_API_URL;
        textView.setText("Api URL: " + apiUrl);

        if(URLValidator.isValid(apiUrl)) {
            Toast.makeText(activity, "The URL " + apiUrl + " is valid", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, "The URL " + apiUrl + " isn't valid", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
