package com.devmarcul.maevent.apis.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.devmarcul.maevent.data.Invitation;
import com.devmarcul.maevent.data.Invitations;

import java.util.ArrayList;
import java.util.List;

public class InvitationsModel implements Parcelable {

    private List<InvitationModel> content;

    public InvitationsModel(Invitations invitations) {
        content = new ArrayList<>();
        for (Invitation invitation :
                invitations) {
            InvitationModel model = new InvitationModel(invitation);
            content.add(model);
        }
    }

    public InvitationsModel(List<InvitationModel> content) {
        this.content = content;
    }

    protected InvitationsModel(Parcel in) {
        content = new ArrayList<>();
        in.readTypedList(content, InvitationModel.CREATOR);
    }

    public static final Creator<InvitationsModel> CREATOR = new Creator<InvitationsModel>() {
        @Override
        public InvitationsModel createFromParcel(Parcel in) {
            return new InvitationsModel(in);
        }

        @Override
        public InvitationsModel[] newArray(int size) {
            return new InvitationsModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(content);
    }

    public Invitations toInvitations() {
        Invitations invitations = new Invitations();
        for (InvitationModel model :
                content) {
            invitations.add(model.toInvitation());
        }
        return invitations;
    }
}
