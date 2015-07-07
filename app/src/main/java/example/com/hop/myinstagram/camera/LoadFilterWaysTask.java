package example.com.hop.myinstagram.camera;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;

import java.util.List;

import example.com.hop.myinstagram.R;
import example.com.hop.myinstagram.utils.ImageFilters;

/**
 * Created by Hop on 02/07/2015.
 */
public class LoadFilterWaysTask extends AsyncTask<Bitmap, Void, Void> {
    Activity activity;
    List<FilterWay> filterWays;
    RecyclerViewAdapter adapter;
    ProgressDialog dialog;

    final int EFFECT_COLOR_BLACK = 0;
    final int EFFECT_BRIGHTNESS = 1;
    final int EFFECT_COLOR_RED = 2;
    final int EFFECT_COLOR_GREEN = 3;
    final int EFFECT_COLOR_BLUE = 4;
    final int EFFECT_GRAYSCALE = 5;
    final int EFFECT_CONTRAST = 6;
    final int EFFECT_ROUND_CORNER = 7;
    final int EFFECT_WATERMARK = 8;


    public LoadFilterWaysTask(Activity activity, List<FilterWay> filterWays, RecyclerViewAdapter adapter) {
        this.activity = activity;
        this.filterWays = filterWays;
        this.adapter = adapter;
    }


    @Override
    protected Void doInBackground(Bitmap... params) {
        Bitmap thumbnail = getThumbnail(params[0]);
        processThumbnail(thumbnail);
        return null;
    }

    private void processThumbnail(Bitmap thumbnail) {
        String[] titles = activity.getResources().getStringArray(R.array.filter_array_titles);
        int size = titles.length;

        for (int i = 0; i < size; i++) {
            Bitmap bmp = getBitmap(i, thumbnail);
            FilterWay way = new FilterWay(bmp, titles[i]);
            filterWays.add(way);
        }
    }

    /**
     * Ap dung hieu ung len buc anh
     * @param effectType loai hieu ung
     * @param src anh nguon
     * @return
     */
    private Bitmap getBitmap(int effectType, Bitmap src) {
        Bitmap bmp = null;
        ImageFilters filter = new ImageFilters();
        switch (effectType){
            case EFFECT_COLOR_BLACK:
                bmp = filter.applyBlackFilter(src);
                break;
            case EFFECT_BRIGHTNESS:
                bmp=filter.applyBrightnessEffect(src, 80);
                break;
            case EFFECT_COLOR_RED:
                bmp=filter.applyColorFilterEffect(src, 255, 0, 0);
                break;
            case EFFECT_COLOR_GREEN:
                bmp=filter.applyColorFilterEffect(src, 0, 255, 0);
                break;
            case EFFECT_COLOR_BLUE:
                bmp=filter.applyColorFilterEffect(src, 0, 0, 255);
                break;
            case EFFECT_GRAYSCALE:
                bmp=filter.applyGrayscaleEffect(src);
                break;
            case EFFECT_CONTRAST:
                bmp=filter.applyContrastEffect(src, 70);
                break;
            case EFFECT_ROUND_CORNER:
                bmp=filter.applyRoundCornerEffect(src, 45);
                break;
            case EFFECT_WATERMARK:
                bmp=filter.applyWaterMarkEffect(src, "hodachop", 200, 200, Color.GREEN, 80, 24, false);
                break;
        }
        return bmp;
    }

    private Bitmap getThumbnail(Bitmap bmp) {
        int scale_factor = 5;
        Bitmap scaled_bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / scale_factor, bmp.getHeight() / scale_factor, false);
        return scaled_bmp;
    }

    private void getData() {
        TypedArray images_preview = activity.getResources().obtainTypedArray(R.array.filter_array_preview);
        String[] titles = activity.getResources().getStringArray(R.array.filter_array_titles);

        int size = images_preview.length();
        for (int i = 0; i < size; i++) {
            Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), images_preview.getResourceId(i, -1));
            FilterWay way = new FilterWay(bmp, titles[i]);
            filterWays.add(way);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        adapter.notifyDataSetChanged();
    }
}
