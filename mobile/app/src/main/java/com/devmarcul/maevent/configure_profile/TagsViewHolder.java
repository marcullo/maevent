package com.devmarcul.maevent.configure_profile;

import android.content.Context;
import android.view.View;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.data.ThisUser;
import com.devmarcul.maevent.utils.Prompt;

import me.originqiu.library.EditTag;

public class TagsViewHolder {
    private View view;
    private EditTag editTagView;

    public TagsViewHolder(final Context context, View itemView) {
        view = itemView;

        editTagView = view.findViewById(R.id.et_configure_profile_tags);
        editTagView.setTagList(ThisUser.getProfile().tags);
        editTagView.setTagAddCallBack(new EditTag.TagAddCallback() {
            @Override
            public boolean onTagAdd(String tagValue) {
                Prompt.displayShort("TODO: Implement tags limitation", context);
                return true;
            }
        });
    }
}
