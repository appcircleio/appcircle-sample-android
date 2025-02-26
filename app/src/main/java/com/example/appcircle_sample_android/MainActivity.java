package com.example.appcircle_sample_android;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import android.os.AsyncTask;
import java.io.IOException;
import java.util.List;
import android.content.Intent;
import android.net.Uri;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;


public class MainActivity extends AppCompatActivity {
    private AppService appService = new AppService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showHomeItem(true);
        SampleFragment newFragment = new SampleFragment();
        newFragment.setActivity(this);
        this.replaceFragment(newFragment, false);

        new GetAccessTokenTask().execute();
    }

    private void showUpdateDialog(final String storePrefix, final String profileId, final AppVersion appVersion, final String accessToken, final String userEmail) {
        new AlertDialog.Builder(this)
                .setTitle("Update Available")
                .setMessage(appVersion.getVersion() + " version is available. Do you want to update?")
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String baseDownloadURL = "https://%s.store.appcircle.io/api/profile/%s/appVersions/%s/download-update/%s/user/%s";
                        Uri downloadURL =  Uri.parse(String.format(baseDownloadURL, storePrefix, profileId, appVersion.getId(), accessToken, userEmail));
                        Intent intent = new Intent(Intent.ACTION_VIEW, downloadURL);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Code to run when "Cancel" is pressed
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private class GetAccessTokenTask extends AsyncTask<String, Void, AuthModel> {
        @Override
        protected AuthModel doInBackground(String... params) {
            try {
                AuthModel response = AuthService.getAccessToken();
                fetchAppVersions(response.getAccessToken(), Environment.PROFILE_ID);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private void fetchAppVersions(final String accessToken, final String profileId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<AppVersion> appVersions = appService.getAppVersions(accessToken, profileId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String versionName = getAppVersionName();

                            if (versionName != null) {
                                VersionUtils versionUtils = new VersionUtils();

                                @Nullable  AppVersion latestVersion = versionUtils.getLatestVersion(versionName, appVersions);
                                if (latestVersion != null) {
                                    showUpdateDialog(Environment.STORE_PREFIX, profileId, latestVersion, accessToken, "USER_EMAIL");
                                }
                            } else {
                                Log.d("MainActivity", "Current Version Not Found");
                            }
                        }
                    });
                } catch (final IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("APIError", "Error fetching app versions", e);
                        }
                    });
                }
            }
        }).start();
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.rootLayout, fragment);
        if(addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void showHomeItem(boolean isEnabled) {
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(isEnabled);
        this.getSupportActionBar().setDisplayShowHomeEnabled(isEnabled);
    }

    public String getAppVersionName() {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            return  pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "NOT_FOUND";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
