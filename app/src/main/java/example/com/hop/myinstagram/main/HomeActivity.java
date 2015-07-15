package example.com.hop.myinstagram.main;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.com.hop.myinstagram.R;
import example.com.hop.myinstagram.home.HomeContentItem;
import example.com.hop.myinstagram.home.HomeHeader;
import example.com.hop.myinstagram.home.StickyHeaderAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Hop on 24/06/2015.
 */
public class HomeActivity extends Activity {
    StickyListHeadersListView stickyList;
    TextView txtViewLogo;
    ImageView option;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        txtViewLogo = (TextView) findViewById(R.id.actionbar_title);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/billabong.ttf");
        txtViewLogo.setTypeface(face);
        option = (ImageView) findViewById(R.id.btnDirect);
        option.setImageResource(R.drawable.icon_direct);
        stickyList = (StickyListHeadersListView) findViewById(R.id.list);

        List<HomeHeader> headers = new ArrayList<>();
        List<HomeContentItem> items = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            headers.add(new HomeHeader());
            items.add(new HomeContentItem());
        }

        StickyHeaderAdapter adapter = new StickyHeaderAdapter(this, headers, items);
        stickyList.setDrawingListUnderStickyHeader(true);
        stickyList.setAreHeadersSticky(true);
        stickyList.setAdapter(adapter);

    }
}
