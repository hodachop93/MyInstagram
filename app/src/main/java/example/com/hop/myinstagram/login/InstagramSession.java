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
    private static final String INSTAGRAM_USERID="user_id";
    private static final String INSTAGRAM_FULLNAME="full_name";

    public InstagramSession(Context mContext) {
        sharedPref = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void storeAccessToken(String username, String access_token,String userid, String fullname ) {
        editor.putString(INSTAGRAM_USERNAME, username);
        editor.putString(INSTAGRAM_ACCESS_TOKEN, access_token);
        editor.putString(INSTAGRAM_USERID, userid);
        editor.putString(INSTAGRAM_FULLNAME, fullname);
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

    public String getInstagramUserID() {
        return sharedPref.getString(INSTAGRAM_USERID, null);
    }

    public String getInstagramFullName() {
        return sharedPref.getString(INSTAGRAM_FULLNAME, null);
    }
}
