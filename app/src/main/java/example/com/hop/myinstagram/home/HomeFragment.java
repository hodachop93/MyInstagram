package example.com.hop.myinstagram.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import example.com.hop.myinstagram.R;
import example.com.hop.myinstagram.login.InstagramSession;
import example.com.hop.myinstagram.model.DataRoot;
import example.com.hop.myinstagram.model.RootInstagram;
import example.com.hop.myinstagram.utils.AppController;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Class Home Fragment
 * Created by hodachop93 on 17/07/2015.
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public final String TAG = this.getClass().getSimpleName();
    public static final String URL_NEWS_FEED = "https://api.instagram.com/v1/users/self/feed?access_token=";
    private StickyListHeadersListView mStickyLV;
    private TextView mTVLogo;
    private ImageView mImgOption;
    private SwipeRefreshLayout mSwipeRef;

    private List<DataRoot> mData;
    private StickyHeaderAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        createView(rootView);
        return rootView;
    }


    private void createView(View rootView) {
        mTVLogo = (TextView) rootView.findViewById(R.id.actionbar_title);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/billabong.ttf");
        mTVLogo.setTypeface(face);
        mImgOption = (ImageView) rootView.findViewById(R.id.btnDirect);
        mImgOption.setImageResource(R.drawable.icon_direct);
        mStickyLV = (StickyListHeadersListView) rootView.findViewById(R.id.list);
        mSwipeRef = (SwipeRefreshLayout) rootView.findViewById(R.id.srfContainer);
        mSwipeRef.setColorSchemeColors(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light);

        mSwipeRef.setOnRefreshListener(this);
        mData = new ArrayList<DataRoot>();
        mAdapter = new StickyHeaderAdapter(getActivity(), mData);
        mStickyLV.setAdapter(mAdapter);

        /*StickyHeaderAdapter adapter = new StickyHeaderAdapter(getActivity(), mHeaders, mItems);
        mStickyLV.setDrawingListUnderStickyHeader(true);
        mStickyLV.setAreHeadersSticky(true);
        mStickyLV.setAdapter(adapter);*/

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRef.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRef.setRefreshing(true);
                fetchData();
            }
        });
    }

    private void fetchData() {
        InstagramSession igSess = new InstagramSession(getActivity());
        if (igSess.getAccessToken() != null) {
            mSwipeRef.setRefreshing(true);
            String url = URL_NEWS_FEED + igSess.getAccessToken();
            makeJsonObjectRequest(url);

        } else {
            Toast.makeText(getActivity(), "Cannot get my access token", Toast.LENGTH_LONG);
        }
    }

    private void makeJsonObjectRequest(String url) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                notifyDataSetChanged(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, (error.getMessage() != null) ? error.getMessage() : "Unknown error");
                Toast.makeText(getActivity(), "Loading data from server unsuccessful", Toast.LENGTH_LONG).show();
            }
        }) {

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void notifyDataSetChanged(JSONObject response) {
        Log.d(TAG, response.toString());
        String jsonStr = response.toString();
        RootInstagram rootIG = new Gson().fromJson(jsonStr, RootInstagram.class);
        int code = rootIG.getMeta().getCode();
        if (code == 200){
            if (mData.size() > 0){
                mData.clear();
            }
            mData.addAll(rootIG.getData());
            mAdapter.notifyDataSetChanged();
            mSwipeRef.setRefreshing(false);
        }else {
            Toast.makeText(getActivity(), "Bad Request", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRefresh() {
        fetchData();
    }
}
