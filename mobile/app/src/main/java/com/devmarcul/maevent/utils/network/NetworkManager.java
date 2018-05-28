package com.devmarcul.maevent.utils.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class NetworkManager {

    public static final String LOG_TAG = "NetworkManager";

    private static final NetworkManager instance = new NetworkManager();
    private static RequestQueue mRequestQueue;

    public interface Requester
    {
        void onResponse(JSONObject response);
        void onError(VolleyError error);
    }

    public static NetworkManager getInstance(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        return instance;
    }

    private NetworkManager() {
    }

    public void addJsonRequest(String tag, int requestMethod, String url, final Requester requester)
    {
        JsonObjectRequest  request = new JsonObjectRequest
                (requestMethod, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        requester.onResponse(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requester.onError(error);
                    }
                });
        request.setTag(tag);
        mRequestQueue.add(request);

    }

    public void cancelAll(String tag)
    {
        mRequestQueue.cancelAll(tag);
    }
}
