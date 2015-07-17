package example.com.hop.myinstagram.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import example.com.hop.myinstagram.utils.ConnectingNetwork;

/**
 * Created by Hop on 13/07/2015.
 */
public class InstagramApp {
    private final String TAG = this.getClass().getSimpleName();
    public static String mCallbackUrl = "";
    private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    private static final String API_URL = "https://api.instagram.com/v1";
    //Dinh nghia 1 so code dung trong Handler
    private static int WHAT_FINALIZE = 0;
    private static int WHAT_ERROR = 1;
    private static int WHAT_FETCH_INFO = 2;
    //Dinh nghia 1 so ar1 trong Handler
    private static int ARG1_IN_ACCESS_TOKEN = 101;
    private static int ARG1_IN_GET_INFOR = 102;

    private InstagramSession mSession;
    private InstagramDialog mDialog;
    private OAuthAuthenticationListener mListener;
    private ProgressDialog mProgressDialog;
    private String mClientID;
    private String mClientSecret;
    private Context mContext;
    private String mAuthUrl;
    private String mTokenUrl;
    private String mAccessToken;
    private Handler mHandler;

    public InstagramApp() {
    }

    public InstagramApp(Context context, String clientID, String clienSecret, String callbackUrl) {
        this.mContext = context;
        this.mClientID = clientID;
        this.mClientSecret = clienSecret;
        this.mCallbackUrl = callbackUrl;
        this.mSession = new InstagramSession(mContext);
        this.mAccessToken = this.mSession.getAccessToken();
        this.mAuthUrl = AUTH_URL
                + "?client_id="
                + mClientID
                + "&redirect_uri="
                + mCallbackUrl
                + "&response_type=code&display=touch&scope=likes+comments+relationships";
        mTokenUrl = TOKEN_URL + "?client_id=" + mClientID + "&client_secret="
                + mClientSecret + "&redirect_uri=" + mCallbackUrl
                + "&grant_type=authorization_code";
        InstagramDialog.OAuthDialogListener ADListener = new InstagramDialog.OAuthDialogListener() {
            @Override
            public void onComplete(String code) {
                getAccessToken(code);
            }

            @Override
            public void onError(String error) {
                mListener.onFail("Authorization failed");
            }
        };
        mDialog = new InstagramDialog(mContext, mAuthUrl, ADListener);
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == WHAT_ERROR) {
                    mProgressDialog.dismiss();
                    if (msg.arg1 == ARG1_IN_ACCESS_TOKEN)
                        mListener.onFail("Failed to get access token");
                    else if (msg.arg1 == ARG1_IN_GET_INFOR)
                        mListener.onFail("Failed to get user information");
                } else if (msg.what == WHAT_FETCH_INFO) {
                    mProgressDialog.dismiss();
                    mListener.onSuccess();
                }
            }
        };
    }

    private void getAccessToken(final String code) {
        //Sau khi da get duoc code ve tu server, tien hanh get accessToken
        mProgressDialog.setMessage("Getting access token...");
        mProgressDialog.show();
        Thread thread = new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "Getting access token");
                int what = WHAT_FETCH_INFO;
                try {
                    ConnectingNetwork conn = ConnectingNetwork.getInstance();
                    ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
                    pairs.add(new BasicNameValuePair("client_id", mClientID));
                    pairs.add(new BasicNameValuePair("client_secret", mClientSecret));
                    pairs.add(new BasicNameValuePair("grant_type", "authorization_code"));
                    pairs.add(new BasicNameValuePair("redirect_uri", mCallbackUrl));
                    pairs.add(new BasicNameValuePair("code", code));
                    String receivedStr = conn.executePost(TOKEN_URL, pairs);
                    Log.d(TAG, receivedStr);
                    JSONObject jsonObj = new JSONObject(receivedStr);
                    mAccessToken = jsonObj.getString("access_token");
                    Log.d(TAG, mAccessToken);
                    JSONObject jsonObjUser = jsonObj.getJSONObject("user");
                    String username = jsonObjUser.getString("username");
                    String userid = jsonObjUser.getString("id");
                    String fullname = jsonObjUser.getString("full_name");
                    mSession.storeAccessToken(username, mAccessToken, userid, fullname);

                } catch (Exception ex) {
                    what = WHAT_ERROR;
                    ex.printStackTrace();
                }
                int arg1 = ARG1_IN_ACCESS_TOKEN;
                mHandler.sendMessage(mHandler.obtainMessage(what, arg1, 0));
            }
        };
        thread.start();
    }

    public void authorize() {
        mDialog.show();
    }

    public boolean hasAccessToken() {
        return (mAccessToken != null) ? true : false;
    }

    public void setOAuthAthenticationListener(OAuthAuthenticationListener listener) {
        mListener = listener;
    }

    public interface OAuthAuthenticationListener {
        public abstract void onSuccess();

        public abstract void onFail(String error);
    }
}
