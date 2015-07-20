package example.com.hop.myinstagram.camera;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import example.com.hop.myinstagram.utils.ImageFilters;

/**
 * Created by Hop on 07/07/2015.
 */
public class ApplyEffectImageTask extends AsyncTask<Integer, Bitmap, Void> {
    final int EFFECT_INVERT = 0;
    final int EFFECT_BRIGHTNESS = 1;
    final int EFFECT_SNOW = 2;
    final int EFFECT_REFLECT = 3;
    final int EFFECT_BOOTS = 4;
    final int EFFECT_GRAYSCALE = 5;
    final int EFFECT_CONTRAST = 6;
    final int EFFECT_TINT = 7;
    final int EFFECT_WATERMARK = 8;

    private Activity mActivity;
    private ImageView mImgView;
    private Bitmap mOrigionalImage;
    private ProgressDialog dialog;

    public ApplyEffectImageTask(Activity mActivity, ImageView mImgView, Bitmap mOrigionalImage) {
        this.mActivity = mActivity;
        this.mImgView = mImgView;
        this.mOrigionalImage = mOrigionalImage;
    }

    @Override
    protected void onPreExecute() {
        showDialog();
    }

    @Override
    protected Void doInBackground(Integer... params) {
        //Lay ma so cua hieu ung
        int effect_type = params[0];
        Bitmap bmp = processImage(effect_type);
        publishProgress(bmp);
        return null;
    }

    private Bitmap processImage(int effectType) {
        Bitmap src = mOrigionalImage;
        Bitmap bmp = null;
        ImageFilters filter = new ImageFilters();
        switch (effectType) {
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

    @Override
    protected void onProgressUpdate(Bitmap... values) {
        Bitmap bmp = values[0];
        if (bmp != null)
            mImgView.setImageBitmap(bmp);
    }

    private void showDialog() {
        dialog = new ProgressDialog(mActivity);
        dialog.setTitle("Loading...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        hideDialog();
    }

    private void hideDialog(){
        dialog.dismiss();
    }
}
