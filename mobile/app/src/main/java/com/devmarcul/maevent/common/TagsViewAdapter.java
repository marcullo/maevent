package com.devmarcul.maevent.common;

import android.view.View;

import java.util.List;

public class TagsViewAdapter implements
        ContentAdapter<List<String>, TagsViewHolder> {

    private TagsViewHolder mViewHolder;

    public TagsViewAdapter(View viewHolderView, int resourceId) {
        mViewHolder = new TagsViewHolder(viewHolderView, resourceId);
    }

    @Override
    public void adaptContent(List<String> tags) {
        if (tags == null) {
            return;
        }

        List<String> oldTagsList = mViewHolder.mEditTagView.getTagList();
        if (oldTagsList != null) {
            int oldTagsSize = oldTagsList.size();
            for (int i = 0; i < oldTagsSize; i++) {
                mViewHolder.mEditTagView.removeTag(oldTagsList.get(0));
            }
        }
        mViewHolder.mEditTagView.setTagList(tags);
    }

    public void adaptEditable(boolean editable) {
        mViewHolder.mEditTagView.setEditable(editable);
    }

    @Override
    public void bindOnClickListeners() {
    }

    @Override
    public void unbindOnClickListeners() {
    }

    @Override
    public TagsViewHolder getViewHolder() {
        return mViewHolder;
    }
}
