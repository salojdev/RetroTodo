package com.saloj.prosaloj.todo.Common;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor prefsEditor;

    public static final String KEY_STATUS = "status";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_MobileNumber = "mobileNo";
    public static final String KEY_HASHKEY = "hashkey";

    public static final String KEY_UserName = "userName";

    public static final String KEY_METHODID = "locmethodid";
    public static final String KEY_METHODVALUE = "locmethodvalue";
    public static final String KEY_TOKEN = "accesstoken";
    public static final String KEY_LOGIN_DATE = "logindate";
    public static final String KEY_TIMEIN_LOGIN_STATUS = "loginstatus";


    public AppPreference(Context context) {
        sharedPreferences = context.getSharedPreferences("userPref", Context.MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();
    }

    public void setStatus(String value) {
        prefsEditor.putString(KEY_STATUS, value);
        prefsEditor.commit();
    }

    public String getStatus() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(KEY_STATUS, "");
        }
        return "";
    }

    public void setMobileNumber(String value) {
        prefsEditor.putString(KEY_MobileNumber, value);
        prefsEditor.commit();
    }

    public String getMobileNumber() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(KEY_MobileNumber, "");
        }
        return "";
    }


    public void setUsername(String name) {
        prefsEditor.putString(KEY_UserName, name);
        prefsEditor.commit();
    }

    public String getUsename() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(KEY_UserName, "");
        }
        return "";
    }


    public void setMethodID(String value) {
        prefsEditor.putString(KEY_METHODID, value);
        prefsEditor.commit();
    }

    public String getMethodID() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(KEY_METHODID, "");
        }
        return "";
    }

    public void setMethodValue(String value) {
        prefsEditor.putString(KEY_METHODVALUE, value);
        prefsEditor.commit();
    }

    public String getMethodValue() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(KEY_METHODVALUE, "");
        }
        return "";
    }

    public void setToken(String value) {
        prefsEditor.putString(KEY_TOKEN, value);
        prefsEditor.commit();
    }

    public String getToken() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(KEY_TOKEN, "");
        }
        return "";
    }

    public void setLoginDate(String value) {
        prefsEditor.putString(KEY_LOGIN_DATE, value);
        prefsEditor.commit();
    }

    public String getLoginDate() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(KEY_LOGIN_DATE, "");
        }
        return "";
    }

    public void setTimeInLoginStatus(String value) {
        prefsEditor.putString(KEY_TIMEIN_LOGIN_STATUS, value);
        prefsEditor.commit();
    }

    public String getTimeInLoginStatus() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(KEY_TIMEIN_LOGIN_STATUS, "");
        }
        return "";
    }

public void setHashkey(String value) {
        prefsEditor.putString(KEY_HASHKEY, value);
        prefsEditor.commit();
    }

    public String getHashkey() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(KEY_HASHKEY, "");
        }
        return "";
    }

public void setUserID(String value) {
        prefsEditor.putString(KEY_USER_ID, value);
        prefsEditor.commit();
    }

    public String getUserID() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(KEY_USER_ID, "");
        }
        return "";
    }

}
