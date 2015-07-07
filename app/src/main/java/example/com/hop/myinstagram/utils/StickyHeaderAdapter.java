package example.com.hop.myinstagram.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Hop on 01/07/2015.
 */
public class StickyHeaderAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {
    private Context mContext;
    private String[] mCountries;

    @Override
    public View getHeaderView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public long getHeaderId(int i) {
        return 0;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }
}
