package com.example.appcircle_sample_android;

import okhttp3.Request;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.HttpUrl;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

class AppVersion {
    private String id;
    private String version;
    private Integer publishType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getPublishType() {
        return publishType;
    }

    public void setPublishType(Integer publishType) {
        this.publishType = publishType;
    }
}

public class AppService {
    private static final String BASE_URL = "https://api.appcircle.io";
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    public List<AppVersion> getAppVersions(String accessToken, String profileId) throws IOException {
        HttpUrl url = HttpUrl.parse("https://" + Environment.STORE_URL + "/api/app-versions");

        if (url == null) {
            throw new IOException("Invalid URL");
        }

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "*/*")
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            System.out.println("Response: " + responseBody); // For debugging

            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonArray dataArray = jsonObject.getAsJsonArray("data");

            Type listType = new TypeToken<List<AppVersion>>() {}.getType();
            return gson.fromJson(dataArray, listType);
        }
    }
}

