package example.com.hop.myinstagram.home;

import android.graphics.Bitmap;

/**
 * Created by Hop on 13/07/2015.
 */
public class HomeContentItem {
    private Bitmap image;
    private int numberLikes;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getNumberLikes() {
        return numberLikes;
    }

    public void setNumberLikes(int numberLikes) {
        this.numberLikes = numberLikes;
    }
}
