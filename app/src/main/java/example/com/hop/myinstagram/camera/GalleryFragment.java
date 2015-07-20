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
public class GalleryFragment extends Fragment {
    private ImageView mLeftNav;
    private TextView mTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gallery_camera_fragment, null, false);
        mTextView = (TextView) rootView.findViewById(R.id.title);
        mLeftNav = (ImageView) rootView.findViewById(R.id.left_navigation);

        mTextView.setText("GALLERY");
        mLeftNav.setImageResource(R.drawable.nav_cancel);
        mLeftNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return rootView;
    }
}
