package example.com.hop.myinstagram.main;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabWidget;

import example.com.hop.myinstagram.R;


public class MainActivity extends TabActivity implements TabHost.OnTabChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    Drawable[] drawables;
    int currentTab = 0;
    TabHost tabHost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDrawableIconID();
        createTabHost();


    }

    private void createTabHost() {
        tabHost = getTabHost();
        tabHost.setup();
        tabHost.setOnTabChangedListener(this);
        TabWidget tabWidget = tabHost.getTabWidget();
        tabWidget.setDividerDrawable(null);

        //Tab for Home Activity
        TabHost.TabSpec homeSpec = tabHost.newTabSpec("home");
        homeSpec.setIndicator(null, drawables[0]);
        Intent homeIntent = new Intent(this, HomeActivity.class);
        homeSpec.setContent(homeIntent);

        //Tab for Search Activity
        TabHost.TabSpec searchSpec = tabHost.newTabSpec("search");
        searchSpec.setIndicator(null, drawables[1]);
        Intent searchIntent = new Intent(this, SearchActivity.class);
        searchSpec.setContent(searchIntent);

        //Tab for Camera Activity
        TabHost.TabSpec cameraSpec = tabHost.newTabSpec("camera");
        cameraSpec.setIndicator(null, drawables[2]);
        cameraSpec.setContent(R.id.empty_tab_content);

        //Tab for News Activity
        TabHost.TabSpec newsSpec = tabHost.newTabSpec("news");
        newsSpec.setIndicator(null, drawables[3]);
        Intent newsIntent = new Intent(this, NewsActivity.class);
        newsSpec.setContent(newsIntent);

        //Tab for Profile Activity
        TabHost.TabSpec profileSpec = tabHost.newTabSpec("profile");
        profileSpec.setIndicator(null, drawables[4]);
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        profileSpec.setContent(profileIntent);

        tabHost.addTab(homeSpec);
        tabHost.addTab(searchSpec);
        tabHost.addTab(cameraSpec);
        tabHost.addTab(newsSpec);
        tabHost.addTab(profileSpec);

        setCurrentTab(currentTab);


    }

    private void setCurrentTab(int currentTab) {
        TabWidget tabWidget = tabHost.getTabWidget();
        tabWidget.setCurrentTab(currentTab);

        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {

            if (i == 2) {
                //Tab camera
                tabWidget.getChildAt(i).setBackgroundResource(R.drawable.tab_selector_camera_bg);
            } else if (i == currentTab) {
                drawables[i].setAlpha(255);
                tabWidget.getChildAt(i).setBackgroundResource(R.drawable.tab_selector_bg);

            } else {
                drawables[i].setAlpha(128);
                tabWidget.getChildAt(i).setBackgroundResource(R.drawable.tab_selector_bg);
                
            }

        }
    }

    private void getDrawableIconID() {
        TypedArray typedArray = getResources().obtainTypedArray(R.array.tabwidget_icon);
        int size = typedArray.length();
        drawables = new Drawable[size];
        for (int i = 0; i < size; i++) {
            drawables[i] = getResources().getDrawable(typedArray.getResourceId(i, -1));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabChanged(String tabId) {
        Log.d(TAG, tabId);

        //setCurrentTab(currentTab);
        if (tabId.equals("camera")) {
            tabHost.setCurrentTab(currentTab);
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
            return;
        } else {
            currentTab = tabHost.getCurrentTab();
            if (currentTab != 2) {
                drawables[currentTab].setAlpha(255);
            }
            for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                if (i != currentTab && i != 2) {
                    drawables[i].setAlpha(128);
                }
            }
        }


    }
}
