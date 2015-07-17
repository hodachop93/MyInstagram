package example.com.hop.myinstagram.model;

/**
 * Created by Hop on 15/07/2015.
 */
public class Images {
    private ImageResolution thumbnail;
    private ImageResolution standard_resolution;

    public ImageResolution getImageResolution() {
        return thumbnail;
    }

    public void setImageResolution(ImageResolution thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ImageResolution getstandard_resolution() {
        return standard_resolution;
    }

    public void setstandard_resolution(ImageResolution standard_resolution) {
        this.standard_resolution = standard_resolution;
    }
}
