package com.devmarcul.maevent.invite;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserProfile;
import com.devmarcul.maevent.data.Users;

public class InviteesAdapter
        extends RecyclerView.Adapter<InviteesAdapter.InviteeViewHolder> {

    private Users mInvitees;

    @NonNull
    @Override
    public InviteeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutId = R.layout.invite_user_profile;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, viewGroup, false);
        return new InviteeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InviteeViewHolder holder, int position) {
        User invitee = mInvitees.get(position);
        adaptContent(holder, invitee);
    }

    @Override
    public int getItemCount() {
        if (mInvitees == null) {
            return 0;
        }
        return mInvitees.size();
    }

    public int getInviteesNumber() {
        if (mInvitees == null) {
            return 0;
        }
        return mInvitees.size();
    }

    public void setInviteesData(Users invitees) {
        mInvitees = invitees;
        notifyDataSetChanged();
    }

    public Users updateInviteesData(int pos, User invitee) {
        mInvitees.set(pos, invitee);
        notifyItemInserted(pos);
        return mInvitees;
    }

    public Users removeInvitation(int pos) {
        mInvitees.remove(pos);
        notifyItemRemoved(pos);
        return mInvitees;
    }

    public void clearInvitations() {
        mInvitees.clear();
        notifyDataSetChanged();
    }

    public class InviteeViewHolder
            extends RecyclerView.ViewHolder {

        final View view;
        final ImageView photoView;
        final TextView firstNameView;
        final TextView lastNameView;
        final TextView locationView;

        public InviteeViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            photoView = view.findViewById(R.id.iv_person_photo);
            firstNameView = view.findViewById(R.id.tv_person_first_name);
            lastNameView = view.findViewById(R.id.tv_person_last_name);
            locationView = view.findViewById(R.id.tv_person_location);

            view.setBackgroundResource(R.drawable.touch_selector);
        }
    }

    private void adaptContent(InviteeViewHolder holder, User invitee) {
        if (invitee == null) {
            return;
        }

        UserProfile profile = invitee.getProfile();
        String firstName = profile.firstName;
        String lastName = profile.lastName;
        String location = profile.location;

        //TODO Add image
        holder.photoView.setImageResource(R.mipmap.maevent_logo);
        holder.firstNameView.setText(firstName);
        holder.lastNameView.setText(lastName);
        holder.locationView.setText(location);
    }
}
