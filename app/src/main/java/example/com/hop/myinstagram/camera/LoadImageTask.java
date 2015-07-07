package example.com.hop.myinstagram.camera;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Hop on 02/07/2015.
 * Cắt hình ảnh chụp được thành hình vuông sau đó load lên Activity Edit để bắt đầu tiến hành chỉnh sửa
 * Load cac cach xu ly len
 */

public class LoadImageTask extends AsyncTask<Object, Bitmap, Bitmap> {
    private Activity activity;
    private ImageView imageView;
    private int screen_width;
    List<FilterWay> filterWays;
    RecyclerViewAdapter adapter;
    private ProgressDialog dialog;


    public LoadImageTask(Activity activity, ImageView imageView, int screen_width, List<FilterWay> filterWays, RecyclerViewAdapter adapter) {
        this.activity = activity;
        this.imageView = imageView;
        this.screen_width = screen_width;
        this.filterWays = filterWays;
        this.adapter = adapter;
    }

    private void showDialog() {
        dialog = new ProgressDialog(activity);
        dialog.setTitle("Loading...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog(){
        dialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
        showDialog();
    }

    @Override
    protected Bitmap doInBackground(Object... params) {
        byte[] data = (byte[]) params[0];
        int rotation = (int) params[1];

        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap rotatedBmp = rotateImage(bmp, rotation);

        //Crop image

        Bitmap cropedImage = Bitmap.createBitmap(rotatedBmp, 0, 0, screen_width, screen_width);

        //Tao thread load cac cach xu ly anh len Recycler View
        LoadFilterWaysTask task1 = new LoadFilterWaysTask(activity, filterWays, adapter);
        task1.execute(cropedImage);
        publishProgress(cropedImage);
        return null;
    }

    @Override
    protected void onProgressUpdate(Bitmap... values) {
        imageView.setImageBitmap(values[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        hideDialog();

    }

    /**
     * Xác đinh hướng của device bằng OrientationEventListener
     *
     * @param img
     * @return
     */
    private Bitmap rotateImage(Bitmap img, int rotation) {
        if (rotation != 0 || rotation != 360) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
            return rotatedImg;
        } else {
            return img;
        }
    }
}
