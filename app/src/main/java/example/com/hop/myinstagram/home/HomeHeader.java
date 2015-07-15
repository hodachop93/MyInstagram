package example.com.hop.myinstagram.home;

import android.graphics.Bitmap;

/**
 * Created by Hop on 13/07/2015.
 */
public class HomeHeader {
    private Bitmap avatar;
    private String name;
    private String time;

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
