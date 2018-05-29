package com.devmarcul.maevent.request_builders;

import android.os.Bundle;

import com.android.volley.ClientError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devmarcul.maevent.apis.MaeventApi;
import com.devmarcul.maevent.apis.MaeventApiModel;
import com.devmarcul.maevent.receivers.NetworkReceiver;

import org.json.JSONObject;

import static com.devmarcul.maevent.services.NetworkService.RESULT_CODE_ERROR;
import static com.devmarcul.maevent.services.NetworkService.RESULT_CODE_OK;

public class JsonObjectRequestBuilder extends RequestBuilder<JsonObjectRequest> {

    public JsonObjectRequest build(String url, MaeventApiModel model) {
        boolean res = super.preBuild(url, model);
        if (!res) {
            return null;
        }

        final Bundle bundle = new Bundle();

        JsonObjectRequest request = new JsonObjectRequest(
                mRequestMethod, url, mBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                bundle.putSerializable(NetworkReceiver.PARAM_RESULT, true);
                mReceiver.send(RESULT_CODE_OK, bundle);
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
                mReceiver.send(RESULT_CODE_ERROR, bundle);
            }
        });

        request.setTag(MaeventApi.TAG);
        return request;
    }
}