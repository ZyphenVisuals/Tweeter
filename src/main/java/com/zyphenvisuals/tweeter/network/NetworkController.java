package com.zyphenvisuals.tweeter.network;

import com.google.gson.Gson;
import com.zyphenvisuals.tweeter.model.UrlParam;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class NetworkController {
    // configs
    @Setter
    @Getter
    private static String token = null;

    private static final String baseUrl = "https://tweeter.zyphenvisuals.com/api/v1";

    // clients and stuff
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public static void resetToken() {
        NetworkController.token = null;
    }

    public static HttpResponse<String> sendPostRequest(String path, Object body)  {
        try {
            HttpRequest.Builder request = HttpRequest.newBuilder();

            // add url
            request.uri(new URI(baseUrl + path));

            // serialize body
            String jsonBody = gson.toJson(body);

            // add method and body
            request.POST(HttpRequest.BodyPublishers.ofString(jsonBody));

            // add authorization if available
            if (token != null) {
                request.header("Authorization", "Bearer " + token);
            }

            // add content headers
            request.header("Content-Type", "application/json");
            request.header("Accept", "application/json");

            // build the request
            HttpRequest finalRequest = request.build();

            // send it
            return client.send(finalRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static HttpResponse<String> sendGetRequest(String path, List<UrlParam> params) {
        try {
            HttpRequest.Builder request = HttpRequest.newBuilder();

            // add params to the url
            if (!params.isEmpty()) {
                StringBuilder pathBuilder = new StringBuilder(path);
                pathBuilder.append("?");
                for (UrlParam param : params) {
                    pathBuilder.append(param.getKey());
                    pathBuilder.append("=");
                    pathBuilder.append(param.getValue());
                    pathBuilder.append("&");
                }
                pathBuilder.deleteCharAt(pathBuilder.length() - 1);
                path = pathBuilder.toString();
            }

            // add url
            request.uri(new URI(baseUrl + path));

            // add method
            request.GET();

            // add authorization if available
            if (token != null) {
                request.header("Authorization", "Bearer " + token);
            }

            // add content headers
            request.header("Accept", "application/json");

            // build the request
            HttpRequest finalRequest = request.build();

            // send it
            return client.send(finalRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
