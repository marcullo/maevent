package com.devmarcul.maevent.common;

import android.view.View;

import me.originqiu.library.EditTag;

public class TagsViewHolder {

    private View view;

    public EditTag mEditTagView;

    public TagsViewHolder(View view, int resourceId) {
        this.view = view;
        mEditTagView = view.findViewById(resourceId);
    }
}
