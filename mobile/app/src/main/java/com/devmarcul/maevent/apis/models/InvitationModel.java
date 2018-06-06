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

    @SerializedName(value = "id", alternate = "Id")
    public int Id;
    @SerializedName(value = "inviter", alternate = "Inviter")
    public UserModel Inviter;
    @SerializedName(value = "invitee", alternate = "Invitee")
    public UserModel Invitee;
    @SerializedName(value = "event", alternate = "Event")
    public MaeventModel Event;
    @SerializedName(value = "message", alternate = "Message")
    public String Message;

    public InvitationModel(Invitation invitation) {
        if (invitation == null) {
            return;
        }
        if (invitation.getHost() == null) {
            throw new IllegalArgumentException("Event host must not be null");
        }

        Id = invitation.getId();
        Inviter = new UserModel(invitation.getInviter());
        Invitee = new UserModel(invitation.getInvitee());

        Maevent event = new Maevent();
        event.setParams(invitation.getParams());
        event.setId(invitation.getEventId());
        event.setHost(invitation.getHost());
        Event = new MaeventModel(event);
        Event.Id = invitation.getEventId();
        Message = invitation.getMessage();
    }

    protected InvitationModel(Parcel in) {
        Id = in.readInt();
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
        dest.writeInt(Id);
        dest.writeParcelable(Inviter, flags);
        dest.writeParcelable(Invitee, flags);
        dest.writeParcelable(Event, flags);
        dest.writeString(Message);
    }

    public Invitation toInvitation() {
        Maevent event = Event.toMaevent();
        User inviter = new User();
        inviter.setProfile(Inviter.toUserProfile());
        User invitee = new User();
        invitee.setProfile(Invitee.toUserProfile());
        String message = Message;

        Invitation invitation = new Invitation(event, inviter, invitee, message);
        invitation.setId(Id);

        return invitation;
    }
}

