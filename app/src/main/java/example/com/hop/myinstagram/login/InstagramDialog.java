package example.com.hop.myinstagram.login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Hop on 13/07/2015.
 */
public class InstagramDialog extends Dialog {
    private final String TAG = this.getClass().getSimpleName();
    private String mUrl;
    private ProgressDialog mProgressDialog;
    private WebView mWebview;
    private LinearLayout mContent;
    private OAuthDialogListener mListener;

    public InstagramDialog(Context context, String url, OAuthDialogListener listener) {
        super(context, android.R.style.Theme_Light_NoTitleBar);
        this.mUrl = url;
        this.mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createProgressDialog();
        mContent = new LinearLayout(getContext());
        addContentView(mContent, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        setUpWebView();

        //Neu ta ko remove cookie, lan dang nhap sau se lay lai thong tin lan truoc, va gui
        // len server luon chu ko co hien bang nhap username password nua
        CookieSyncManager.createInstance(getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    private void setUpWebView() {
        mWebview = new WebView(getContext());
        mWebview.setWebViewClient(new OAuthWebViewClient());
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.loadUrl(mUrl);
        mWebview.setLayoutParams(new
                RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mContent.addView(mWebview);
    }


    private void createProgressDialog() {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
    }

    private class OAuthWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //Dieu huong du lieu nhan tu server trong ham nay
            Log.d(TAG, "Redirect URL: " + url);
            if (url.startsWith(InstagramApp.mCallbackUrl)) {
                String[] urls = url.split("=");
                String accessToken = urls[1];
                mListener.onComplete(accessToken);
                dismiss();
                Log.d(TAG, InstagramApp.mCallbackUrl);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d(TAG, "Loading URL: " + url);
            mProgressDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "onPageFnished URL: " + url);
            mProgressDialog.dismiss();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.d(TAG, "Page Error: " + description);
            mListener.onError(description);
            mProgressDialog.dismiss();
        }
    }

    public interface OAuthDialogListener {
        public abstract void onComplete(String accessToken);

        public abstract void onError(String error);
    }
}
