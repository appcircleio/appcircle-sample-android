package com.example.appcircle_sample_android;

import okhttp3.*;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

class AuthModel {
    @SerializedName("access_token")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}

public class AuthService {
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public static AuthModel getAccessToken() throws IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(Environment.STORE_URL)
                .addPathSegment("api")
                .addPathSegment("auth")
                .addPathSegment("token")
                .build();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("ProfileId", Environment.PROFILE_ID);
            jsonBody.put("Secret", Environment.SECRET);
        } catch (JSONException e) {
            throw new IOException("Error creating JSON body", e);
        }

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonBody.toString()
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();
            System.out.println("Response: " + responseBody);

            Gson gson = new Gson();
            return gson.fromJson(responseBody, AuthModel.class);
        }
    }
}
