package example.com.hop.myinstagram.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import example.com.hop.myinstagram.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Hop on 24/06/2015.
 */
public class HomeActivity extends Activity {
    StickyListHeadersListView stickyList;
    SwipeRefreshLayout swipeRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        stickyList = (StickyListHeadersListView) findViewById(R.id.content_home_sticky_header_lv);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.content_home_refresh_layout);

        stickyList.addHeaderView(getLayoutInflater().inflate(R.layout.stickyheader_lv_header, null));
        stickyList.addFooterView(getLayoutInflater().inflate(R.layout.stickyheader_lv_footer, null));


    }
}
