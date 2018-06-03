package com.devmarcul.maevent.apis.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.devmarcul.maevent.apis.MaeventApiModel;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserProfile;
import com.devmarcul.maevent.main.common.EventDetailsViewHolder;
import com.devmarcul.maevent.utils.TimeUtils;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class MaeventModel extends MaeventApiModel implements Parcelable {

    public static final String TIME_FORMAT = "yyyy-MM-ddTHH:mm:ss";

    @SerializedName(value = "Id", alternate = {"id"})
    public int Id;
    @SerializedName(value = "Name", alternate = {"name"})
    public String Name;
    @SerializedName(value = "Host", alternate = {"host"})
    public UserModel Host;
    @SerializedName(value = "Place", alternate = {"place"})
    public String Place;
    @SerializedName(value = "AddressStreet", alternate = {"addressStreet"})
    public String AddressStreet;
    @SerializedName(value = "AddressPostCode", alternate = {"addressPostCode"})
    public String AddressPostCode;
    @SerializedName(value = "BeginTime", alternate = {"beginTime"})
    public String BeginTime;
    @SerializedName(value = "EndTime", alternate = {"endTime"})
    public String EndTime;
    @SerializedName(value = "Rsvp", alternate = {"rsvp", "RSVP"})
    public boolean Rsvp;
    @SerializedName(value = "AttendeesIds", alternate = {"attendeesIds"})
    public String AttendeesIds;
    @SerializedName(value = "InviteesNumber", alternate = {"inviteesNumber"})
    public int InviteesNumber;

    public MaeventModel(Maevent event) {
        if (event == null) {
            return;
        }
        if (event.getHost() == null) {
            throw new IllegalArgumentException("Event host must not be null");
        }

        final MaeventParams params = event.getParams();

        final String begin =
                TimeUtils.convertTimeStringToOtherFormat(params.beginTime,
                        MaeventParams.TIME_FORMAT, TIME_FORMAT);
        final String end =
                TimeUtils.convertTimeStringToOtherFormat(params.endTime,
                        MaeventParams.TIME_FORMAT, TIME_FORMAT);

        Id = event.getId();
        Name = params.name;
        Host = new UserModel(event.getHost());
        Place = params.place;
        AddressStreet = params.addressStreet;
        AddressPostCode = params.addressPostCode;
        BeginTime = begin;
        EndTime = end;
        Rsvp = params.rsvp;
        AttendeesIds = event.getAttendeesIds();
        InviteesNumber = event.getInviteesNumber();
    }

    protected MaeventModel(Parcel in) {
        Id = in.readInt();
        Name = in.readString();
        Host = new UserModel(in);
        Place = in.readString();
        AddressStreet = in.readString();
        AddressPostCode = in.readString();
        BeginTime = in.readString();
        EndTime = in.readString();
        Rsvp = in.readByte() != 0;
        AttendeesIds = in.readString();
        InviteesNumber = in.readInt();
    }

    public static final Creator<MaeventModel> CREATOR = new Creator<MaeventModel>() {
        @Override
        public MaeventModel createFromParcel(Parcel in) {
            return new MaeventModel(in);
        }

        @Override
        public MaeventModel[] newArray(int size) {
            return new MaeventModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Name);
        Host.writeToParcel(dest, flags);
        dest.writeString(Place);
        dest.writeString(AddressStreet);
        dest.writeString(AddressPostCode);
        dest.writeString(BeginTime);
        dest.writeString(EndTime);
        dest.writeByte((byte) (Rsvp ? 1 : 0));
        dest.writeString(AttendeesIds);
        dest.writeInt(InviteesNumber);
    }

    public Maevent toMaevent() {
        Maevent event = new Maevent();
        MaeventParams params = new MaeventParams();

        User host = new User();
        if (this.Host != null) {
            host.setProfile(this.Host.toUserProfile());
        }

        final String begin =
                TimeUtils.convertTimeStringToOtherFormat(this.BeginTime,
                        TIME_FORMAT, MaeventParams.TIME_FORMAT);
        final String end =
                TimeUtils.convertTimeStringToOtherFormat(this.EndTime,
                        TIME_FORMAT, MaeventParams.TIME_FORMAT);

        params.name = this.Name;
        params.place = this.Place;
        params.addressStreet = this.AddressStreet;
        params.addressPostCode = this.AddressPostCode;
        params.beginTime = begin;
        params.endTime = end;
        params.rsvp = this.Rsvp;

        event.setId(this.Id);
        event.setParams(params);
        event.setHost(host);
        event.setInviteesNumber(this.InviteesNumber);
        event.setAttendeesIds(this.AttendeesIds);
        return event;
    }
}
