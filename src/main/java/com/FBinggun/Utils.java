package com.FBinggun;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Utils {
    private Utils() {
    }

    public static FileConfiguration load(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public static String translateColorCode(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static long getExpiredTimeSec(int day) {
        long timeToAdd = TimeUnit.DAYS.toSeconds(day);
        return getCurrentTimeSec() + timeToAdd;
    }

    public static long getCurrentTimeSec() {
        return System.currentTimeMillis() / 1000;
    }

    public static int getDayRemain(long expiredTime) {
        if (expiredTime == 0) {
            return 0;
        }
        long timeRemainSec = expiredTime - getCurrentTimeSec();
        return Math.max(0, (int) (timeRemainSec / 24 / 60 / 60));
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
