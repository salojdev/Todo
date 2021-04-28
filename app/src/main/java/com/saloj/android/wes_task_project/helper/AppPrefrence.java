package com.saloj.android.wes_task_project.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPrefrence {

    // Shared Preferences
    private SharedPreferences pref;

    // Editor for Shared preferences
    private SharedPreferences.Editor editor;

    // Context
    private Context _context;

    private static final String PREF_NAME = "TODO_APP_PREF";

    public static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    public static final String KEY_MOBILE = "mobile";
    private static final String KEY_USER_IMAGE = "user_image";
    public static final String KEY_EMAIL = "email";


    public AppPrefrence(Context context){
     //   this._context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setIsLogin(String  value) {
        editor.putString(IS_LOGIN, value);
        editor.commit();
    }

    public String getIsLogin() {
        if (pref != null) {
            return pref.getString(IS_LOGIN,"");
        }
        return "";
    }

    public void setUserID(String value) {
        editor.putString(KEY_USER_ID, value);
        editor.commit();
    }

    public String getUserID() {
        if (pref != null) {
            return pref.getString(KEY_USER_ID, "");
        }
        return "";
    }

    public void setEmail(String value) {
        editor.putString(KEY_EMAIL, value);
        editor.commit();
    }

    public String getEmail() {
        if (pref != null) {
            return pref.getString(KEY_EMAIL, "");
        }
        return "";
    }

    public void setUsername(String value) {
        editor.putString(KEY_USER_NAME, value);
        editor.commit();
    }

    public String getUsername() {
        if (pref != null) {
            return pref.getString(KEY_USER_NAME, "");
        }
        return "";
    }


    public void setMobile(String value) {
        editor.putString(KEY_MOBILE, value);
        editor.commit();
    }

    public String getMobile() {
        if (pref != null) {
            return pref.getString(KEY_MOBILE, "");
        }
        return "";
    }

}

