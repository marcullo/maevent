package com.devmarcul.maevent.apis.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.devmarcul.maevent.apis.MaeventApiModel;
import com.devmarcul.maevent.data.Tags;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserProfile;
import com.google.gson.annotations.SerializedName;

public class UserModel extends MaeventApiModel implements Parcelable {

    @SerializedName(value = "Id", alternate = {"id"})
    public String Id;
    @SerializedName(value = "FirstName", alternate = {"firstName", "firstname"})
    public String FirstName;
    @SerializedName(value = "LastName", alternate = {"lastName", "lastname"})
    public String LastName;
    @SerializedName(value = "Title", alternate = {"title"})
    public String Title;
    @SerializedName(value = "Pose", alternate = {"pose"})
    public String Pose;
    @SerializedName(value = "Headline", alternate = {"headline"})
    public String Headline;
    @SerializedName(value = "Phone", alternate = {"phone"})
    public String Phone;
    @SerializedName(value = "Email", alternate = {"email"})
    public String Email;
    @SerializedName(value = "Linkedin", alternate = {"linkedin", "linkedIn"})
    public String Linkedin;
    @SerializedName(value = "Location", alternate = {"location"})
    public String Location;
    @SerializedName(value = "Tags", alternate = {"tags"})
    public String Tags;

    public UserModel(User user) {
        if (user == null) {
            return;
        }

        final UserProfile profile = user.getProfile();

        StringBuilder builder = new StringBuilder();

        String tags = "";
        if (profile.tags != null) {
            builder.setLength(0);
            for (String tag :
                    profile.tags) {
                builder.append(tag).append(";");
            }
            tags = builder.toString();
        }

        Id = String.valueOf(profile.id);
        FirstName = profile.firstName;
        LastName = profile.lastName;
        Title = profile.title;
        Pose = profile.pose;
        Headline = profile.headline;
        Phone = profile.phone;
        Email = profile.email;
        Linkedin = profile.linkedin;
        Location = profile.location;
        Tags = tags;
    }

    protected UserModel(Parcel in) {
        Id = in.readString();
        FirstName = in.readString();
        LastName = in.readString();
        Title = in.readString();
        Pose = in.readString();
        Headline = in.readString();
        Phone = in.readString();
        Email = in.readString();
        Linkedin = in.readString();
        Location = in.readString();
        Tags = in.readString();
    }

    public UserProfile toUserProfile() {
        Tags tags = null;
        if (this.Tags != null) {
            String tagsArray[] = this.Tags.split(";");
            tags = new Tags();
            for (String tag:
                    tagsArray) {
                if (tag != null && tag != ";") {
                    tags.add(tag);
                }
            }
        }

        UserProfile profile = new UserProfile();
        profile.id = Integer.valueOf(this.Id);
        profile.firstName = this.FirstName;
        profile.lastName = this.LastName;
        profile.title = this.Title;
        profile.pose = this.Pose;
        profile.headline = this.Headline;
        profile.phone = this.Phone;
        profile.email = this.Email;
        profile.linkedin = this.Linkedin;
        profile.location = this.Location;
        profile.tags = tags;

        return profile;
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(FirstName);
        dest.writeString(LastName);
        dest.writeString(Title);
        dest.writeString(Pose);
        dest.writeString(Headline);
        dest.writeString(Phone);
        dest.writeString(Email);
        dest.writeString(Linkedin);
        dest.writeString(Location);
        dest.writeString(Tags);
    }
}
