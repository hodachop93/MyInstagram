package example.com.hop.myinstagram.camera;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import example.com.hop.myinstagram.R;

/**
 * Created by Hop on 25/06/2015.
 */
public class VideoFragment extends Fragment {
    private ImageView mLeftNav, mRightNav;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.video_camera_fragment, null, false);
        TextView txtView = (TextView) rootView.findViewById(R.id.title);
        txtView.setText("VIDEO");
        mLeftNav = (ImageView) rootView.findViewById(R.id.left_navigation);
        mRightNav = (ImageView) rootView.findViewById(R.id.right_navigation);
        mLeftNav.setImageResource(R.drawable.nav_cancel);
        mRightNav.setImageResource(R.drawable.blue_arrow);
        mLeftNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return rootView;
    }
}
