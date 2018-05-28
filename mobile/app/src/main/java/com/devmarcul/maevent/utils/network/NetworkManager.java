package com.devmarcul.maevent.utils.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class NetworkManager {

    public static final String LOG_TAG = "NetworkManager";
    public static final String REQUEST_METHODS[] = {
            "GET", "POST"," PUT", "DELETE", "HEAD", "OPTIONS", "TRACE", "PATCH" };

    private static final NetworkManager instance = new NetworkManager();
    private static RequestQueue mRequestQueue;

    public interface RequestHandler
    {
        void onResponse(JSONArray response);
        void onError(VolleyError error);
    }

    public static synchronized NetworkManager getInstance(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        return instance;
    }

    private NetworkManager() {
    }

    public void addJsonArrayRequest(String tag, int requestMethod, String url, final RequestHandler handler)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(REQUEST_METHODS[requestMethod]).append(" Request ").append(url);
        Log.i(LOG_TAG, builder.toString());

        JsonArrayRequest request = new JsonArrayRequest
                (requestMethod, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        handler.onResponse(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handler.onError(error);
                    }
                });
        request.setTag(tag);
        mRequestQueue.add(request);

    }

    public void cancelAll(String tag) {
        mRequestQueue.cancelAll(tag);
    }

    public static class UrlBuilder
    {
        private StringBuilder mBuilder;

        public UrlBuilder() {
            mBuilder = new StringBuilder();
        }

        public UrlBuilder setBase(String base)
        {
            mBuilder.setLength(0);
            mBuilder.append(base);
            return this;
        }

        public String build(String container)
        {
            return mBuilder.append(container).toString();
        }
    }
}
