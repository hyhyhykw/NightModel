package com.xxx.nightmodel.utils;

import android.text.TextUtils;

import com.orhanobut.hawk.Hawk;

/**
 * Created by like on 16/7/21.
 */
public class DayNightUtils {
    public static final String BRIGHTNESS = "brightness";
    public static final String DAY = "day";
    public static final String NIGHT = "night";

    public static boolean isNightModel() {
        return TextUtils.equals(Hawk.get(BRIGHTNESS, DAY), NIGHT);
    }

    public static void setNightModel(boolean isNight) {
        Hawk.put(BRIGHTNESS, isNight ? NIGHT : DAY);
    }
}
