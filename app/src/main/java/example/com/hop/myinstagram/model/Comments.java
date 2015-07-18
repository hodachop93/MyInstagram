package example.com.hop.myinstagram.model;

import java.util.List;

/**
 * Created by Hop on 15/07/2015.
 */
public class Comments {
    private int count;
    private List<DataComment> data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<DataComment> getData() {
        return data;
    }

    public void setData(List<DataComment> data) {
        this.data = data;
    }
}
