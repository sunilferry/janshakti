package apps.janshakti.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSession {
    //Declaration of variables
    private static final String SESSION_NAME = "apps.nms";
    private static final String APP_DEFAULT_LANGUAGE = "en";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor prefsEditor;

    //Constructor
    public AppSession(Context context) {
        mSharedPreferences = context.getSharedPreferences(SESSION_NAME, Context.MODE_PRIVATE);
        prefsEditor = mSharedPreferences.edit();
    }

    public boolean isLogin() {
        return mSharedPreferences.getBoolean("Login", false);
    }

    public void setLogin(boolean Login) {
        prefsEditor.putBoolean("Login", Login).apply();
    }

    public String getAccessToken() {
        return mSharedPreferences.getString("getAccessToken", "");
    }

    public void setAccessToken(String accessToken) {
        prefsEditor.putString("getAccessToken", accessToken).apply();
    }

    public String getPersonId() {
        return mSharedPreferences.getString("personId", "");
    }

    public void setPersonId(String faceId) {
        prefsEditor.putString("personId", faceId).apply();
    }

    public String getGroupId() {
        return mSharedPreferences.getString("groupId", "");
    }

    public void setGroupId(String faceId) {
        prefsEditor.putString("groupId", faceId).apply();
    }

    public String getMobile() {
        return mSharedPreferences.getString("getMobile", "");
    }

    public void setMobile(String mobile) {
        prefsEditor.putString("getMobile", mobile).apply();
    }


    public String getUsername() {
        return mSharedPreferences.getString("getUsername", "");
    }

    public void setUsername(String username) {
        prefsEditor.putString("getUsername", username).apply();
    }

    public String getEmailAddress() {
        return mSharedPreferences.getString("getEmailAddress", "");
    }

    public void setEmailAddress(String emailAddress) {
        prefsEditor.putString("getEmailAddress", emailAddress).apply();
    }

    public String getImage() {
        return mSharedPreferences.getString("image", "");
    }

    public void setImage(String image) {
        prefsEditor.putString("image", image).apply();
    }

    public String getOrganization() {
        return mSharedPreferences.getString("organization", "");
    }

    public void setOrganization(String organization) {
        prefsEditor.putString("organization", organization).apply();
    }

    public String isAppointed() {
        return mSharedPreferences.getString("appoint", "");
    }

    public void setAppoint(String appoint) {
        prefsEditor.putString("appoint", appoint).apply();
    }

}
