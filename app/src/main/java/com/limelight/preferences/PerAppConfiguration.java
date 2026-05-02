package com.limelight.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class PerAppConfiguration {
    private static final String PREFS_NAME = "PerAppSettings";

    public int width; // 0 = no override
    public int height; // 0 = no override
    public int fps; // 0 = no override
    public int bitrate; // 0 = no override (kbps)

    private static String prefix(String uuid, int appId) {
        return uuid + "_" + appId;
    }

    public static PerAppConfiguration readOverride(Context context, String uuid, int appId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String keyPrefix = prefix(uuid, appId);
        PerAppConfiguration config = new PerAppConfiguration();
        config.width = prefs.getInt(keyPrefix + "_w", 0);
        config.height = prefs.getInt(keyPrefix + "_h", 0);
        config.fps = prefs.getInt(keyPrefix + "_fps", 0);
        config.bitrate = prefs.getInt(keyPrefix + "_bitrate", 0);
        return config;
    }

    public static void saveOverride(Context context, String uuid, int appId, PerAppConfiguration override) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        String keyPrefix = prefix(uuid, appId);
        if (override.width > 0) {
            editor.putInt(keyPrefix + "_w", override.width);
            editor.putInt(keyPrefix + "_h", override.height);
        } else {
            editor.remove(keyPrefix + "_w");
            editor.remove(keyPrefix + "_h");
        }
        if (override.fps > 0) {
            editor.putInt(keyPrefix + "_fps", override.fps);
        } else {
            editor.remove(keyPrefix + "_fps");
        }
        if (override.bitrate > 0) {
            editor.putInt(keyPrefix + "_bitrate", override.bitrate);
        } else {
            editor.remove(keyPrefix + "_bitrate");
        }
        editor.apply();
    }

    public static void deleteOverride(Context context, String uuid, int appId) {
        String keyPrefix = prefix(uuid, appId);
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
                .remove(keyPrefix + "_w")
                .remove(keyPrefix + "_h")
                .remove(keyPrefix + "_fps")
                .remove(keyPrefix + "_bitrate")
                .apply();
    }

    public void applyTo(PreferenceConfiguration config) {
        if (width > 0 && height > 0) {
            config.width = width;
            config.height = height;
        }
        if (fps > 0) {
            config.fps = fps;
        }
        if (bitrate > 0) {
            config.bitrate = bitrate;
        }
    }
}
