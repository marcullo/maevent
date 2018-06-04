package com.devmarcul.maevent.business_logic.receivers;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.devmarcul.maevent.business_logic.services.NetworkService;

public class NetworkReceiver<T> extends ResultReceiver {

    public static final String LOG_TAG = "NetworkReceiver";
    public static final String PARAM_EXCEPTION = "exception";
    public static final String PARAM_RESULT = "result-serializable";

    private Callback mCallback;

    public interface Callback<T>
    {
        void onSuccess(T data);
        void onError(Exception exception);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == NetworkService.RESULT_CODE_OK_PARCEL || resultCode == NetworkService.RESULT_CODE_OK_PARCEL1) {
            mCallback.onSuccess(resultData.getParcelable(PARAM_RESULT));
        }
        else if (resultCode == NetworkService.RESULT_CODE_OK) {
            mCallback.onSuccess(resultData.getSerializable(PARAM_RESULT));
        }
        else if (resultCode == NetworkService.RESULT_CODE_INTERNAL_ERROR) {
            mCallback.onError(new Exception());
        }
        else if (resultCode == NetworkService.RESULT_CODE_CLIENT_ERROR
                || resultCode == NetworkService.RESULT_CODE_SERVER_ERROR) {
            mCallback.onError((Exception) resultData.getSerializable(PARAM_EXCEPTION));
        }
        else {
            mCallback.onError((Exception) resultData.getSerializable(PARAM_EXCEPTION));
        }

        StringBuilder builder = new StringBuilder();
        builder.append("Result code: ").append(resultCode).append(", data: ").append(resultData.toString());
        Log.i(LOG_TAG, builder.toString());
    }

    public NetworkReceiver(Handler handler, Callback callback) {
        super(handler);
        mCallback = callback;
    }
}
