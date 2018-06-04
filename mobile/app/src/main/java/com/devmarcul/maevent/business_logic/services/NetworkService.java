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

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devmarcul.maevent.apis.MaeventApi;
import com.devmarcul.maevent.apis.models.InvitationModel;
import com.devmarcul.maevent.apis.models.InvitationsModel;
import com.devmarcul.maevent.apis.models.MaeventModel;
import com.devmarcul.maevent.apis.models.MaeventsModel;
import com.devmarcul.maevent.apis.models.UserModel;
import com.devmarcul.maevent.apis.models.UsersModel;
import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NetworkService extends IntentService implements MaeventApi {

    public static final String LOG_TAG = "NetworkService";

    public static final String REQUEST_METHODS[] = { "GET", "POST"," PUT", "DELETE", "HEAD", "OPTIONS", "TRACE", "PATCH" };
    public static final int RESULT_CODE_OK = 200;
    public static final int RESULT_CODE_OK_PARCEL = 250;
    public static final int RESULT_CODE_OK_PARCEL1 = 251;
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
        else if (Action.GET_EVENTS_INTENDED_FOR_USER.name().equals(action)) {
            final String identifier = intent.getStringExtra(Param.STRING.name());
            if (identifier == null) {
                Log.d(LOG_TAG,"Invalid intent");
                return;
            }
            handleGetEventsIntendedForUser(receiver, identifier);
        }
        else if (Action.CREATE_EVENT.name().equals(action)) {
            final MaeventModel model = intent.getParcelableExtra(Param.EVENT.name());
            handleCreateEvent(receiver, model);
        }
        else if (Action.GET_USERS.name().equals(action)) {
            handleGetUsers(receiver);
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
        else if (Action.GET_INVITATIONS.name().equals(action)) {
            handleGetInvitations(receiver);
        }
        else if (Action.GET_INVITATIONS_INTENDED_FOR_USER.name().equals(action)) {
            final String identifier = intent.getStringExtra(Param.STRING.name());
            if (identifier == null) {
                Log.d(LOG_TAG,"Invalid intent");
                return;
            }
            handleGetInvitationsIntendedForUser(receiver, identifier);
        }
    }

    public void startService(Context context, MaeventApi.Action action, MaeventApi.Param param, String str, NetworkReceiver.Callback callback) {
        NetworkReceiver receiver = new NetworkReceiver(new Handler(context.getMainLooper()), callback);

        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(action.name());
        intent.putExtra(param.name(), str);
        intent.putExtra(RESULT_RECEIVER, receiver);
        context.startService(intent);

        Log.d(LOG_TAG, action.name() + " Handling network intent service: " + str);
    }

    public void startService(Context context, MaeventApi.Action action, MaeventApi.Param param, Parcelable parcel, NetworkReceiver.Callback callback) {
        NetworkReceiver receiver = new NetworkReceiver(new Handler(context.getMainLooper()), callback);

        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(action.name());
        intent.putExtra(param.name(), parcel);
        intent.putExtra(RESULT_RECEIVER, receiver);
        context.startService(intent);

        Log.d(LOG_TAG, action.name() + " Handling network intent service: parcel");
    }

    public void startService(Context context, MaeventApi.Action action, MaeventApi.Param param, NetworkReceiver.Callback callback) {
        // This case param is none

        NetworkReceiver receiver = new NetworkReceiver(new Handler(context.getMainLooper()), callback);

        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(action.name());
        intent.putExtra(RESULT_RECEIVER, receiver);
        context.startService(intent);

        Log.d(LOG_TAG, action.name() + " Handling network intent service (without passing content)");
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

        StringRequest request = new StringRequest
                (Request.Method.GET, MaeventApi.URL_EVENTS, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MaeventsModel model;
                        try {
                            Gson gson = new Gson();
                            List<MaeventModel> content = gson.fromJson(response, new TypeToken<List<MaeventModel>>(){}.getType());
                            model = new MaeventsModel(content);
                        }
                        catch (JsonSyntaxException ex) {
                            Log.e(LOG_TAG, "Error while parsing JSON");
                            model = null;
                        }
                        catch (Exception ex) {
                            Log.e(LOG_TAG, ex.toString());
                            model = null;
                        }
                        if (model == null) {
                            bundle.putSerializable(NetworkReceiver.PARAM_RESULT, response);
                            receiver.send(RESULT_CODE_INTERNAL_ERROR, bundle);
                        }
                        else {
                            bundle.putParcelable(NetworkReceiver.PARAM_RESULT, model);
                            receiver.send(RESULT_CODE_OK_PARCEL, bundle);
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
    public void handleGetEventsIntendedForUser(final ResultReceiver receiver, String identifier) {
        final Bundle bundle = new Bundle();

        StringBuilder builder = new StringBuilder();
        builder.append(MaeventApi.URL_EVENTS_ATTENDEE).append("?")
                .append(URL_USER_ID).append(identifier);
        String url = builder.toString();

        StringRequest request = new StringRequest
                (Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MaeventsModel model;
                        try {
                            Gson gson = new Gson();
                            List<MaeventModel> content = gson.fromJson(response, new TypeToken<List<MaeventModel>>(){}.getType());
                            model = new MaeventsModel(content);
                        }
                        catch (JsonSyntaxException ex) {
                            Log.e(LOG_TAG, "Error while parsing JSON");
                            model = null;
                        }
                        catch (Exception ex) {
                            Log.e(LOG_TAG, ex.toString());
                            model = null;
                        }
                        if (model == null) {
                            bundle.putSerializable(NetworkReceiver.PARAM_RESULT, response);
                            receiver.send(RESULT_CODE_INTERNAL_ERROR, bundle);
                        }
                        else {
                            bundle.putParcelable(NetworkReceiver.PARAM_RESULT, model);
                            receiver.send(RESULT_CODE_OK_PARCEL, bundle);
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
                        bundle.putSerializable(NetworkReceiver.PARAM_EXCEPTION, error);
                        receiver.send(RESULT_CODE_ERROR, bundle);
                    }
                });
        mRequestQueue.add(request);
    }

    @Override
    public void handleGetUsers(final ResultReceiver receiver) {
        final Bundle bundle = new Bundle();

        StringRequest request = new StringRequest
                (Request.Method.GET, MaeventApi.URL_USERS, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UsersModel model;
                        try {
                            Gson gson = new Gson();
                            List<UserModel> content = gson.fromJson(response, new TypeToken<List<UserModel>>(){}.getType());
                            model = new UsersModel(content);
                        }
                        catch (JsonSyntaxException ex) {
                            Log.e(LOG_TAG, "Error while parsing JSON");
                            model = null;
                        }
                        catch (Exception ex) {
                            Log.e(LOG_TAG, ex.toString());
                            model = null;
                        }
                        if (model == null) {
                            bundle.putSerializable(NetworkReceiver.PARAM_RESULT, response);
                            receiver.send(RESULT_CODE_INTERNAL_ERROR, bundle);
                        }
                        else {
                            bundle.putParcelable(NetworkReceiver.PARAM_RESULT, model);
                            receiver.send(RESULT_CODE_OK_PARCEL, bundle);
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
    public void handleGetUser(final ResultReceiver receiver, String identifier) {
        StringBuilder builder = new StringBuilder();
        builder.append(MaeventApi.URL_USERS).append(identifier);
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

    @Override
    public void handleGetInvitations(final ResultReceiver receiver) {
        final Bundle bundle = new Bundle();

        StringRequest request = new StringRequest
                (Request.Method.GET, MaeventApi.URL_INVITATIONS, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        InvitationsModel model;
                        try {
                            Gson gson = new Gson();
                            List<InvitationModel> content = gson.fromJson(response, new TypeToken<List<InvitationModel>>(){}.getType());
                            model = new InvitationsModel(content);
                        }
                        catch (JsonSyntaxException ex) {
                            Log.e(LOG_TAG, "Error while parsing JSON");
                            model = null;
                        }
                        catch (Exception ex) {
                            Log.e(LOG_TAG, ex.toString());
                            model = null;
                        }
                        if (model == null) {
                            bundle.putSerializable(NetworkReceiver.PARAM_RESULT, response);
                            receiver.send(RESULT_CODE_INTERNAL_ERROR, bundle);
                        }
                        else {
                            bundle.putParcelable(NetworkReceiver.PARAM_RESULT, model);
                            receiver.send(RESULT_CODE_OK_PARCEL1, bundle);
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
    public void handleGetInvitationsIntendedForUser(final ResultReceiver receiver, String identifier) {
        final Bundle bundle = new Bundle();

        StringBuilder builder = new StringBuilder();
        builder.append(MaeventApi.URL_INVITATIONS_INVITEE).append("?")
                .append(URL_USER_ID).append(identifier).append("&")
                .append(URL_EVENT_ID).append(URL_ALL);
        String url = builder.toString();

        StringRequest request = new StringRequest
                (Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        InvitationsModel model;
                        try {
                            Gson gson = new Gson();
                            List<InvitationModel> content = gson.fromJson(response, new TypeToken<List<InvitationModel>>(){}.getType());
                            model = new InvitationsModel(content);
                        }
                        catch (JsonSyntaxException ex) {
                            Log.e(LOG_TAG, "Error while parsing JSON");
                            model = null;
                        }
                        catch (Exception ex) {
                            Log.e(LOG_TAG, ex.toString());
                            model = null;
                        }
                        if (model == null) {
                            bundle.putSerializable(NetworkReceiver.PARAM_RESULT, response);
                            receiver.send(RESULT_CODE_INTERNAL_ERROR, bundle);
                        }
                        else {
                            bundle.putParcelable(NetworkReceiver.PARAM_RESULT, model);
                            receiver.send(RESULT_CODE_OK_PARCEL1, bundle);
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
