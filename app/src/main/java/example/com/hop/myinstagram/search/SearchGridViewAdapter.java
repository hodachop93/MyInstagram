package example.com.hop.myinstagram.search;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import example.com.hop.myinstagram.model.DataRoot;
import example.com.hop.myinstagram.model.Media;
import example.com.hop.myinstagram.utils.ImageLoader;

/**
 * Created by Hop on 18/07/2015.
 */
public class SearchGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<DataRoot> mData;
    private ImageLoader mImgLoader;

    public SearchGridViewAdapter(Context mContext, List<DataRoot> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.mImgLoader = new ImageLoader(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imgView = null;
        if (convertView==null){
            imgView = new ImageView(mContext);
            imgView.setAdjustViewBounds(true);
            imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imgView.setPadding(4,4,0,0);
        } else {
            imgView = (ImageView) convertView;
        }
        setValueImageView(position, imgView);
        return imgView;
    }

    private void setValueImageView(int position, ImageView imgView) {
        DataRoot item = mData.get(position);
        Media image = item.getImages();
        String urlImg = image.getLow_resolution().getUrl();
        mImgLoader.DisplayImage(urlImg, imgView);
    }


}
