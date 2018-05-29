package com.devmarcul.maevent.apis.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.devmarcul.maevent.apis.MaeventApiModel;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;

public class MaeventModel extends MaeventApiModel implements Parcelable {
    public String Name;
    public String Place;
    public String AddressStreet;
    public String AddressPostCode;
    public String BeginTime;
    public String EndTime;
    public boolean Rsvp;
    public int HostId;

    public MaeventModel(Maevent event) {
        if (event == null) {
            return;
        }
        MaeventParams params = event.getParams();
        Name = params.name;
        Place = params.place;
        AddressStreet = params.addressStreet;
        AddressPostCode = params.addressPostCode;
        BeginTime = params.beginTime;
        EndTime = params.endTime;
        Rsvp = params.rsvp;
        HostId = event.getHostId();
    }

    protected MaeventModel(Parcel in) {
        Name = in.readString();
        Place = in.readString();
        AddressStreet = in.readString();
        AddressPostCode = in.readString();
        BeginTime = in.readString();
        EndTime = in.readString();
        Rsvp = in.readByte() != 0;
        HostId = in.readInt();
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
        dest.writeString(Name);
        dest.writeString(Place);
        dest.writeString(AddressStreet);
        dest.writeString(AddressPostCode);
        dest.writeString(BeginTime);
        dest.writeString(EndTime);
        dest.writeByte((byte) (Rsvp ? 1 : 0));
        dest.writeInt(HostId);
    }
}
