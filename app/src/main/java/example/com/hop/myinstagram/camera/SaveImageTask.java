package example.com.hop.myinstagram.camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Hop on 26/06/2015.
 */
public class SaveImageTask extends AsyncTask<Object, Void, Void> {
    private static final String TAG = SaveImageTask.class.getSimpleName();


    private Context mContext;
    private CameraPreview mCameraPreview;
    private int rotationOfImage;

    public SaveImageTask(Context mContext, CameraPreview mCameraPreview) {
        this.mContext = mContext;
        this.mCameraPreview = mCameraPreview;
    }

    @Override
    protected Void doInBackground(Object... objects) {
        byte[] data = (byte[]) objects[0];
        rotationOfImage = (int) objects[1];

        // Write to SD Card
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/MyInstagram");
            dir.mkdirs();

            String fileName = String.format("%d.jpg", System.currentTimeMillis());

            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            Bitmap rotatedBmp = rotateImageIfRequired(bmp);

            File outFile = new File(dir, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(outFile);
            rotatedBmp.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

            Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to " + outFile.getAbsolutePath());

            refreshGallery(outFile);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }


    /**
     * Xác định hướng của device bằng OrientationEventListener
     * @param img
     * @return
     */
    private Bitmap rotateImageIfRequired(Bitmap img) {
        if (rotationOfImage != 0 || rotationOfImage != 360) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotationOfImage);
            Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
            return rotatedImg;
        } else {
            return img;
        }
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        mContext.sendBroadcast(mediaScanIntent);
    }

}
