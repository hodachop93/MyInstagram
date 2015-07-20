package example.com.hop.myinstagram.main;

import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import example.com.hop.myinstagram.R;
import example.com.hop.myinstagram.camera.CameraFragment;
import example.com.hop.myinstagram.camera.GalleryFragment;
import example.com.hop.myinstagram.camera.VideoFragment;
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
    private List<Fragment> fragments;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);


        viewPager = (ViewPager) findViewById(R.id.camera_viewpager);
        slideTabs = (SlidingTabLayout) findViewById(R.id.camera_sliding_tab);

        CharSequence[] titles = {"GALLERY", "PHOTO", "VIDEO"};
        fragments = new ArrayList<Fragment>();
        fragments.add(new GalleryFragment());
        fragments.add(new CameraFragment());
        fragments.add(new VideoFragment());
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, fragments);
        viewPager.setAdapter(viewPagerAdapter);

        slideTabs.setDistributeEvenly(true);

        //Set mau cho tab indicator: la cai thanh nam nganng o bottom
        slideTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tab_indicator);
            }
        });
        slideTabs.setmTabStripColorID(R.color.tab_strip_selector_camera);

        slideTabs.setViewPager(viewPager);
        //hideStatusBar();


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
