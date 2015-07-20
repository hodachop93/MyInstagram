package example.com.hop.myinstagram.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import example.com.hop.myinstagram.R;
import example.com.hop.myinstagram.news.FragmentFollowing;
import example.com.hop.myinstagram.news.FragmentYou;
import example.com.hop.myinstagram.utils.SlidingTabLayout;
import example.com.hop.myinstagram.utils.ViewPagerAdapter;

/**
 * Created by Hop on 24/06/2015.
 */
public class NewsActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private ViewPagerAdapter mVPAdapter;
    private SlidingTabLayout mSlidingTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        initialize();
    }

    private void initialize() {
        mViewPager = (ViewPager) findViewById(R.id.pager);

        // names of tabs
        CharSequence[] titles = {"FOLLOWING", "YOU"};
        // tabs
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new FragmentFollowing());
        fragments.add(new FragmentYou());

        // init & set adapter
        mVPAdapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, fragments);
        mViewPager.setAdapter(mVPAdapter);

        // set view pager to sliding tabs
        mSlidingTab = (SlidingTabLayout) findViewById(R.id.tabs);
        mSlidingTab.setDistributeEvenly(true); // sacle full width
        mSlidingTab.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tab_indicator);
            }
        });
        mSlidingTab.setmTabStripColorID(R.color.tab_strip_selector_news);
        mSlidingTab.setViewPager(mViewPager);
    }
}
