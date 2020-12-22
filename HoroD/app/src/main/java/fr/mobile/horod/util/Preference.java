package fr.mobile.horod.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preference {
    private static final String PREFERENCE_CITY = "city";

    private static SharedPreferences getPreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setCity(Context context, String city){
        getPreference(context)
                .edit()
                .putString(PREFERENCE_CITY, city)
                .apply();
    }
    public static String getCity(Context context){
        return getPreference(context).getString(PREFERENCE_CITY, null);
    }
}
