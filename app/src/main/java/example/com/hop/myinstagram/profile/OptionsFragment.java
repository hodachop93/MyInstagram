package example.com.hop.myinstagram.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import example.com.hop.myinstagram.R;
import example.com.hop.myinstagram.login.InstagramSession;
import example.com.hop.myinstagram.main.LoginActivity;

/**
 * Created by Hop on 14/07/2015.
 */
public class OptionsFragment extends Fragment {
    LinearLayout toolbar;
    ImageView left_navigation;
    TextView title;
    RelativeLayout btnLogout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.option_fragment, container, false);
        toolbar = (LinearLayout) rootView.findViewById(R.id.toolbar);
        left_navigation = (ImageView) rootView.findViewById(R.id.left_navigation);
        title = (TextView) rootView.findViewById(R.id.title);
        btnLogout = (RelativeLayout) rootView.findViewById(R.id.btnLogOutOption);

        toolbar.setBackgroundColor(getResources().getColor(R.color.tab_indicator));
        left_navigation.setBackgroundResource(R.drawable.white_arrow);
        title.setText("Options");
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnectInstagram();
            }
        });
    }

    private void disconnectInstagram() {
        final InstagramSession mSess = new InstagramSession(getActivity());
        if (mSess.getAccessToken() != null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Disconnect from Instagram?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    mSess.resetAccessToken();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            final AlertDialog alert = builder.create();
            alert.show();
        }

    }
}
