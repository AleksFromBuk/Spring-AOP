package com.springaopopenschool1.firsttask.utils;

public class ThreadUtils {

    private ThreadUtils() {

    }

    public static void waitTime(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
