package example.com.hop.myinstagram.main;

import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import example.com.hop.myinstagram.R;
import example.com.hop.myinstagram.utils.SlidingTabLayout;
import example.com.hop.myinstagram.utils.ViewPagerAdapter;

/**
 * Created by Hop on 24/06/2015.
 */
public class CameraActivity extends FragmentActivity {
    public static final int NUM_OF_TABS = 3;
    Camera camera;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    SlidingTabLayout slideTabs;

    @Override
    protected void onResume() {
        super.onResume();
        hideStatusBar();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);


        viewPager = (ViewPager) findViewById(R.id.camera_viewpager);
        slideTabs = (SlidingTabLayout) findViewById(R.id.camera_sliding_tab);

        CharSequence[] titles = {"GALLERY", "PHOTO", "VIDEO"};

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), titles);
        viewPager.setAdapter(viewPagerAdapter);

        slideTabs.setDistributeEvenly(true);

        //Set mau cho tab indicator: la cai thanh nam nganng o bottom
        slideTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tab_indicator);
            }
        });

        slideTabs.setViewPager(viewPager);


    }

    private void hideStatusBar() {
        int uioption = getWindow().getDecorView().getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= 14) {
            uioption ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            uioption ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }


        if (Build.VERSION.SDK_INT >= 18) {
            uioption ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getWindow().getDecorView().setSystemUiVisibility(uioption);
    }


}
