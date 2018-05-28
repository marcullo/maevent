package com.devmarcul.maevent.receivers;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

public class NetworkReceiver<T> extends ResultReceiver {

    public static final String LOG_TAG = "NetworkReceiver";

    public static final int RESULT_CODE_OK = 200;
    public static final int RESULT_CODE_ERROR = 400;
    public static final String PARAM_EXCEPTION = "exception";
    public static final String PARAM_RESULT = "result";

    private Callback mCallback;

    public interface Callback<T>
    {
        void onSuccess(T data);
        void onError(Exception exception);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        Log.i(LOG_TAG, "Server answered");

        if (mCallback != null) {
            mCallback.onSuccess(resultData.getSerializable(PARAM_RESULT));
        }
        else {
            mCallback.onError((Exception)resultData.getSerializable(PARAM_EXCEPTION));
        }
    }

    public NetworkReceiver(Handler handler) {
        super(handler);
    }
    public void setReceiver(Callback<T> callback) {
        mCallback = callback;
    }
}
