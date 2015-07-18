package example.com.hop.myinstagram.model;


import java.util.List;

/**
 * Created by Hop on 15/07/2015.
 * Store data from Instagram service
 */
public class DataRoot {
    private List<String> tags;
    private Comments comments;
    private Likes likes;
    private Media images;
    private String type;
    private Media videos;
    private Caption caption;
    private User user;
    private String created_time;

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Comments getComments() {
        return comments;
    }

    public void setComments(Comments commnents) {
        this.comments = commnents;
    }

    public Likes getLikes() {
        return likes;
    }

    public void setLikes(Likes likes) {
        this.likes = likes;
    }

    public Media getImages() {
        return images;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Media getVideos() {
        return videos;
    }

    public void setVideos(Media videos) {
        this.videos = videos;
    }

    public void setImages(Media images) {
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
