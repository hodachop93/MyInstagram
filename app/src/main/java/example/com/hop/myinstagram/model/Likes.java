package example.com.hop.myinstagram.model;

import java.util.List;

/**
 * Created by Hop on 15/07/2015.
 */
public class Likes {
    private int count;
    private List<From> data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<From> getData() {
        return data;
    }

    public void setData(List<From> data) {
        this.data = data;
    }
}
