package com.devmarcul.maevent.apis.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserProfile;
import com.devmarcul.maevent.data.Users;

import java.util.ArrayList;
import java.util.List;

public class UsersModel implements Parcelable {

    private List<UserModel> content;

    public UsersModel(List<UserModel> content) {
        this.content = content;
    }

    public UsersModel(Users users) {
        content = new ArrayList<>();
        for (User user :
                users) {
            UserModel model = new UserModel(user);
            content.add(model);
        }
    }

    protected UsersModel(Parcel in) {
        content = new ArrayList<>();
        in.readTypedList(content, UserModel.CREATOR);
    }

    public static final Creator<UsersModel> CREATOR = new Creator<UsersModel>() {
        @Override
        public UsersModel createFromParcel(Parcel in) {
            return new UsersModel(in);
        }

        @Override
        public UsersModel[] newArray(int size) {
            return new UsersModel[size];
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

    public Users toUsers() {
        Users users = new Users();
        for (UserModel model :
                content) {
            UserProfile profile = model.toUserProfile();
            User user = new User();
            user.setProfile(profile);
            users.add(user);
        }
        return users;
    }
}
