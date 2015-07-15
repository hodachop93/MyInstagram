package example.com.hop.myinstagram.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import example.com.hop.myinstagram.R;
import example.com.hop.myinstagram.login.AppData;
import example.com.hop.myinstagram.login.InstagramApp;

/**
 * Created by Hop on 13/07/2015.
 */
public class LoginActivity extends Activity implements View.OnClickListener{
    private LinearLayout btnLogin;
    private InstagramApp mApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        btnLogin = (LinearLayout) findViewById(R.id.login);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnLogin.setOnClickListener(this);
        mApp = new InstagramApp(this, AppData.CLIENT_ID,AppData.CLIENT_SECRET, AppData.CALLBACK_URL);
        InstagramApp.OAuthAuthenticationListener listener = new InstagramApp.OAuthAuthenticationListener() {
            @Override
            public void onSuccess() {
                openMainActivity();
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
            }
        };
        mApp.setOAuthAthenticationListener(listener);

        //Kiem tra thu da dang nhap hay chua
        if (mApp.hasAccessToken()){
            //Neu da dang nhap roi thi chay vao main activity
            openMainActivity();
        }
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onClick(View v) {
        startConnecting();
    }

    private void startConnecting() {
        mApp.authorize();
    }
}
