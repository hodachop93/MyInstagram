package example.com.hop.myinstagram.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import example.com.hop.myinstagram.R;

/**
 * Created by Hop on 19/07/2015.
 */
public class DirectFragment extends Fragment {
    private ImageView mLeftNav;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.direct_fragment, container, false);
        mLeftNav = (ImageView) rootView.findViewById(R.id.left_navigation);
        mLeftNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return rootView;
    }
}
