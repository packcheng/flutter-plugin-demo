package com.packcheng.flutter_plugin_demo.bluetooth;

import android.util.Log;

public class DDLog {
    private static final boolean isDebug = true;
    private static final String TAG = "dinsafer";

    public DDLog() {
    }

    public static void d(String tag, String msg) {
        if (isDebug || Log.isLoggable(TAG, Log.WARN)) {
            Log.d(tag, formatMsg(tag, msg));
        }

    }

    public static void e(String tag, String msg) {
        if (isDebug || Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.e(tag, msg == null ? "" : formatMsg(tag, msg));
        }

    }

    public static void i(String tag, String msg) {
        if (isDebug || Log.isLoggable(TAG, Log.VERBOSE)) {
            if (msg == null) {
                return;
            }

            Log.i(tag, formatMsg(tag, msg));
        }

    }


    private static String formatMsg(String tag, String msg) {
        StringBuilder msgBuilder = new StringBuilder();
        msgBuilder.append("•");
        msgBuilder.append("\r\n");
        msgBuilder.append("╔════════════════════════════════════════════════════════════════════════════════════════");
        msgBuilder.append("\r\n");
        msgBuilder.append("║ Thread: ").append(Thread.currentThread().getName());
        msgBuilder.append("\r\n");
        msgBuilder.append("╟────────────────────────────────────────────────────────────────────────────────────────");
        msgBuilder.append("\r\n");
        msgBuilder.append("║ ").append(tag).append(" ").append(msg);
        msgBuilder.append("\r\n");
        msgBuilder.append("╚════════════════════════════════════════════════════════════════════════════════════════");
        msgBuilder.append("\r\n");
        return msg;
    }

}
