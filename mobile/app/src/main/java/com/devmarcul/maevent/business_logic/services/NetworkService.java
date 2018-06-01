package com.devmarcul.maevent.business_logic.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.ClientError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devmarcul.maevent.apis.MaeventApi;
import com.devmarcul.maevent.apis.models.MaeventModel;
import com.devmarcul.maevent.apis.models.UserModel;
import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkService extends IntentService implements MaeventApi {

    public static final String LOG_TAG = "NetworkService";

    public static final String REQUEST_METHODS[] = { "GET", "POST"," PUT", "DELETE", "HEAD", "OPTIONS", "TRACE", "PATCH" };
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

    // Do not use this constructor! This is only for service declaration in Android Manifest.
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
        else if (Action.GET_USER.name().equals(action)) {
            final String identifier = intent.getStringExtra(Param.STRING.name());
            if (identifier == null) {
                Log.d(LOG_TAG,"Invalid intent");
                return;
            }
            handleGetUser(receiver, identifier);
        }
        else if (Action.CREATE_USER.name().equals(action)) {
            final UserModel model = intent.getParcelableExtra(Param.USER.name());
            handleCreateUser(receiver, model);
        }
        else if (Action.UPDATE_USER.name().equals(action)) {
            final UserModel model = intent.getParcelableExtra(Param.USER.name());
            handleUpdateUser(receiver, model);
        }
    }

    public void startService(Context context, MaeventApi.Action action, MaeventApi.Param param, String str, NetworkReceiver.Callback callback) {
        NetworkReceiver receiver = new NetworkReceiver(new Handler(context.getMainLooper()), callback);

        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(action.name());
        intent.putExtra(param.name(), str);
        intent.putExtra(RESULT_RECEIVER, receiver);
        context.startService(intent);

        Log.d(LOG_TAG, action.name() + " Handling network intent service:");
        Log.d(LOG_TAG, action.name() + " " + str);
    }

    public void startService(Context context, MaeventApi.Action action, MaeventApi.Param param, Parcelable parcel, NetworkReceiver.Callback callback) {
        NetworkReceiver receiver = new NetworkReceiver(new Handler(context.getMainLooper()), callback);

        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(action.name());
        intent.putExtra(param.name(), parcel);
        intent.putExtra(RESULT_RECEIVER, receiver);
        context.startService(intent);

        Log.d(LOG_TAG, action.name() + " Handling network intent service:");
        Log.d(LOG_TAG, action.name() + " " + parcel);
    }

    @Override
    public void cancelAllRequests() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(MaeventApi.TAG);
        }
    }

    @Override
    public void handleGetEvents(final ResultReceiver receiver) {
        final Bundle bundle = new Bundle();

        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, MaeventApi.URL_EVENTS, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (receiver != null) {
                            bundle.putSerializable(NetworkReceiver.PARAM_RESULT, response.toString());
                            receiver.send(RESULT_CODE_OK, bundle);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof ClientError) {
                            bundle.putSerializable(NetworkReceiver.PARAM_EXCEPTION, new ClientError());
                        }
                        else {
                            bundle.putSerializable(NetworkReceiver.PARAM_EXCEPTION, new ServerError());
                        }
                        receiver.send(RESULT_CODE_ERROR, bundle);
                    }
                });
        mRequestQueue.add(request);
    }

    @Override
    public void handleCreateEvent(final ResultReceiver receiver, MaeventModel model) {
        JSONObject body;
        try {
            Gson gson = new Gson();
            body = new JSONObject(gson.toJson(model));
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Invalid request");
            return;
        }

        final Bundle bundle = new Bundle();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, MaeventApi.URL_EVENTS, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        bundle.putSerializable(NetworkReceiver.PARAM_RESULT, true);
                        receiver.send(RESULT_CODE_OK, bundle);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof ClientError) {
                            bundle.putSerializable(NetworkReceiver.PARAM_EXCEPTION, new ClientError());
                        }
                        else {
                            bundle.putSerializable(NetworkReceiver.PARAM_EXCEPTION, new ServerError());
                        }
                        receiver.send(RESULT_CODE_ERROR, bundle);
                    }
                });
        mRequestQueue.add(request);
    }

    @Override
    public void handleGetUser(final ResultReceiver receiver, String uid) {
        StringBuilder builder = new StringBuilder();
        builder.append(MaeventApi.URL_USERS).append(uid);
        String url = builder.toString();

        final Bundle bundle = new Bundle();

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        bundle.putSerializable(NetworkReceiver.PARAM_RESULT, response);
                        receiver.send(RESULT_CODE_OK, bundle);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof ClientError) {
                            bundle.putSerializable(NetworkReceiver.PARAM_EXCEPTION, new ClientError());
                        }
                        else {
                            bundle.putSerializable(NetworkReceiver.PARAM_EXCEPTION, new ServerError());
                        }
                        receiver.send(RESULT_CODE_ERROR, bundle);
                    }
                });
        mRequestQueue.add(request);
    }

    @Override
    public void handleCreateUser(final ResultReceiver receiver, UserModel model) {
        JSONObject body;
        try {
            Gson gson = new Gson();
            body = new JSONObject(gson.toJson(model));
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Invalid request");
            return;
        }

        final Bundle bundle = new Bundle();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, MaeventApi.URL_USERS, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        UserModel model;
                        try {
                            Gson gson = new Gson();
                            model = gson.fromJson(response.toString(), UserModel.class);
                        }
                        catch (Exception ex) {
                            model = null;
                        }
                        if (model == null) {
                            bundle.putSerializable(NetworkReceiver.PARAM_RESULT, response.toString());
                            receiver.send(RESULT_CODE_INTERNAL_ERROR, bundle);
                        }
                        else {
                            bundle.putSerializable(NetworkReceiver.PARAM_RESULT, String.valueOf(model.Id));
                            receiver.send(RESULT_CODE_OK, bundle);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ClientError) {
                    bundle.putSerializable(NetworkReceiver.PARAM_EXCEPTION, new ClientError());
                }
                else {
                    bundle.putSerializable(NetworkReceiver.PARAM_EXCEPTION, new ServerError());
                }
                receiver.send(RESULT_CODE_ERROR, bundle);
            }
        });
        mRequestQueue.add(request);
    }

    @Override
    public void handleUpdateUser(final ResultReceiver receiver, UserModel model) {
        JSONObject body;
        try {
            Gson gson = new Gson();
            body = new JSONObject(gson.toJson(model));
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Invalid request");
            return;
        }

        final Bundle bundle = new Bundle();

        StringBuilder builder = new StringBuilder();
        builder.append(MaeventApi.URL_USERS).append(model.Id);
        String url = builder.toString();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        UserModel model;
                        try {
                            Gson gson = new Gson();
                            model = gson.fromJson(response.toString(), UserModel.class);
                        }
                        catch (Exception ex) {
                            model = null;
                        }
                        if (model == null) {
                            bundle.putSerializable(NetworkReceiver.PARAM_RESULT, response.toString());
                            receiver.send(RESULT_CODE_INTERNAL_ERROR, bundle);
                        }
                        else {
                            bundle.putSerializable(NetworkReceiver.PARAM_RESULT, String.valueOf(model.Id));
                            receiver.send(RESULT_CODE_OK, bundle);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ClientError) {
                    bundle.putSerializable(NetworkReceiver.PARAM_EXCEPTION, new ClientError());
                }
                else {
                    bundle.putSerializable(NetworkReceiver.PARAM_EXCEPTION, new ServerError());
                }
                receiver.send(RESULT_CODE_ERROR, bundle);
            }
        });
        mRequestQueue.add(request);
    }
}
