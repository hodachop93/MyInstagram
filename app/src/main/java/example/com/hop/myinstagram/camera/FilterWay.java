package example.com.hop.myinstagram.camera;

import android.graphics.Bitmap;

/**
 * Created by Hop on 02/07/2015.
 */
public class FilterWay {
    private Bitmap bmp;
    private String title;

    public FilterWay(Bitmap bmp, String title) {
        this.bmp = bmp;
        this.title = title;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
