package com.devmarcul.maevent.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devmarcul.maevent.apis.MaeventApi;
import com.devmarcul.maevent.apis.MaeventApiModel;
import com.devmarcul.maevent.apis.models.EventModel;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;
import com.devmarcul.maevent.receivers.NetworkReceiver;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkService extends IntentService implements MaeventApi {

    public static final String LOG_TAG = "NetworkService";
    public static final String REQUEST_METHODS[] = {
            "GET", "POST"," PUT", "DELETE", "HEAD", "OPTIONS", "TRACE", "PATCH" };

    private static final NetworkService instance = new NetworkService();
    private static RequestQueue mRequestQueue;

    public static synchronized NetworkService getInstance() {
        return instance;
    }

    // Do not use this constructor!
    public NetworkService() {
        super(NetworkService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this);
        }

        ResultReceiver receiver = intent.getParcelableExtra(RESULT_RECEIVER);
        final String action = intent.getAction();

        if (Action.GET_EVENTS.name().equals(action)) {
            handleGetEvents(receiver);
        }
        else if (Action.CREATE_EVENT.name().equals(action)) {
            final Maevent event = intent.getParcelableExtra(Param.EVENT.name());
            handleCreateEvent(receiver, event);
        }
    }

    public void startService(Context context, MaeventApi.Action action, MaeventApi.Param param, Parcelable parcel, NetworkReceiver.Callback callback) {
        NetworkReceiver receiver = new NetworkReceiver(new Handler(context.getMainLooper()));
        receiver.setReceiver(callback);

        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(action.name());
        intent.putExtra(param.name(), parcel);
        intent.putExtra(RESULT_RECEIVER, receiver);
        context.startService(intent);

        Log.d(LOG_TAG, action.name() + " Handling network intent service");
    }

    @Override
    public void handleGetEvents(final ResultReceiver receiver) {
        StringBuilder builder = new StringBuilder();
        builder.append(REQUEST_METHODS[Request.Method.GET]).append(" Request ").append(MaeventApi.URL_EVENTS);
        Log.i(LOG_TAG, builder.toString());

        final Bundle bundle = new Bundle();
        final int code = NetworkReceiver.RESULT_CODE_OK;

        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, MaeventApi.URL_EVENTS, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (receiver != null) {
                            bundle.putSerializable(NetworkReceiver.PARAM_RESULT, true);
                            receiver.send(code, bundle);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (receiver != null) {
                            bundle.putSerializable(NetworkReceiver.PARAM_RESULT, false);
                            receiver.send(code, bundle);
                        }
                    }
                });

        request.setTag(MaeventApi.TAG);
        mRequestQueue.add(request);
    }

    @Override
    public void handleCreateEvent(final ResultReceiver receiver, Maevent event) {
        StringBuilder builder = new StringBuilder();
        builder.append(REQUEST_METHODS[Request.Method.POST]).append(" Request ").append(MaeventApi.URL_EVENTS);
        Log.i(LOG_TAG, builder.toString());

        final Bundle bundle = new Bundle();
        final int code = NetworkReceiver.RESULT_CODE_OK;

        MaeventParams params = event.getParams();

        EventModel model = new EventModel();
        model.Name = params.name;
        model.Place = params.place;
        model.AddressStreet = params.addressStreet;
        model.AddressPostCode = params.addressPostCode;
        model.BeginTime = params.beginTime;
        model.EndTime = params.endTime;
        model.Rsvp = params.rsvp;
        model.HostId = 2;

        Gson gson = new Gson();
        JSONObject body = null;
        try {
            body = new JSONObject(gson.toJson(model));
        } catch (JSONException e) {
            bundle.putSerializable(NetworkReceiver.PARAM_RESULT, false);
            receiver.send(code, bundle);
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.POST, MaeventApi.URL_EVENTS, body, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (receiver != null) {
                            bundle.putSerializable(NetworkReceiver.PARAM_RESULT, true);
                            receiver.send(code, bundle);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (receiver != null) {
                            bundle.putSerializable(NetworkReceiver.PARAM_RESULT, false);
                            receiver.send(code, bundle);
                        }
                    }
                });

        request.setTag(MaeventApi.TAG);
        mRequestQueue.add(request);
    }

    @Override
    public void cancelAllRequests(String tag) {
        mRequestQueue.cancelAll(tag);
    }
}
