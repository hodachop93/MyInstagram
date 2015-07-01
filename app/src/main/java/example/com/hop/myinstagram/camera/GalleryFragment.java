package example.com.hop.myinstagram.camera;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import example.com.hop.myinstagram.R;

/**
 * Created by Hop on 25/06/2015.
 */
public class GalleryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gallery_camera_fragment, null, false);
        return rootView;
    }
}
