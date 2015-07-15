package example.com.hop.myinstagram.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Class nay dung de quan ly username va access token bang SharedPreferences
 * Created by Hop on 13/07/2015.
 */
public class InstagramSession {
    private SharedPreferences sharedPref;
    private Editor editor;
    private static final String SHARED_PREF_NAME = "Instagram_Preferences";
    private static final String INSTAGRAM_USERNAME = "username";
    private static final String INSTAGRAM_ACCESS_TOKEN = "access_token";

    public InstagramSession(Context mContext) {
        sharedPref = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void storeAccessToken(String username, String access_token) {
        editor.putString(INSTAGRAM_USERNAME, username);
        editor.putString(INSTAGRAM_ACCESS_TOKEN, access_token);
        //lenh commit de tien hanh luu trang thai
        editor.commit();
    }

    public void resetAccessToken() {
        editor.putString(INSTAGRAM_USERNAME, null);
        editor.putString(INSTAGRAM_ACCESS_TOKEN, null);
        editor.commit();
    }

    public String getUsername() {
        return sharedPref.getString(INSTAGRAM_USERNAME, null);
    }

    public String getAccessToken() {
        return sharedPref.getString(INSTAGRAM_ACCESS_TOKEN, null);
    }
}
