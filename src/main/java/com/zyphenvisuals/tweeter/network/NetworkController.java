package com.zyphenvisuals.tweeter.network;

import com.google.gson.Gson;
import com.zyphenvisuals.tweeter.model.UrlParam;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class NetworkController {
    // configs
    private static String token;
    private static final String baseUrl = "https://zyphen.tweeter.com/api/v1";

    // clients and stuff
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public static void setToken(String token) {
        NetworkController.token = token;
    }

    public static String sendPostRequest(String path, Object body)  {
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

            // build the request
            HttpRequest finalRequest = request.build();

            // send it
            HttpResponse<String> response = client.send(finalRequest, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200 ? response.body() : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String sendGetRequest(String path, List<UrlParam> params) {
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

            // build the request
            HttpRequest finalRequest = request.build();

            // send it
            HttpResponse<String> response = client.send(finalRequest, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200 ? response.body() : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
