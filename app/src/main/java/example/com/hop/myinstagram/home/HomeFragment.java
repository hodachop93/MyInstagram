package example.com.hop.myinstagram.home;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    public final String TAG = this.getClass().getSimpleName();
    public static final String URL_NEWS_FEED = "https://api.instagram.com/v1/users/self/feed?access_token=";
    private StickyListHeadersListView mStickyLV;
    private TextView mTVLogo;
    private ImageView mImgOption;
    private SwipeRefreshLayout mSwipeRef;

    private List<DataRoot> mData;
    private StickyHeaderAdapter mAdapter;
    private int mPrevLast = 0;
    private RootInstagram mRootIG;
    private ProgressDialog mProDialog;
    private boolean mIsSwRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        createView(rootView);
        mProDialog = new ProgressDialog(getActivity());
        mProDialog.setCancelable(false);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = new ArrayList<DataRoot>();
        mAdapter = new StickyHeaderAdapter(getActivity(), mData);
    }

    private void createView(View rootView) {
        mTVLogo = (TextView) rootView.findViewById(R.id.actionbar_title);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/billabong.ttf");
        mTVLogo.setTypeface(face);
        mImgOption = (ImageView) rootView.findViewById(R.id.btnDirect);
        mImgOption.setImageResource(R.drawable.icon_direct);
        mStickyLV = (StickyListHeadersListView) rootView.findViewById(R.id.list);
        mSwipeRef = (SwipeRefreshLayout) rootView.findViewById(R.id.srfContainer);
        mSwipeRef.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mImgOption.setOnClickListener(this);

        mSwipeRef.setOnRefreshListener(this);

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
                if (mData.size() == 0) {
                    mSwipeRef.setRefreshing(true);
                    mIsSwRef = true;
                    fetchData();
                }

            }
        });

        mStickyLV.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount) {
                    if (mPrevLast != lastItem) {
                        //Loading more data if possible
                        if (mRootIG.getPagination() != null) {
                            mPrevLast = lastItem;
                            loadingMoreData();
                        }
                    }
                }

            }
        });
    }

    private void loadingMoreData() {
        mProDialog.show();
        makeJsonObjectRequest(mRootIG.getPagination().getNext_url());
        mIsSwRef = false;
    }

    private void fetchData() {
        InstagramSession igSess = new InstagramSession(getActivity());
        if (igSess.getAccessToken() != null) {
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
                Toast.makeText(getActivity(), "Can't connect right now", Toast.LENGTH_SHORT).show();
                mSwipeRef.setRefreshing(false);
            }
        }) {

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void notifyDataSetChanged(JSONObject response) {
        String jsonStr = response.toString();
        mRootIG = new Gson().fromJson(jsonStr, RootInstagram.class);
        int code = mRootIG.getMeta().getCode();
        if (code == 200) {


            if (mIsSwRef) {
                mData.clear();
                mData.addAll(mRootIG.getData());
                mSwipeRef.setRefreshing(false);
            } else {
                mData.addAll(mRootIG.getData());
                mProDialog.dismiss();
            }
            mAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), "Bad Request", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRefresh() {
        mIsSwRef = true;
        fetchData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragManager = getFragmentManager();
        FragmentTransaction transaction = fragManager.beginTransaction();
        DirectFragment f = new DirectFragment();
        transaction.replace(getId(), f);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
