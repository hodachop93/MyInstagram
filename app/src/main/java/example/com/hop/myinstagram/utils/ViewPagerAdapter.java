package example.com.hop.myinstagram.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import example.com.hop.myinstagram.camera.CameraFragment;
import example.com.hop.myinstagram.camera.GalleryFragment;
import example.com.hop.myinstagram.camera.VideoFragment;
import example.com.hop.myinstagram.main.CameraActivity;

/**
 * Created by Hop on 28/05/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    //This array will store the Titles of the Tabs which are going to be passed when View Pager Adapter is created
    private CharSequence[] titles;

    public ViewPagerAdapter(FragmentManager fm, CharSequence[] titles) {
        super(fm);
        this.titles = titles;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                GalleryFragment tab0 = new GalleryFragment();
                return tab0;
            case 1:
                CameraFragment tab1 = new CameraFragment();
                return tab1;
            case 2:
                VideoFragment tab2 = new VideoFragment();
                return tab2;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return CameraActivity.NUM_OF_TABS;
    }

    // This method return the titles for the Tabs in the Tab Strip
    //This method will return the titles for the Tabs in the tab strip
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
