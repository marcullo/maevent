package com.devmarcul.maevent.apis.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.Maevents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MaeventsModel implements Parcelable {

    private List<MaeventModel> content;

    public MaeventsModel(List<MaeventModel> content) {
        this.content = content;
    }

    public MaeventsModel(Maevents events) {
        content = new ArrayList<>();
        for (Maevent event :
                events) {
            MaeventModel model = new MaeventModel(event);
            content.add(model);
        }
    }
    
    protected MaeventsModel(Parcel in) {
        content = new ArrayList<>();
        in.readTypedList(content, MaeventModel.CREATOR);
    }

    public static final Creator<MaeventsModel> CREATOR = new Creator<MaeventsModel>() {
        @Override
        public MaeventsModel createFromParcel(Parcel in) {
            return new MaeventsModel(in);
        }

        @Override
        public MaeventsModel[] newArray(int size) {
            return new MaeventsModel[size];
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

    public Maevents toMaevents() {
        Maevents events = new Maevents();
        for (MaeventModel model :
                content) {
            events.add(model.toMaevent());
        }
        return events;
    }
}
