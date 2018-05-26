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
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;
import com.devmarcul.maevent.data.ThisUser;
import com.devmarcul.maevent.main.common.EventDetailsViewHolder;
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

    private View mSelectTimeView;
    private DetailsDialog mSelectTimeDialog;
    private DateRangeCalendarView mSelectTimeCalendarView;

    private View mLoadingView;
    private TextView mSelectedPlaceNameView;
    private ImageView mCreateEventPlaceSelectedView;
    private TextView mCreateEventPlaceSelectedTextView;
    private DetailsDialog mLoadingDialog;

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

        mSelectTimeView = inflater.inflate(R.layout.main_select_time, container, false);
        mLoadingView =inflater.inflate(R.layout.main_maevent_loading, container, false);
        view = inflater.inflate(R.layout.main_maevent, container, false);

        calendarTimeSelectedView = view.findViewById(R.id.iv_calendar_time_selected);
        calendarLabelView = view.findViewById(R.id.tv_create_event_calendar_label);
        mMaeventSelectTimeView = view.findViewById(R.id.main_maevent_select_time);
        mMaeventSelectTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectTimeDialog.show();
            }
        });
        mCreateEventTimeView = view.findViewById(R.id.tv_create_event_time);

        mSelectTimeCalendarView = mSelectTimeView.findViewById(R.id.dr_create_event_calendar);
        mSelectTimeCalendarView.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                if (startDate != null && endDate != null) {
                    String dateString = Utils.getStringFromCalendarDuration(startDate, endDate, EventDetailsViewHolder.TIME_FORMAT);

                    calendarTimeSelectedView.setVisibility(View.VISIBLE);
                    calendarLabelView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorCreatingEventOk));
                    mCreateEventTimeView.setText(dateString);
                    mCreateEventTimeView.setVisibility(View.VISIBLE);
                    mSelectTimeDialog.hide();
                }

                updateCreateEventButtonVisibility();
            }

            @Override
            public void onCancel() {
                calendarTimeSelectedView.setVisibility(View.INVISIBLE);
                mCreateEventTimeView.setVisibility(View.INVISIBLE);
                calendarLabelView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorSecondaryText));

                updateCreateEventButtonVisibility();
            }
        });
        DetailsDialog.Builder builder = new DetailsDialog.Builder(view.getContext(), mSelectTimeView);
        mSelectTimeDialog = builder.build(false);

        mCreateEventNameView = view.findViewById(R.id.et_select_name);
        mCreateEventNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Maevent.isNameValid(s.toString())) {
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
                mLoadingDialog.show();
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    parent.startActivityForResult(builder.build(parent), MainActivity.PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        builder = new DetailsDialog.Builder(view.getContext(), mLoadingView);
        mLoadingDialog = builder.build(true);

        mCreateEventPlaceSelectedTextView = view.findViewById(R.id.tv_maevent_selected_name_place);
        mSelectedPlaceNameView = view.findViewById(R.id.tv_create_event_place_selected_label);
        mCreateEventPlaceSelectedView = view.findViewById(R.id.iv_create_event_place_selected);

        mCreateMaeventButton = view.findViewById(R.id.btn_main_maevent_create);
        mCreateMaeventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingDialog.show();
                createEvent();
            }
        });

        mMaeventSelectRsvpView = view.findViewById(R.id.main_maevent_select_rsvp);
        mMaeventSelectRsvpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleRsvp();
            }
        });
        mMaeventSelectedRsvpView = view.findViewById(R.id.tv_maevent_selected_rsvp);
        mCreateEventRsvpSelectedView = view.findViewById(R.id.iv_create_event_rsvp_selected);
        mCreateEventRsvpSelectedTextView = view.findViewById(R.id.tv_create_event_name_rsvp_label);
        mCreateEventRsvpSelectView = view.findViewById(R.id.cb_maevent_rsvp);

        return view;
    }

    public void toggleRsvp() {
        boolean rsvpChecked = mCreateEventRsvpSelectView.isChecked();
        mCreateEventRsvpSelectView.setChecked(!rsvpChecked);
        mMaeventSelectedRsvpView.setVisibility(View.VISIBLE);
        updateCreateEventButtonVisibility();
    }

    @Override
    public void onStart() {
        super.onStart();
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
        //TODO Refactor
        MaeventParams params = new MaeventParams();
        params.name = mCreateEventNameView.getText().toString();
        params.place = mCreateEventPlaceSelectedTextView.getText().toString();
        params.addressStreet = "Witolda Budryka 2";
        params.addressPostCode = "30-072 Krakow";
        params.beginTime = mCreateEventPlaceSelectedTextView.getText().toString();
        params.endTime = "22:00 29.02";
        Maevent event = ThisUser.createEvent(params);
        Prompt.displayShort("TODO Create event. " + event.getParams().name +
                ". RSVP: " + (mCreateEventRsvpSelectView.isChecked() ? "yes" : "no"), parent);

        mLoadingDialog.hide();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(parent, data);
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
            mLoadingDialog.hide();
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
        if (mCreateEventPlaceSelectedView.getVisibility() == View.VISIBLE
                && mCreateEventNameSelectedView.getVisibility() == View.VISIBLE
                && mMaeventSelectTimeView.getVisibility() == View.VISIBLE) {
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
