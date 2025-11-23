package com.example.brawlmovilv01.Sesion;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "AppSessionPref";
    private static final String KEY_AUTH_TOKEN = "user_auth_token";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_ID = "user_id";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void createLoginSession(String token, String name, int id) {
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.putString(KEY_USER_NAME, name);
        editor.putInt(KEY_USER_ID, id);
        editor.apply();
    }

    public void logoutUser() {
        editor.remove(KEY_AUTH_TOKEN);
        editor.remove(KEY_USER_NAME);
        editor.remove(KEY_USER_ID);
        editor.clear();
        editor.apply();
    }

    public boolean isLoggedIn() {
        return fetchAuthToken() != null;
    }

    public void saveAuthToken(String token) {
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.apply();
    }

    public String fetchAuthToken() {
        return prefs.getString(KEY_AUTH_TOKEN, null);
    }

    public void saveUserName(String name) {
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }

    public String fetchUserName() {
        return prefs.getString(KEY_USER_NAME, null);
    }

    public void saveUserId(int id) {
        editor.putInt(KEY_USER_ID, id);
        editor.apply();
    }

    public int fetchUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }
}