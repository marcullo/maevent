package com.devmarcul.maevent.live_event;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.utils.tools.Prompt;

public class GuestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private View view;

    public ImageView mGuestPhotoView;
    public TextView mGuestFirstName;
    public TextView mGuestLastName;
    public TextView mGuestLocationView;
    public TextView mGuestHeadlineView;

    public GuestViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        itemView.setOnClickListener(this);
        mGuestPhotoView = view.findViewById(R.id.iv_guest_photo);
        mGuestFirstName = view.findViewById(R.id.tv_guest_first_name);
        mGuestLastName = view.findViewById(R.id.tv_guest_last_name);
        mGuestLocationView = view.findViewById(R.id.tv_guest_location);
        mGuestHeadlineView = view.findViewById(R.id.tv_guest_headline);
    }

    @Override
    public void onClick(View v) {
        Prompt.displayTodo(view.getContext());
    }
}
