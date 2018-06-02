package com.devmarcul.maevent.main.create_event;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.common.ContentListener;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;
import com.devmarcul.maevent.main.common.EventDetailsViewHolder;
import com.devmarcul.maevent.utils.TimeUtils;
import com.devmarcul.maevent.utils.StringUtils;
import com.devmarcul.maevent.utils.dialog.DetailsDialog;
import com.google.android.gms.location.places.Place;

import java.util.Calendar;

public class CreateEventViewAdapter implements
        ContentListener,
        CreateEventDialogViewAdapter.Handler {

    private CreateEventViewHolder mViewHolder;
    private Handler mHandler;

    private DetailsDialog mDialog;
    private View mDialogView;
    private CreateEventDialogViewAdapter mDialogAdapter;

    public interface Handler
    {
        void onCreateEventButtonClicked(MaeventParams params);
        void onHideKeyboardRequested();
        void onPickPlaceRequested();
    }

    public CreateEventViewAdapter(Handler handler, View viewHolderView, Context dialogContext, View dialogView) {
        mHandler = handler;
        mViewHolder = new CreateEventViewHolder(viewHolderView);
        mDialogView = dialogView;
        mDialogAdapter = new CreateEventDialogViewAdapter(this, dialogView) {
            @Override
            public void adaptContentForSelectingTime() {
                CreateEventDialogViewHolder holder = mDialogAdapter.mViewHolder;
                holder.mLoadingIndicatorProgressBar.setVisibility(View.INVISIBLE);
                holder.mSelectTimeView.setVisibility(View.VISIBLE);
            }

            @Override
            public void adaptContentForPickingPlace() {
                CreateEventDialogViewHolder holder = mDialogAdapter.mViewHolder;
                holder.mLoadingIndicatorProgressBar.setVisibility(View.VISIBLE);
                holder.mSelectTimeView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void adaptContentForCreatingEvent() {
            }

            @Override
            public void displayError(String message) {
            }
        };

        DetailsDialog.Builder builder = new DetailsDialog.Builder(dialogContext, mDialogView);
        mDialog = builder.build(false);

        updateRsvpViews();
        updateCreateEventButtonVisibility();
    }

    @Override
    public void bindListeners() {
        mViewHolder.mSelectNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNameUnselected();
                updateCreateEventButtonVisibility();
            }
        });
        mViewHolder.mWriteNameView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode != KeyEvent.KEYCODE_ENTER) {
                    return false;
                }
                if (event.getAction() != KeyEvent.ACTION_UP) {
                    return false;
                }

                updateNameViews();
                updateCreateEventButtonVisibility();

                return false;
            }
        });
        mViewHolder.mSelectTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogAdapter.adaptContentForSelectingTime();
                mDialog.show();

                if (!isNameSelected()) {
                    mHandler.onHideKeyboardRequested();
                    updateNameViews();
                    updateCreateEventButtonVisibility();
                }
            }
        });
        mViewHolder.mSelectPlaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogAdapter.adaptContentForPickingPlace();
                mDialog.show();

                mHandler.onPickPlaceRequested();

                if (!isNameSelected()) {
                    mHandler.onHideKeyboardRequested();
                    updateNameViews();
                    updateCreateEventButtonVisibility();
                }
            }
        });
        mViewHolder.mSelectRsvpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewHolder.mCheckRsvpView.toggle();

                if (!isNameSelected()) {
                    mHandler.onHideKeyboardRequested();
                    updateNameViews();
                    updateCreateEventButtonVisibility();
                }
            }
        });
        mViewHolder.mCheckRsvpView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateRsvpViews();

                if (!isNameSelected()) {
                    mHandler.onHideKeyboardRequested();
                    updateNameViews();
                    updateCreateEventButtonVisibility();
                }
            }
        });
        mViewHolder.mCreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCreateEventButtonGone();
                setCreateEventLoadingVisible();
                MaeventParams params = getParamsBuffer();
                mHandler.onCreateEventButtonClicked(params);
            }
        });

        mDialogAdapter.bindListeners();
    }

    @Override
    public void unbindListeners() {
        mViewHolder.mSelectNameView.setOnClickListener(null);
        mViewHolder.mWriteNameView.setOnKeyListener(null);
        mViewHolder.mSelectTimeView.setOnClickListener(null);
        mViewHolder.mSelectPlaceView.setOnClickListener(null);
        mViewHolder.mSelectRsvpView.setOnClickListener(null);
        mViewHolder.mCheckRsvpView.setOnCheckedChangeListener(null);
        mDialogAdapter.unbindListeners();
    }

    @Override
    public void onEventTimeSelected(Calendar startDate, Calendar endDate) {
        if (startDate == null || endDate == null) {
            return;
        }

        Calendar start;
        Calendar end;

        if (startDate.getTimeInMillis() > endDate.getTimeInMillis()) {
            start = endDate;
            end = startDate;
        }
        else {
            start = startDate;
            end = endDate;
        }

        String startDateText = TimeUtils.getStringFromCalendar(start, CreateEventViewHolder.TIME_FORMAT);
        String endDateText = TimeUtils.getStringFromCalendar(end, CreateEventViewHolder.TIME_FORMAT);

        updateTimeViews(startDateText, endDateText);
        updateCreateEventButtonVisibility();
        mDialog.hide();
    }

    @Override
    public void onEventTimeCancelled() {
    }

    public void updatePlaceViews(Place place) {
        mDialog.hide();

        if (place == null) {
            return;
        }
        if (place.getName() == null || place.getAddress() == null) {
            mDialogAdapter.displayError("TODO ERROR INVALID PLACE");
            mDialog.show();
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(place.getName()).append(StringUtils.getNewLine()).append(place.getAddress());
        mViewHolder.mSelectedPlaceView.setText(sb.toString());
        setPlaceSelected();
        updateCreateEventButtonVisibility();
    }

    private void updateNameViews() {
        String newName = mViewHolder.mWriteNameView.getText().toString();
        mViewHolder.mSelectedNameView.setText(newName);

        if (Maevent.isNameValid(newName)) {
            setNameSelected();
        }
        else {
            setNameUnselected();
        }
    }

    private void updateTimeViews(String start, String end) {
        String newTime = TimeUtils.getTimeStringFromStringDuration(start, end, CreateEventViewHolder.TIME_FORMAT);
        mViewHolder.mSelectedTimeView.setText(newTime);
        setTimeSelected();
    }

    private void updateRsvpViews() {
        boolean rsvpChecked = mViewHolder.mCheckRsvpView.isChecked();
        Context context = mDialogView.getContext();
        String textRsvp = context.getString(R.string.text_rsvp);
        String textYes = context.getString(R.string.text_yes);
        String textNo = context.getString(R.string.text_no);
        StringBuilder builder = new StringBuilder();
        builder.append(textRsvp).append(": ").append(rsvpChecked ? textYes : textNo);
        mViewHolder.mSelectedRsvpView.setText(builder.toString());
        mViewHolder.mSelectedRsvpView.setVisibility(View.VISIBLE);
    }

    private boolean isNameSelected() {
        return mViewHolder.mNameValidIndicator.getVisibility() == View.VISIBLE;
    }

    private boolean isTimeSelected() {
        return mViewHolder.mTimeValidIndicator.getVisibility() == View.VISIBLE;
    }

    private boolean isPlaceSelected() {
        return mViewHolder.mPlaceValidIndicator.getVisibility() == View.VISIBLE;
    }

    public void updateCreateEventButtonVisibility() {
        if (!isNameSelected()
                || !isTimeSelected()
                || !isPlaceSelected()) {
            setCreateEventButtonGone();
            return;
        }

        setCreateEventLoadingGone();
        setCreateEventButtonVisible();
    }

    public void setCreateEventButtonVisible() {
        mViewHolder.mCreateEventButton.setVisibility(View.VISIBLE);
    }

    public void setCreateEventButtonGone() {
        mViewHolder.mCreateEventButton.setVisibility(View.GONE);
    }

    public void setCreateEventLoadingVisible() {
        mViewHolder.mCreateEventProgressBar.setVisibility(View.VISIBLE);
    }

    public void setCreateEventLoadingGone() {
        mViewHolder.mCreateEventProgressBar.setVisibility(View.GONE);
    }

    private void setNameSelected() {
        mViewHolder.mWriteNameView.setVisibility(View.GONE);
        mViewHolder.mNameValidIndicator.setVisibility(View.VISIBLE);
        mViewHolder.mNameValidLabel.setVisibility(View.VISIBLE);
        mViewHolder.mSelectedNameView.setVisibility(View.VISIBLE);
    }

    private void setNameUnselected() {
        mViewHolder.mWriteNameView.setVisibility(View.VISIBLE);
        mViewHolder.mNameValidIndicator.setVisibility(View.GONE);
        mViewHolder.mNameValidLabel.setVisibility(View.GONE);
    }

    private void setTimeSelected() {
        mViewHolder.mTimeValidLabel.setTextColor(getSelectedColor());
        mViewHolder.mTimeValidIndicator.setVisibility(View.VISIBLE);
        mViewHolder.mTimeValidLabel.setVisibility(View.VISIBLE);
        mViewHolder.mSelectedTimeView.setVisibility(View.VISIBLE);
    }

    private void setTimeUnselected() {
        mViewHolder.mTimeValidLabel.setTextColor(getUnselectedColor());
        mViewHolder.mTimeValidIndicator.setVisibility(View.GONE);
        mViewHolder.mTimeValidLabel.setVisibility(View.GONE);
    }

    private void setPlaceSelected() {
        mViewHolder.mPlaceValidLabel.setTextColor(getSelectedColor());
        mViewHolder.mPlaceValidIndicator.setVisibility(View.VISIBLE);
        mViewHolder.mPlaceValidLabel.setVisibility(View.VISIBLE);
        mViewHolder.mSelectedPlaceView.setVisibility(View.VISIBLE);
    }

    private void setPlaceUnselected() {
        mViewHolder.mPlaceValidLabel.setTextColor(getUnselectedColor());
        mViewHolder.mPlaceValidIndicator.setVisibility(View.GONE);
        mViewHolder.mPlaceValidLabel.setVisibility(View.GONE);
    }

    private int getSelectedColor() {
        Context context = mDialogView.getContext();
        int color = ContextCompat.getColor(context, R.color.colorCreateEventItemSelected);
        return color;
    }

    private int getUnselectedColor() {
        Context context = mDialogView.getContext();
        int color = ContextCompat.getColor(context, R.color.colorCreateEventItemUnselected);
        return color;
    }

    private MaeventParams getParamsBuffer() {
        String[] wholePlace = mViewHolder.mSelectedPlaceView.getText().toString().split("[,\n]");

        String rsvpStr = mViewHolder.mSelectedRsvpView.getText().toString().split(" ")[1];
        Context context = mDialogView.getContext();
        String textYes = context.getString(R.string.text_yes);


        //TODO REFACTOR SO AS TO DATA HAS YEAR
        String[] timeStr = TimeUtils.splitCalendarDuration(mViewHolder.mSelectedTimeView.getText().toString(),
                EventDetailsViewHolder.TIME_FORMAT, EventDetailsViewHolder.TIME_FORMAT);

        MaeventParams params = new MaeventParams();
        params.name = mViewHolder.mSelectedNameView.getText().toString();
        params.place = wholePlace[0];
        params.addressStreet = wholePlace[1];
        params.addressPostCode = wholePlace[2];
        params.beginTime = timeStr[0];
        params.endTime = timeStr[1];
        params.rsvp = textYes.equals(rsvpStr);

        return params;
    }
}
