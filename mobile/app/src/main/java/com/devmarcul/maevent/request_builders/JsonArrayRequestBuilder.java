package com.devmarcul.maevent.request_builders;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.devmarcul.maevent.apis.MaeventApi;
import com.devmarcul.maevent.apis.MaeventApiModel;
import com.devmarcul.maevent.receivers.NetworkReceiver;

import org.json.JSONArray;

import static com.devmarcul.maevent.services.NetworkService.RESULT_CODE_OK;

public class JsonArrayRequestBuilder extends RequestBuilder<JsonArrayRequest> {

    public JsonArrayRequest build(String url, MaeventApiModel model) {
        boolean res = super.preBuild(url, model);
        if (!res) {
            return null;
        }

        final Bundle bundle = new Bundle();
        final int code = RESULT_CODE_OK;

        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, MaeventApi.URL_EVENTS, null, new Response.Listener<JSONArray>() {
                        @Override
                    public void onResponse(JSONArray response) {
                        if (mReceiver != null) {
                            bundle.putSerializable(NetworkReceiver.PARAM_RESULT, true);
                            mReceiver.send(code, bundle);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        bundle.putSerializable(NetworkReceiver.PARAM_RESULT, false);
                        mReceiver.send(code, bundle);
                    }
                });

        request.setTag(MaeventApi.TAG);
        return request;
    }

}
