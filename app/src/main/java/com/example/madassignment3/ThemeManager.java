// ThemeManager.java
package com.example.madassignment3;

import android.content.Context;
import android.content.SharedPreferences;

public class ThemeManager {
    private static final String PREF_NAME = "theme_prefs";
    private static final String KEY_THEME = "selected_theme";

    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;
    public static final int THEME_CUSTOM = 2;

    public static void setTheme(Context context, int theme) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_THEME, theme).apply();
    }

    public static int getTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_THEME, THEME_LIGHT);
    }

    public static void applyTheme(Context context) {
        int theme = getTheme(context);
        switch (theme) {
            case THEME_DARK:
                context.setTheme(R.style.AppTheme_Dark);
                break;
            case THEME_CUSTOM:
                context.setTheme(R.style.AppTheme_Custom);
                break;
            default:
                context.setTheme(R.style.AppTheme_Light);
        }
    }
}