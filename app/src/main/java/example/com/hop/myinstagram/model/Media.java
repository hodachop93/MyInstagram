package example.com.hop.myinstagram.model;

import android.graphics.Bitmap;

/**
 * Created by hodachop93 on 17/07/2015.
 */
public class Media {
    private Resolution standard_resolution;
    private Resolution low_resolution;
    private Resolution thumbnail; //Only image
    private Resolution low_bandwidth; //Only video

    private Bitmap image;

    /**
     * Only used in Imagr, not used in Video
     * @return a bitmap
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * Only used in Imagr, not used in Video
     * @param image
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Resolution getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Resolution thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Resolution getLow_bandwidth() {
        return low_bandwidth;
    }

    public void setLow_bandwidth(Resolution low_bandwidth) {
        this.low_bandwidth = low_bandwidth;
    }

    public Resolution getStandard_resolution() {
        return standard_resolution;
    }

    public void setStandard_resolution(Resolution standard_resolution) {
        this.standard_resolution = standard_resolution;
    }

    public Resolution getLow_resolution() {
        return low_resolution;
    }

    public void setLow_resolution(Resolution low_resolution) {
        this.low_resolution = low_resolution;
    }
}
