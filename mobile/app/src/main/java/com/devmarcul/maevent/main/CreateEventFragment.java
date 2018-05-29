package com.devmarcul.maevent.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devmarcul.maevent.MainActivity;
import com.devmarcul.maevent.R;
import com.devmarcul.maevent.business_logic.MaeventManager;
import com.devmarcul.maevent.data.MaeventParams;
import com.devmarcul.maevent.main.create_event.CreateEventViewAdapter;
import com.devmarcul.maevent.receivers.NetworkReceiver;
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
        mCreateEventAdapter.bindListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
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

    private void createEvent(MaeventParams params) {
        MaeventManager.getInstance().createEvent(parent, params, new NetworkReceiver.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean success) {
                if (success) {
                    Prompt.displayShort("Success!", parent);
                }
                else {
                    Prompt.displayShort("Server refused request or unavailable", parent);
                }
                mCreateEventAdapter.setCreateEventLoadingGone();
                mCreateEventAdapter.updateCreateEventButtonVisibility();
                mCreateEventAdapter.bindListeners();
            }

            @Override
            public void onError(Exception exception) {
                Prompt.displayShort("Failure!", parent);
                mCreateEventAdapter.setCreateEventLoadingGone();
                mCreateEventAdapter.updateCreateEventButtonVisibility();
                mCreateEventAdapter.bindListeners();
            }
        });
    }
}