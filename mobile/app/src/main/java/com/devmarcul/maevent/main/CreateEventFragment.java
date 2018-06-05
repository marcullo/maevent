package com.devmarcul.maevent.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.ServerError;
import com.devmarcul.maevent.InviteActivity;
import com.devmarcul.maevent.MainActivity;
import com.devmarcul.maevent.R;
import com.devmarcul.maevent.apis.models.MaeventModel;
import com.devmarcul.maevent.business_logic.MaeventManager;
import com.devmarcul.maevent.business_logic.ThisUser;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserProfile;
import com.devmarcul.maevent.main.create_event.CreateEventViewAdapter;
import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.devmarcul.maevent.business_logic.services.NetworkService;
import com.devmarcul.maevent.utils.CustomTittleSetter;
import com.devmarcul.maevent.utils.Prompt;
import com.devmarcul.maevent.utils.Tools;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class CreateEventFragment extends Fragment implements
        CustomTittleSetter,
        CreateEventViewAdapter.Handler {

    private static final String LOG_TAG = "CreateEventFragment";

    private Activity parent;
    private View view;

    private CreateEventViewAdapter mCreateEventAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parent = getActivity();
        view = inflater.inflate(R.layout.main_create_event, container, false);
        View dialogView = inflater.inflate(R.layout.main_create_event_dialog, container, false);

        mCreateEventAdapter = new CreateEventViewAdapter(this, view, parent, dialogView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle();
        mCreateEventAdapter.bindListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        NetworkService.getInstance().cancelAllRequests();
        mCreateEventAdapter.unbindListeners();
    }

    @Override
    public void setTitle() {
        ActionBar bar = ((AppCompatActivity)parent).getSupportActionBar();
        if (bar != null) {
            int titleRes = R.string.toolbar_title_create_event;
            bar.setTitle(titleRes);
        }
    }

    @Override
    public void onCreateEventButtonClicked(MaeventParams params) {
        mCreateEventAdapter.unbindListeners();
        createEvent(params);
    }

    @Override
    public void onHideKeyboardRequested() {
        Tools.hideSoftKeyboard(parent, view);
    }

    @Override
    public void onPickPlaceRequested() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            parent.startActivityForResult(builder.build(parent), MainActivity.PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Place place = null;
        if (requestCode == MainActivity.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                place = PlacePicker.getPlace(parent, data);
            }
        }
        mCreateEventAdapter.updatePlaceViews(place);
    }

    private void createEvent(final MaeventParams params) {
        if (!Maevent.areParamsValid(params)) {
            Prompt.displayShort("Invalid parameters!", parent);
            return;
        }

        ThisUser.updateContent(parent);
        UserProfile profile = ThisUser.getProfile();
        if (profile == null) {
            throw new IllegalStateException("ThisUser profile must not be null");
        }

        User host = new User();
        host.setProfile(profile);

        final Maevent event = new Maevent();
        event.setParams(params);
        event.setHost(host);
        event.setAttendeesIds(";" + String.valueOf(host.getProfile().id) + ";");

        MaeventManager.getInstance().createEvent(parent, event, new NetworkReceiver.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean success) {
                Prompt.displayShort("Success!", parent);
                mCreateEventAdapter.setCreateEventLoadingGone();
                mCreateEventAdapter.updateCreateEventButtonVisibility();
                mCreateEventAdapter.bindListeners();

                startInviteActivity(event);
            }

            @Override
            public void onError(Exception exception) {
                if (exception instanceof ClientError) {
                    Prompt.displayShort("Event name exists. Choose another one.", parent);
                }
                else if (exception instanceof AuthFailureError) {
                    Prompt.displayShort("Your profile is not registered in the system!", parent);
                }
                else {
                    Prompt.displayShort("No connection with server.", parent);
                }
                mCreateEventAdapter.setCreateEventLoadingGone();
                mCreateEventAdapter.updateCreateEventButtonVisibility();
                mCreateEventAdapter.bindListeners();
            }
        });
    }

    private void startInviteActivity(Maevent event) {
        if (event == null) {
            return;
        }
        if (!event.isValid()) {
            return;
        }

        MaeventModel focusedModel = new MaeventModel(event);

        Log.d(LOG_TAG, "Setting next activity.");
        Intent intent = new Intent(parent, InviteActivity.class);
        intent.putExtra(InviteActivity.KEY_PARCEL_FOCUSED_EVENT_MODEL, focusedModel);
        startActivity(intent);
    }
}
