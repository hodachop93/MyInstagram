package example.com.hop.myinstagram.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.GridView;
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
import example.com.hop.myinstagram.search.SearchGridViewAdapter;
import example.com.hop.myinstagram.utils.AppController;

/**
 * Created by Hop on 24/06/2015.
 */
public class SearchActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{
    public static final String URL_SEARCH = "https://api.instagram.com/v1/media/popular?access_token=";
    private final String TAG = this.getClass().getSimpleName();
    private List<DataRoot> mData;
    private SearchGridViewAdapter mAdapter;
    private GridView mGridView;
    private SwipeRefreshLayout mSwRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        createView();
    }

    private void createView() {
        mGridView = (GridView) findViewById(R.id.search_gridview);
        mSwRef = (SwipeRefreshLayout) findViewById(R.id.search_swRef);
        mSwRef.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwRef.setOnRefreshListener(this);
        mData = new ArrayList<>();
        mAdapter = new SearchGridViewAdapter(this, mData);
        mGridView.setAdapter(mAdapter);
        mSwRef.post(new Runnable() {
            @Override
            public void run() {
                mSwRef.setRefreshing(true);
                fetchData();
            }
        });
    }

    private void fetchData() {
        InstagramSession igSes = new InstagramSession(this);
        String access_token = igSes.getAccessToken();
        if (access_token != null){
            String url = URL_SEARCH + access_token;
            makeJsonObjectRequest(url);
        }else{
            Toast.makeText(this, "Cannot get my access token", Toast.LENGTH_LONG);
            mSwRef.setRefreshing(false);
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
                Toast.makeText(SearchActivity.this, "Can't connect right now", Toast.LENGTH_SHORT).show();
                mSwRef.setRefreshing(false);
            }
        }) {

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void notifyDataSetChanged(JSONObject response) {
        String jsonStr = response.toString();
        RootInstagram rootIG = new Gson().fromJson(jsonStr, RootInstagram.class);
        int code = rootIG.getMeta().getCode();
        if (code == 200){
            if (mData.size()!=0){
                mData.clear();
            }
            mData.addAll(rootIG.getData());
            mAdapter.notifyDataSetChanged();
            mSwRef.setRefreshing(false);
        }else {
            Toast.makeText(this, "Bad Request", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRefresh() {
        fetchData();
    }
}
