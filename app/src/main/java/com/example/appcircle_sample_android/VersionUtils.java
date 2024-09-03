package com.example.appcircle_sample_android;

import androidx.annotation.Nullable;
import java.util.List;
import java.util.ArrayList;

/*
    You can implement your custom update check mechanism within this function.
    Currently, we convert the version to an integer and compare it with the 'CFBundleShortVersionString'.
    You may want to check other datas about the app version to write the update control mechanism please check
    /v2/profiles/{profileId}/app-versions at https://api.appcircle.io/openapi/index.html?urls.primaryName=store
*/
public class VersionUtils {
    private List<Integer> versionComponents(String version) {
        List<Integer> components = new ArrayList<>();
        String[] parts = version.split("\\.");
        for (String part : parts) {
            try {
                components.add(Integer.parseInt(part));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return components;
    }


    public @Nullable AppVersion getLatestVersion(String currentVersion, List<AppVersion> appVersions) {
        AppVersion latestAppVersion = null;
        List<Integer> currentComponents = versionComponents(currentVersion);

        for (AppVersion app : appVersions) {
            List<Integer> latestComponents = versionComponents(app.getVersion());
            boolean isNewerVersion = false;

            for (int i = 0; i < Math.min(currentComponents.size(), latestComponents.size()); i++) {
                int current = currentComponents.get(i);
                int latest = latestComponents.get(i);
                if (latest > current && app.getPublishType() != 0) {
                    isNewerVersion = true;
                    break;
                } else if (latest < current) {
                    break;
                }
            }

            if (isNewerVersion) {
                latestAppVersion = app;
            }
        }

        return latestAppVersion;
    }
}
