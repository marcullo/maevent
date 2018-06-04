package com.devmarcul.maevent.apis.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.devmarcul.maevent.data.Invitation;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserDummy;
import com.google.gson.annotations.SerializedName;

public class InvitationModel implements Parcelable {

    @SerializedName(value = "Id", alternate = "id")
    public String Id;
    @SerializedName(value = "Inviter", alternate = "inviter")
    public UserModel Inviter;
    @SerializedName(value = "Invitee", alternate = "invitee")
    public UserModel Invitee;
    @SerializedName(value = "Event", alternate = "event")
    public MaeventModel Event;
    @SerializedName(value = "Message", alternate = "message")
    public String Message;

    public InvitationModel(Invitation invitation) {
        if (invitation == null) {
            return;
        }
        if (invitation.getHost() == null) {
            throw new IllegalArgumentException("Event host must not be null");
        }

        Id = String.valueOf(invitation.getId());
        Inviter = new UserModel(invitation.getInviter());
        Invitee = new UserModel(invitation.getInvitee());
    }

    protected InvitationModel(Parcel in) {
        Id = in.readString();
        Inviter = in.readParcelable(UserModel.class.getClassLoader());
        Invitee = in.readParcelable(UserModel.class.getClassLoader());
        Event = in.readParcelable(MaeventModel.class.getClassLoader());
        Message = in.readString();
    }

    public static final Creator<InvitationModel> CREATOR = new Creator<InvitationModel>() {
        @Override
        public InvitationModel createFromParcel(Parcel in) {
            return new InvitationModel(in);
        }

        @Override
        public InvitationModel[] newArray(int size) {
            return new InvitationModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeParcelable(Inviter, flags);
        dest.writeParcelable(Invitee, flags);
        dest.writeParcelable(Event, flags);
        dest.writeString(Message);
    }

    public Invitation toInvitation() {
        Maevent event = Event.toMaevent();
        MaeventParams params = event.getParams();
        User host = event.getHost();
        User inviter = new User();
        inviter.setProfile(Inviter.toUserProfile());
        User invitee = new User();
        invitee.setProfile(Invitee.toUserProfile());
        String message = Message;
        String attendeesIds = event.getAttendeesIds();

        return new Invitation(
                params, attendeesIds, host, inviter, invitee, message);
    }
}

