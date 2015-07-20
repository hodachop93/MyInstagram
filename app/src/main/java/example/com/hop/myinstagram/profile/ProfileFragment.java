package example.com.hop.myinstagram.profile;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import example.com.hop.myinstagram.R;
import example.com.hop.myinstagram.model.DataRoot;
import example.com.hop.myinstagram.model.RootInstagram;

/**
 * Created by Hop on 14/07/2015.
 */
public class ProfileFragment extends Fragment {
    public final String TAG = this.getClass().getSimpleName();
    TextView txtTitle;
    ImageView option;
    private RootInstagram mRootIG;
    private List<DataRoot> mData;
    private ProgressDialog mProDialog;
    private SwipeRefreshLayout mSwipeRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
        createView(rootView);
        setEventListener();
        return rootView;
    }
    private void createView(View rootView) {
        txtTitle = (TextView) rootView.findViewById(R.id.actionbar_title);
        option = (ImageView) rootView.findViewById(R.id.btnDirect);
        txtTitle.setText("Name");
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/billabong.ttf");
        txtTitle.setTypeface(face);
        option.setImageResource(R.drawable.icon_option);
    }

    private void setEventListener() {
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                OptionsFragment fragment = new OptionsFragment();
                transaction.add(getId(), fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
