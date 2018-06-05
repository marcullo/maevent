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

public class SearchingUsersAdapter
        extends RecyclerView.Adapter<SearchingUsersAdapter.SearchingViewHolder> {

    private Users mFoundUsers;
    private final OnClickHandler mClickHandler;

    public interface OnClickHandler {
        void onClick(User user);
    }

    public SearchingUsersAdapter(OnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public SearchingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.invite_user_profile_details;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, parent, false);
        return new SearchingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchingUsersAdapter.SearchingViewHolder holder, int position) {
        User foundUser = mFoundUsers.get(position);
        adaptContent(holder, foundUser);
    }

    @Override
    public int getItemCount() {
        if (mFoundUsers == null) {
            return 0;
        }
        return mFoundUsers.size();
    }

    public void setFoundUsersData(Users foundUsers) {
        mFoundUsers = foundUsers;
        notifyDataSetChanged();
    }

    public void clearFoundUsersData() {
        mFoundUsers.clear();
        notifyDataSetChanged();
    }

    public class SearchingViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final View view;
        final ImageView photoView;
        final TextView firstNameView;
        final TextView lastNameView;
        final TextView locationView;

        public SearchingViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            photoView = view.findViewById(R.id.iv_person_photo);
            firstNameView = view.findViewById(R.id.tv_person_first_name);
            lastNameView = view.findViewById(R.id.tv_person_last_name);
            locationView = view.findViewById(R.id.tv_person_location);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            User foundUser = mFoundUsers.get(adapterPosition);
            mClickHandler.onClick(foundUser);
        }
    }

    private void adaptContent(SearchingViewHolder holder, User foundUser) {
        if (foundUser == null) {
            return;
        }

        UserProfile profile = foundUser.getProfile();
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
