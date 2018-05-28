package com.devmarcul.maevent.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.devmarcul.maevent.MainActivity;
import com.devmarcul.maevent.R;
import com.devmarcul.maevent.business_logic.MaeventManager;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;
import com.devmarcul.maevent.main.common.EventDetailsViewHolder;
import com.devmarcul.maevent.receivers.NetworkReceiver;
import com.devmarcul.maevent.utils.CustomTittleSetter;
import com.devmarcul.maevent.utils.Prompt;
import com.devmarcul.maevent.utils.Utils;
import com.devmarcul.maevent.utils.dialog.DetailsDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Calendar;

public class MaeventFragment extends Fragment implements CustomTittleSetter {

    private Activity parent;

    private MaeventParams mEventParamsBuffer;

    private View view;
    private ImageView calendarTimeSelectedView;
    private TextView calendarLabelView;
    private View mMaeventSelectTimeView;
    private View mMaeventSelectPlaceView;
    private TextView mCreateEventTimeView;
    private EditText mCreateEventNameView;

    private View mMaeventSelectNameView;
    private TextView mCreateEventNameLabelView;
    private TextView mCreateEventNameSelectedTextView;
    private ImageView mCreateEventNameSelectedView;

    private DetailsDialog mDialog;
    private View mDialogView;

    private DateRangeCalendarView mSelectTimeCalendarView;

    private View mLoadingView;
    private TextView mSelectedPlaceNameView;
    private ImageView mCreateEventPlaceSelectedView;
    private TextView mCreateEventPlaceSelectedTextView;

    private View mMaeventSelectRsvpView;
    private ImageView mCreateEventRsvpSelectedView;
    private TextView mCreateEventRsvpSelectedTextView;
    private TextView mMaeventSelectedRsvpView;
    private CheckBox mCreateEventRsvpSelectView;

    private FloatingActionButton mCreateMaeventButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parent = getActivity();
        view = inflater.inflate(R.layout.main_maevent, container, false);
        mDialogView = inflater.inflate(R.layout.main_maevent_dialog, container, false);

        DetailsDialog.Builder builder = new DetailsDialog.Builder(parent, mDialogView);
        mDialog = builder.build(false);

        mLoadingView = mDialogView.findViewById(R.id.select_place_loading_indicator);
        mSelectTimeCalendarView = mDialogView.findViewById(R.id.dr_create_event_calendar);
        mSelectTimeCalendarView.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                mEventParamsBuffer.beginTime = null;
                mEventParamsBuffer.endTime = null;
                if (startDate != null && endDate != null) {
                    mEventParamsBuffer.beginTime = Utils.getStringFromCalendar(startDate, MaeventParams.TIME_FORMAT);
                    mEventParamsBuffer.endTime = Utils.getStringFromCalendar(endDate, MaeventParams.TIME_FORMAT);

                    String dateString = Utils.getStringFromCalendarDuration(startDate, endDate, EventDetailsViewHolder.TIME_FORMAT);

                    calendarTimeSelectedView.setVisibility(View.VISIBLE);
                    calendarLabelView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorCreatingEventOk));
                    mCreateEventTimeView.setText(dateString);
                    mCreateEventTimeView.setVisibility(View.VISIBLE);
                    mSelectTimeCalendarView.setVisibility(View.INVISIBLE);
                    mDialog.hide();

                    updateCreateEventButtonVisibility();
                }
            }

            @Override
            public void onCancel() {
                calendarTimeSelectedView.setVisibility(View.INVISIBLE);
                mCreateEventTimeView.setVisibility(View.INVISIBLE);
                calendarLabelView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorSecondaryText));

                updateCreateEventButtonVisibility();
            }
        });

        calendarTimeSelectedView = view.findViewById(R.id.iv_calendar_time_selected);
        calendarLabelView = view.findViewById(R.id.tv_create_event_calendar_label);
        mMaeventSelectTimeView = view.findViewById(R.id.main_maevent_select_time);
        mMaeventSelectTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCreateMaeventButton.setVisibility(View.GONE);
                mSelectTimeCalendarView.setVisibility(View.VISIBLE);
                mLoadingView.setVisibility(View.INVISIBLE);
                mSelectTimeCalendarView.setVisibility(View.VISIBLE);
                mDialog.show();
            }
        });
        mCreateEventTimeView = view.findViewById(R.id.tv_create_event_time);

        mCreateEventNameView = view.findViewById(R.id.et_select_name);
        mCreateEventNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null) {
                    return;
                }

                if (Maevent.isNameValid(s.toString())) {
                    mEventParamsBuffer.name = s.toString();
                    mCreateEventNameSelectedTextView.setText(s);
                    textNameChangedProperly();
                }
                else {
                    textNameChangedImproperly();
                }
                updateCreateEventButtonVisibility();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mCreateEventNameLabelView = view.findViewById(R.id.tv_create_event_name_selected_label);
        mCreateEventNameSelectedView = view.findViewById(R.id.iv_create_event_name_selected);

        mMaeventSelectNameView = view.findViewById(R.id.main_maevent_select_name);
        mMaeventSelectNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCreateEventNameView.getVisibility() == View.GONE) {
                    mCreateEventNameLabelView.setVisibility(View.GONE);
                    mCreateEventNameSelectedView.setVisibility(View.GONE);
                    mCreateEventNameView.setVisibility(View.VISIBLE);
                }
            }
        });

        mCreateEventNameSelectedTextView = view.findViewById(R.id.tv_maevent_selected_name_label);

        mMaeventSelectPlaceView = view.findViewById(R.id.main_maevent_select_place);
        mMaeventSelectPlaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCreateMaeventButton.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.VISIBLE);
                mSelectTimeCalendarView.setVisibility(View.INVISIBLE);
                mDialog.show();

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    parent.startActivityForResult(builder.build(parent), MainActivity.PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        mCreateEventPlaceSelectedTextView = view.findViewById(R.id.tv_maevent_selected_name_place);
        mSelectedPlaceNameView = view.findViewById(R.id.tv_create_event_place_selected_label);
        mCreateEventPlaceSelectedView = view.findViewById(R.id.iv_create_event_place_selected);

        mCreateMaeventButton = view.findViewById(R.id.btn_main_maevent_create);
        mCreateMaeventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingView.setVisibility(View.VISIBLE);
                mSelectTimeCalendarView.setVisibility(View.INVISIBLE);
                mDialog.show();
                createEvent();
                mDialog.hide();
            }
        });

        mMaeventSelectRsvpView = view.findViewById(R.id.main_maevent_select_rsvp);
        mMaeventSelectRsvpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = !mCreateEventRsvpSelectView.isChecked();
                mEventParamsBuffer.rsvp = checked;
                mCreateEventRsvpSelectView.setChecked(checked);
                mMaeventSelectedRsvpView.setVisibility(View.VISIBLE);
                updateCreateEventButtonVisibility();
            }
        });
        mMaeventSelectedRsvpView = view.findViewById(R.id.tv_maevent_selected_rsvp);
        mCreateEventRsvpSelectedView = view.findViewById(R.id.iv_create_event_rsvp_selected);
        mCreateEventRsvpSelectedTextView = view.findViewById(R.id.tv_create_event_name_rsvp_label);
        mCreateEventRsvpSelectView = view.findViewById(R.id.cb_maevent_rsvp);
        mCreateEventRsvpSelectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = mCreateEventRsvpSelectView.isChecked();
                mEventParamsBuffer.rsvp = checked;
                mMaeventSelectedRsvpView.setVisibility(View.VISIBLE);
                updateCreateEventButtonVisibility();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mEventParamsBuffer = new MaeventParams();
        setTitle();
    }

    @Override
    public void setTitle() {
        ActionBar bar = ((AppCompatActivity)parent).getSupportActionBar();
        if (bar != null) {
            int titleRes = R.string.toolbar_title_create_event;
            bar.setTitle(titleRes);
        }
    }

    public void createEvent() {
        MaeventManager.getInstance().createEvent(parent, mEventParamsBuffer, new NetworkReceiver.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                Prompt.displayShort("Success!", parent);
            }

            @Override
            public void onError(Exception exception) {
                Prompt.displayShort("Failure!", parent);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(parent, data);

                mEventParamsBuffer.place = place.getName().toString();
                mEventParamsBuffer.addressStreet = place.getAddress().toString();
                mEventParamsBuffer.addressPostCode = "TODO EXTRACT FROM getAddress";

                StringBuilder sb = new StringBuilder();
                sb.append(place.getName()).append(Utils.getNewLine()).append(place.getAddress());
                mCreateEventPlaceSelectedTextView.setText(sb.toString());
                placeSelectedProperly();
            }
            else {
                if (mCreateEventPlaceSelectedView.getVisibility() != View.VISIBLE) {
                    placeSelectedImproperly();
                }
            }
            updateCreateEventButtonVisibility();
            mDialog.hide();
        }
    }

    private void textNameChangedProperly() {
        mCreateEventNameView.setVisibility(View.GONE);
        mCreateEventNameLabelView.setVisibility(View.VISIBLE);
        mCreateEventNameLabelView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorCreatingEventOk));
        mCreateEventNameSelectedView.setVisibility(View.VISIBLE);
        mCreateEventNameSelectedTextView.setVisibility(View.VISIBLE);
    }

    private void textNameChangedImproperly() {
        mCreateEventNameLabelView.setVisibility(View.GONE);
        mCreateEventNameSelectedView.setVisibility(View.GONE);
        mCreateEventNameView.setVisibility(View.VISIBLE);
        mCreateEventNameSelectedView.setVisibility(View.GONE);
        mCreateEventNameSelectedTextView.setVisibility(View.INVISIBLE);
    }

    private void placeSelectedProperly() {
        mSelectedPlaceNameView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorCreatingEventOk));
        mCreateEventPlaceSelectedView.setVisibility(View.VISIBLE);
        mCreateEventPlaceSelectedTextView.setVisibility(View.VISIBLE);
    }

    private void placeSelectedImproperly() {
        mSelectedPlaceNameView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorSecondaryText));
        mCreateEventPlaceSelectedView.setVisibility(View.GONE);
    }

    private void updateCreateEventButtonVisibility() {
        Maevent event = new Maevent();
        event.setParams(mEventParamsBuffer);
        if (event.isValid()) {
            String textYes = parent.getString(R.string.text_yes);
            String textNo = parent.getString(R.string.text_no);
            String textRsvp = parent.getString(R.string.main_create_event_rsvp);
            boolean rsvpTrue = mCreateEventRsvpSelectView.isChecked();

            StringBuilder sb = new StringBuilder();
            sb.append(textRsvp).append(": ").append(rsvpTrue ? textYes : textNo);
            String rsvp = sb.toString();

            mMaeventSelectedRsvpView.setText(rsvp);
            mCreateMaeventButton.setVisibility(View.VISIBLE);
            mMaeventSelectedRsvpView.setVisibility(View.VISIBLE);
        }
        else {
            mCreateMaeventButton.setVisibility(View.GONE);
        }
    }
}
