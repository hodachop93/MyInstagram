package example.com.hop.myinstagram.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Hop on 28/05/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    //This array will store the Titles of the Tabs which are going to be passed when View Pager Adapter is created
    private CharSequence[] titles;
    private List<Fragment> fragments;

    public ViewPagerAdapter(FragmentManager fm, CharSequence[] titles, List<Fragment> fragments) {
        super(fm);
        this.titles = titles;
        this.fragments = fragments;
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    // This method return the titles for the Tabs in the Tab Strip
    //This method will return the titles for the Tabs in the tab strip
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
