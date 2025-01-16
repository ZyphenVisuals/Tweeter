package com.zyphenvisuals.tweeter.background;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DateUpdater {
    private static final List<Runnable> updateTasks = new ArrayList<>();
    private static final Timer timer = new Timer();

    public static void addUpdateTask(Runnable runnable) {
        updateTasks.add(runnable);
    }

    public static void reset() {
        updateTasks.clear();
    }

    public static void start() {
        System.out.println("Starting DateUpdater in the background.");

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Runnable runnable : updateTasks) {
                    runnable.run();
                }
            }
        }, Duration.ofSeconds(1).toMillis(), Duration.ofSeconds(1).toMillis());
    }

    public static void stop() {
        timer.cancel();
    }
}
