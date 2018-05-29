package com.devmarcul.maevent.request_builders;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.devmarcul.maevent.apis.MaeventApiModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import static com.devmarcul.maevent.services.NetworkService.LOG_TAG;
import static com.devmarcul.maevent.services.NetworkService.REQUEST_METHODS;
import static com.devmarcul.maevent.services.NetworkService.RESULT_CODE_INTERNAL_ERROR;

public abstract class RequestBuilder<T> {

    protected ResultReceiver mReceiver;
    protected int mRequestMethod;
    protected JSONObject mBody;

    public RequestBuilder setReceiver(ResultReceiver receiver) {
        mReceiver = receiver;
        return this;
    }

    public RequestBuilder setMethod(int requestMethod) {
        mRequestMethod = requestMethod;
        return this;
    }

    protected boolean preBuild(String url, MaeventApiModel model) {
        StringBuilder builder = new StringBuilder();
        builder.append(REQUEST_METHODS[mRequestMethod]).append(" Request ").append(url);
        Log.i(LOG_TAG, builder.toString());

        if (model == null) {
            return true;
        }

        final Bundle bundle = new Bundle();

        Gson gson = new Gson();
        try {
            mBody = new JSONObject(gson.toJson(model));
        } catch (JSONException e) {
            mReceiver.send(RESULT_CODE_INTERNAL_ERROR, bundle);
            return false;
        }

        return true;
    }

    public abstract T build(String url, MaeventApiModel model);
}
