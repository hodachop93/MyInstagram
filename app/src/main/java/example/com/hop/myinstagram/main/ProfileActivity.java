package example.com.hop.myinstagram.main;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import example.com.hop.myinstagram.R;
import example.com.hop.myinstagram.profile.ProfileFragment;

/**
 * Created by Hop on 24/06/2015.
 */
public class ProfileActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ProfileFragment fragment = new ProfileFragment();
        transaction.add(R.id.profile_activity_container, fragment);
        transaction.commit();
    }

}
