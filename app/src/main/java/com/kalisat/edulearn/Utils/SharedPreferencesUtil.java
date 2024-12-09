package com.kalisat.edulearn.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

    private static final String PREFS_NAME = "app_prefs";
    private static final String FOTO_KEY = "saved_foto_base64";

    // Menyimpan gambar base64 ke SharedPreferences
    public static void saveImageToPreferences(Context context, String fotoBase64) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FOTO_KEY, fotoBase64);
        editor.apply();
    }

    // Mengambil gambar base64 dari SharedPreferences
    public static String getSavedImageFromPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getString(FOTO_KEY, "");
    }
}
