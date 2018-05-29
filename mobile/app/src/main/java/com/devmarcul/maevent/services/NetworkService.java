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
import com.devmarcul.maevent.apis.models.MaeventModel;
import com.devmarcul.maevent.receivers.NetworkReceiver;
import com.devmarcul.maevent.request_builders.JsonArrayRequestBuilder;
import com.devmarcul.maevent.request_builders.JsonObjectRequestBuilder;

import org.json.JSONArray;

public class NetworkService extends IntentService implements MaeventApi {

    public static final String LOG_TAG = "NetworkService";

    public static final String REQUEST_METHODS[] = { "GET", "POST"," PUT", "DELETE", "HEAD", "OPTIONS", "TRACE", "PATCH" };
    public enum ResultCode { Ok, ClientError, ServerError };
    public static final int RESULT_CODE_OK = 200;
    public static final int RESULT_CODE_CLIENT_ERROR = 400;
    public static final int RESULT_CODE_SERVER_ERROR = 500;
    public static final int RESULT_CODE_INTERNAL_ERROR = 999;
    public static final int RESULT_CODE_ERROR = 1000;

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
            final MaeventModel model = intent.getParcelableExtra(Param.EVENT.name());
            handleCreateEvent(receiver, model);
        }
    }

    public void startService(Context context, MaeventApi.Action action, MaeventApi.Param param, Parcelable parcel, NetworkReceiver.Callback callback) {
        NetworkReceiver receiver = new NetworkReceiver(new Handler(context.getMainLooper()), callback);

        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(action.name());
        intent.putExtra(param.name(), parcel);
        intent.putExtra(RESULT_RECEIVER, receiver);
        context.startService(intent);

        Log.d(LOG_TAG, action.name() + " Handling network intent service");
    }

    @Override
    public void handleGetEvents(final ResultReceiver receiver) {
        JsonArrayRequest request = (JsonArrayRequest) new JsonArrayRequestBuilder()
            .setReceiver(receiver)
            .setMethod(Request.Method.GET)
            .build(MaeventApi.URL_EVENTS, null);

        if (request == null) {
            Log.e(LOG_TAG, "Invalid request");
            return;
        }
        mRequestQueue.add(request);
    }

    @Override
    public void handleCreateEvent(final ResultReceiver receiver, MaeventModel model) {
        JsonObjectRequest request = (JsonObjectRequest) new JsonObjectRequestBuilder()
                .setReceiver(receiver)
                .setMethod(Request.Method.POST)
                .build(MaeventApi.URL_EVENTS, model);

        if (request == null) {
            Log.e(LOG_TAG, "Invalid request");
            return;
        }
        mRequestQueue.add(request);
    }

    @Override
    public void cancelAllRequests() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(MaeventApi.TAG);
        }
    }
}
