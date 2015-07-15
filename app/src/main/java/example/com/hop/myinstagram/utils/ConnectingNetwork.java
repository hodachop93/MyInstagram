package example.com.hop.myinstagram.utils;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ConnectingNetwork {
    public static final String TAG = ConnectingNetwork.class.getSimpleName();
    public static final int READ_TIMEOUT = 10 * 1000;
    public static final int CONNECT_TIMEOUT = 15 * 1000;

    private static ConnectingNetwork mInstance;

    private ConnectingNetwork() {
    }

    public static ConnectingNetwork getInstance() {
        if (mInstance == null) {
            mInstance = new ConnectingNetwork();
        }

        return mInstance;
    }

    private void addHeader(HttpURLConnection conn) {
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Language", "en-US");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
    }

    public String executePost(String urlString, ArrayList<NameValuePair> params) throws IOException, JSONException {
        String result = null;
        InputStream is = null;
        HttpURLConnection conn = null;

        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // add header
            addHeader(conn);

            // add content body
            writeStream(conn.getOutputStream(), params);

            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            if (response < 400) {
                is = conn.getInputStream();
                result = readStream(is);

            } else {
                is = conn.getErrorStream();
                result = readStream(is);
            }

        } finally {
            if (is != null) {
                is.close();
            }

            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;
    }

    public String executeGet(String urlString) throws IOException, JSONException {
        String result = null;
        InputStream is = null;
        HttpURLConnection conn = null;

        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setDoInput(true);

            // add header
            addHeader(conn);

            // Starts the query
            conn.connect();

            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            result = readStream(is);

        } finally {
            if (is != null) {
                is.close();
            }

            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;
    }

    private void writeStream(OutputStream out, ArrayList<NameValuePair> params) throws IOException {
        DataOutputStream writer = new DataOutputStream(out);
        writer.writeBytes(getQuery(params));
        writer.flush();
        writer.close();
    }

    private String readStream(InputStream is) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
            total.append("\r");
        }

        return total.toString();
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
