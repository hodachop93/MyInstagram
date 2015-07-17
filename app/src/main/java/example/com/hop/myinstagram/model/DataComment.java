package example.com.hop.myinstagram.model;

/**
 * Created by Hop on 15/07/2015.
 */
public class DataComment {
    private String text;
    private From from;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }
}
