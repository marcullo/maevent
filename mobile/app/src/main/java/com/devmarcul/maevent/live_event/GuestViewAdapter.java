package com.devmarcul.maevent.live_event;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devmarcul.maevent.R;

public class GuestViewAdapter extends RecyclerView.Adapter<GuestViewHolder> {

    //TODO Replace with Content Provider / etc.
    private Guests mGuests;
    private Context context;

    public GuestViewAdapter(Context context, Guests guests) {
        this.context = context;
        this.mGuests = guests;
    }

    @NonNull
    @Override
    public GuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_guest, null);
        GuestViewHolder gvh = new GuestViewHolder(layoutView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull GuestViewHolder holder, int position) {
        //TODO Replace with real profile after making it non-static
        holder.mGuestFirstName.setText("Andrew");
        holder.mGuestLastName.setText("Block");
        holder.mGuestLocationView.setText("Warsaw");
        holder.mGuestHeadlineView.setText("A person who never made a mistake never tried anything new.");
    }

    @Override
    public int getItemCount() {
        if (mGuests == null) {
            return 0;
        }
        return mGuests.size();
    }
}
