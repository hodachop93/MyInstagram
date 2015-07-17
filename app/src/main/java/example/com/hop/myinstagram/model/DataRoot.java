package example.com.hop.myinstagram.model;


import android.nfc.Tag;

import java.util.List;

/**
 * Created by Hop on 15/07/2015.
 * Store data from Instagram service
 */
public class DataRoot {
    private List<Tag> tags;
    private Commnents commnents;
    private Likes likes;
    private Images images;
    private Caption caption;
    private User user;

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Commnents getCommnents() {
        return commnents;
    }

    public void setCommnents(Commnents commnents) {
        this.commnents = commnents;
    }

    public Likes getLikes() {
        return likes;
    }

    public void setLikes(Likes likes) {
        this.likes = likes;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public Caption getCaption() {
        return caption;
    }

    public void setCaption(Caption caption) {
        this.caption = caption;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
