package com.zyphenvisuals.tweeter.background;

import com.google.gson.Gson;
import com.zyphenvisuals.tweeter.model.AuthToken;
import com.zyphenvisuals.tweeter.network.NetworkController;
import com.zyphenvisuals.tweeter.router.RouterController;
import com.zyphenvisuals.tweeter.router.RouterPath;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TokenUpdater {
    private static final Timer timer = new Timer();
    private static final Gson gson = new Gson();

    private static void updateToken(){
        // guard clause
        if(NetworkController.getToken() == null){
            return;
        }

        HttpResponse<String> res = NetworkController.sendPostRequest("/user/token", List.of());

        if(res.statusCode() == 200){
            AuthToken token = gson.fromJson(res.body(), AuthToken.class);
            NetworkController.setToken(token.getToken());
            System.out.println("Token refreshed in the background.");
        } else {
            System.out.println("Token refreshed failed.");
            NetworkController.resetToken();
            RouterController.goTo(RouterPath.LOGIN);
        }
    }

    public static void start() {
        System.out.println("Starting TokenUpdater in the background.");

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateToken();
            }
        }, Duration.ofMinutes(10).toMillis(), Duration.ofMinutes(10).toMillis());
    }

    public static void stop() {
        timer.cancel();
    }
}
