package example.com.hop.myinstagram.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.List;

import example.com.hop.myinstagram.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Hop on 01/07/2015.
 */
public class StickyHeaderAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {
    private Context mContext;
    private List<HomeHeader> headers;
    private List<HomeContentItem> items;

    public StickyHeaderAdapter(Context mContext, List<HomeHeader> headers, List<HomeContentItem> items) {
        this.mContext = mContext;
        this.headers = headers;
        this.items = items;
    }

    public StickyHeaderAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup viewGroup) {
        HeaderViewHolder headerVH = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.home_stikyheader_header, viewGroup, false);

            headerVH = new HeaderViewHolder();

            //init XML
            headerVH.avatar = (ImageView) convertView.findViewById(R.id.home_header_avatar);
            headerVH.name = (TextView) convertView.findViewById(R.id.home_header_name);

            convertView.setTag(headerVH);
        } else {
            headerVH = (HeaderViewHolder) convertView.getTag();
        }
        setValueHeader(headerVH, position);
        return convertView;
    }


    @Override
    public long getHeaderId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return headers.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewHolder itemVH = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.home_stickyheader_item, parent, false);

            itemVH = new ItemViewHolder();
            //init XML
            itemVH.image = (ImageView) convertView.findViewById(R.id.home_item_image);
            itemVH.likes = (TextView) convertView.findViewById(R.id.home_item_likes);

            convertView.setTag(itemVH);
        } else {
            itemVH = (ItemViewHolder) convertView.getTag();
        }
        setValueItem(itemVH, position);
        return convertView;
    }


    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return sectionIndex;
    }

    @Override
    public int getSectionForPosition(int position) {
        return position + 1;
    }

    private static class ItemViewHolder {
        ImageView image;
        TextView likes;
    }

    private static class HeaderViewHolder {
        TextView name;
        ImageView avatar;
    }

    private void setValueHeader(HeaderViewHolder headerVH, int position) {

    }

    private void setValueItem(ItemViewHolder itemVH, int position) {

    }
}
