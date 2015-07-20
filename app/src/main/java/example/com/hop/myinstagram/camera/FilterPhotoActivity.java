package example.com.hop.myinstagram.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import example.com.hop.myinstagram.R;

/**
 * Created by Hop on 02/07/2015.
 */
public class FilterPhotoActivity extends Activity implements
        RecyclerViewAdapter.FilterWayViewHolder.RecyclerViewClickListener,
        View.OnClickListener {
    ImageView left_navigation, right_navigation, photo_viewer;
    TextView title;
    RecyclerView recyclerView;
    List<FilterWay> filterWays;
    RecyclerViewAdapter adapter;
    Bitmap origionalImage;

    private byte[] mData;
    private int mRotation;
    private int screen_width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_photo_activity);
        //hideStatusBar();
        createGUI();
        init();
    }

    private void init() {
        left_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        //Get du lieu trong bundle duoc gui qua tu camera fragment
        Bundle bundle = getIntent().getBundleExtra("ImagePackage");
        mData = bundle.getByteArray("data");
        mRotation = bundle.getInt("rotation");

        filterWays = new ArrayList<>();
        adapter = new RecyclerViewAdapter(filterWays, this);
        recyclerView.setAdapter(adapter);

        //Tao thread load image vao image view photo_viewer va load cac cach xu ly anh len man hinh
        LoadImageTask task = new LoadImageTask(this, photo_viewer, screen_width, filterWays, adapter);
        task.execute(mData, mRotation);


    }

    private void goBack() {
        super.onBackPressed();
    }

    private void createGUI() {
        left_navigation = (ImageView) findViewById(R.id.left_navigation);
        right_navigation = (ImageView) findViewById(R.id.right_navigation);
        title = (TextView) findViewById(R.id.title);
        photo_viewer = (ImageView) findViewById(R.id.photo_viewer);
        recyclerView = (RecyclerView) findViewById(R.id.recyler_view);

        title.setText("Edit");
        left_navigation.setImageResource(R.drawable.white_arrow);
        right_navigation.setVisibility(View.VISIBLE);
        right_navigation.setOnClickListener(this);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(llm);

        //Lay screen_width man hinh
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screen_width = displayMetrics.widthPixels;

        LinearLayout.LayoutParams photo_viewer_params = (LinearLayout.LayoutParams) photo_viewer.getLayoutParams();
        photo_viewer_params.height = screen_width;
        this.screen_width = screen_width;

    }

    @Override
    public void onItemClicked(int position) {
        toogleSelection(position);
    }

    private void toogleSelection(int position) {
        adapter.clearSelection();
        adapter.toogleSelection(position);
        if (origionalImage == null) {
            //Luu lai anh goc
            origionalImage = ((BitmapDrawable) photo_viewer.getDrawable()).getBitmap();
        }
        ApplyEffectImageTask task = new ApplyEffectImageTask(this, photo_viewer, origionalImage);
        task.execute(position);
    }



    @Override
    public void onClick(View v) {
        BitmapDrawable bmpDrawable = (BitmapDrawable) (photo_viewer.getDrawable());
        Bitmap bmp = bmpDrawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();
        SaveImageTask task = new SaveImageTask(this);
        task.execute(data, 0);
        Toast.makeText(this, "New image saved", Toast.LENGTH_LONG).show();
        this.finish();
    }

    /*private void hideStatusBar() {
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
    }*/
}
