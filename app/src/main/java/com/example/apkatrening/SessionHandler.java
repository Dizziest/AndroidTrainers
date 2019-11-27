package com.example.apkatrening;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.Date;

public class SessionHandler {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_EMPTY = "";
    private static final String KEY_IMIE = "imie";
    private static final String KEY_NAZWISKO = "nazwisko";
    private static final String KEY_WIEK = "wiek";
    private static final String KEY_EXPERIENCE = "doswiadczenie";
    private static final String KEY_ID = "id";
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public SessionHandler(Context mContext){
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }


    public void loginUser(String login, String email, String imie, String nazwisko, int wiek, int id){
        mEditor.putString(KEY_LOGIN, login);
        mEditor.putString(KEY_EMAIL, email);
        mEditor.putString(KEY_IMIE, imie);
        mEditor.putString(KEY_NAZWISKO, nazwisko);
        mEditor.putInt(KEY_WIEK, wiek);
        mEditor.putInt(KEY_ID, id);
        Date date = new Date();

        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }


    public boolean isLoggedIn(){
        Date currentDate = new Date();
        long millis = mPreferences.getLong(KEY_EXPIRES, 0);

        if (millis == 0){
            return false;
        }
        Date expiryDate = new Date(millis);

        return currentDate.before(expiryDate);
    }

    public User getUserDetails(){
        if (!isLoggedIn()){
            return null;
        }

        User user = new User();
        user.setLogin(mPreferences.getString(KEY_LOGIN, KEY_EMPTY));
        user.setEmail(mPreferences.getString(KEY_EMAIL, KEY_EMPTY));
        user.setAge(mPreferences.getInt(KEY_WIEK, 0));
        user.setExperience(mPreferences.getInt(KEY_EXPERIENCE, 0));
        user.setName(mPreferences.getString(KEY_IMIE, KEY_EMPTY));
        user.setSurname(mPreferences.getString(KEY_NAZWISKO, KEY_EMPTY));
        user.setSessionExpiryDate(new Date(mPreferences.getLong(KEY_EXPIRES, 0)));
        user.setId(mPreferences.getInt(KEY_ID, 0));

        return user;
    }

    public void logoutUser(){
        mEditor.clear();
        mEditor.commit();
    }
}
