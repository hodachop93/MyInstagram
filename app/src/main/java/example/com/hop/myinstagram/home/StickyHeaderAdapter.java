package example.com.hop.myinstagram.home;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import example.com.hop.myinstagram.R;
import example.com.hop.myinstagram.model.Comments;
import example.com.hop.myinstagram.model.DataRoot;
import example.com.hop.myinstagram.model.Likes;
import example.com.hop.myinstagram.model.Media;
import example.com.hop.myinstagram.model.User;
import example.com.hop.myinstagram.utils.ImageLoader;
import example.com.hop.myinstagram.utils.LinkedTextView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;


/**
 * Created by Hop on 01/07/2015.
 */
public class StickyHeaderAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {
    public static final int MAX_COMMENTS = 3;
    private Context mContext;
    private List<DataRoot> mData;
    private ImageLoader mImageLoader;

    public StickyHeaderAdapter(Context context, List<DataRoot> data) {
        this.mContext = context;
        this.mData = data;
        this.mImageLoader = new ImageLoader(mContext);
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
            headerVH.time = (TextView) convertView.findViewById(R.id.home_header_time);

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
        return mData.size();
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
            itemVH.comments = (TextView) convertView.findViewById(R.id.home_comment);
            itemVH.btnLike = (ImageView) convertView.findViewById(R.id.icon_like);
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
        ImageView image, btnLike;
        TextView likes;
        TextView comments;
    }

    private static class HeaderViewHolder {
        TextView name;
        ImageView avatar;
        TextView time;
    }

    private void setValueHeader(HeaderViewHolder headerVH, int position) {
        User user = mData.get(position).getUser();
        headerVH.name.setText(user.getUsername());
        headerVH.time.setText(convertTime(mData.get(position).getCreated_time()));
        mImageLoader.DisplayImage(user.getProfile_picture(), headerVH.avatar);
    }

    private String convertTime(String created_time) {
        //Get current time
        Calendar calendar = Calendar.getInstance();
        Date curDate = calendar.getTime();
        long lCurTime = curDate.getTime();
        long lServerTime = Long.parseLong(created_time) * 1000;
        String time = (String) DateUtils.getRelativeTimeSpanString(lServerTime, lCurTime, DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL);
        return time;
    }

    private void setValueItem(ItemViewHolder itemVH, int position) {
        DataRoot item = mData.get(position);
        User user = item.getUser();
        Likes likes = item.getLikes();
        Media image = item.getImages();
        Comments comments = item.getComments();

        mImageLoader.DisplayImage(image.getStandard_resolution().getUrl(), itemVH.image);
        itemVH.likes.setText(likes.getCount() + "");
        // set value textview comments:
        try {
            String contentOfTvComments = "<font color='gray'>Add a comment</font>";
            String captionOfUser = "";
            if (item.getCaption() != null) {
                String caption = item.getCaption().getText();
                captionOfUser = "(user)" + user.getUsername() + "(/user) " + "<font color='black'>" + caption + "</font>" + "<br>";

            }
            if (comments.getCount() > 0) {
                String countComments = ((comments.getCount() > MAX_COMMENTS) ?
                        "<font color='gray'>View all " + comments.getCount() + "comments</font> <br>" : "");
                String content_comment = "";
                for (int i = 0; i < ((comments.getCount() > MAX_COMMENTS) ? MAX_COMMENTS : comments.getCount()); i++) {
                    String account = comments.getData().get(i).getFrom().getUsername();
                    String comment = comments.getData().get(i).getText();

                    content_comment += "(user)" + account + "(/user) " + "<font color='black'>" + comment + "</font>" + "<br>";
                }
                contentOfTvComments = countComments + content_comment + "<font color='gray'>Add a comment</font>";
            }
            contentOfTvComments = captionOfUser + contentOfTvComments;

            // set text & event for tvComments
            LinkedTextView.autoLink(itemVH.comments, contentOfTvComments, null);
        } catch (NullPointerException ex){
            ex.printStackTrace();
        }


    }
}
