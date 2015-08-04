package example.com.hop.myinstagram.camera;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    final int EFFECT_INVERT = 0;
    final int EFFECT_BRIGHTNESS = 1;
    final int EFFECT_SNOW = 2;
    final int EFFECT_REFLECT = 3;
    final int EFFECT_BOOTS = 4;
    final int EFFECT_GRAYSCALE = 5;
    final int EFFECT_CONTRAST = 6;
    final int EFFECT_TINT = 7;
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
            case EFFECT_INVERT:
                bmp = filter.applyInvertEffect(src);
                break;
            case EFFECT_BRIGHTNESS:
                bmp=filter.applyBrightnessEffect(src, 80);
                break;
            case EFFECT_SNOW:
                bmp=filter.applySnowEffect(src);
                break;
            case EFFECT_REFLECT:
                bmp=filter.applyReflection(src);
                break;
            case EFFECT_BOOTS:
                bmp=filter.applyBoostEffect(src, 1, 20);
                break;
            case EFFECT_GRAYSCALE:
                bmp=filter.applyGrayscaleEffect(src);
                break;
            case EFFECT_CONTRAST:
                bmp=filter.applyContrastEffect(src, 70);
                break;
            case EFFECT_TINT:
                bmp=filter.applyTintEffect(src, 45);
                break;
            case EFFECT_WATERMARK:
                bmp=filter.applyWaterMarkEffect(src, "", 200, 200, 128, 80, 24, false);
                break;
        }
        return bmp;
    }

    private Bitmap getThumbnail(Bitmap bmp) {
        int scale_factor = 5;
        Bitmap scaled_bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / scale_factor, bmp.getHeight() / scale_factor, false);
        return scaled_bmp;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        adapter.notifyDataSetChanged();
    }
}
