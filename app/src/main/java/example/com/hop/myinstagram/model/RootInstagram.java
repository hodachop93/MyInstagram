package example.com.hop.myinstagram.model;

import java.util.List;

/**
 * Created by Hop on 15/07/2015.
 */
public class RootInstagram {
    private Meta meta;
    private List<DataRoot> data;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<DataRoot> getData() {
        return data;
    }

    public void setData(List<DataRoot> data) {
        this.data = data;
    }
}