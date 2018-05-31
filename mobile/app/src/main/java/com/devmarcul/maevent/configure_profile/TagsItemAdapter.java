package com.devmarcul.maevent.configure_profile;

import android.content.Context;
import android.view.View;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.business_logic.ThisUser;
import com.devmarcul.maevent.common.ContentAdapter;
import com.devmarcul.maevent.common.TagsViewAdapter;
import com.devmarcul.maevent.common.TagsViewHolder;
import com.devmarcul.maevent.data.Tags;

public class TagsItemAdapter implements
        ContentAdapter<Tags, TagsViewHolder>,
        View.OnClickListener {

    private ItemViewHolder mLabelViewHolder;
    private TagsViewAdapter mTagsViewAdapter;

    public TagsItemAdapter(Context context, View labelView, View contentView) {
        mTagsViewAdapter = new TagsViewAdapter(contentView, R.id.et_tags);

        final String tagsPickerLabel = context.getString(R.string.configure_profile_tags_picker_label);
        final int tagsPickerResource = R.mipmap.butterfly_tie;
        mLabelViewHolder = new ItemViewHolder(labelView, contentView, tagsPickerLabel, tagsPickerResource, true);

        mTagsViewAdapter.adaptEditable(true);
    }

    @Override
    public void adaptContent(Tags tags) {
        mTagsViewAdapter.adaptContent(ThisUser.getProfile().tags);
    }

    @Override
    public void bindListeners() {
        mLabelViewHolder.view.setOnClickListener(this);
    }

    @Override
    public void unbindListeners() {
        mLabelViewHolder.view.setOnClickListener(null);
    }

    @Override
    public TagsViewHolder getViewHolder() {
        return mTagsViewAdapter.getViewHolder();
    }

    @Override
    public void onClick(View v) {
        mLabelViewHolder.toggle();
    }

    public void expandContent() {
        mLabelViewHolder.expand();
    }
}
