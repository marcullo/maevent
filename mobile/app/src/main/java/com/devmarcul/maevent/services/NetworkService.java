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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.devmarcul.maevent.apis.MaeventApi;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.receivers.NetworkReceiver;
import com.devmarcul.maevent.utils.network.NetworkManager;

import org.json.JSONArray;

public class NetworkService extends IntentService implements MaeventApi {

    public static String LOG_TAG = "NetworkService";
    private static final NetworkService instance = new NetworkService();

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
        final Bundle bundle = new Bundle();
        final int code = NetworkReceiver.RESULT_CODE_OK;

        NetworkManager.getInstance(this)
                .addJsonArrayRequest(
                        MaeventApi.TAG, Request.Method.GET, MaeventApi.URL_EVENTS, new NetworkManager.RequestHandler() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (receiver != null) {
                            bundle.putSerializable(NetworkReceiver.PARAM_RESULT, true);
                            receiver.send(code, bundle);
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        if (receiver != null) {
                            bundle.putSerializable(NetworkReceiver.PARAM_RESULT, false);
                            receiver.send(code, bundle);
                        }
                    }
                });
    }

    @Override
    public void handleCreateEvent(final ResultReceiver receiver, Maevent event) {
        //TODO
    }
}
