package com.devmarcul.maevent.agenda;

import android.view.View;

public class ItemViewHolder {
    private View view;
    private View child;
    private boolean collapsed;

    public ItemViewHolder(View view, View child) {
        this.view = view;
        this.child = child;
        this.collapsed = false;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void toggle() {
        if (isCollapsed()) {
            expand();
        }
        else {
            collapse();
        }
    }

    public void expand() {
        animateExpand();
        child.setVisibility(View.VISIBLE);
        collapsed = false;
    }

    public void collapse() {
        animateCollapse();
        child.setVisibility(View.GONE);
        collapsed = true;
    }

    private void animateExpand() {
        //TODO Incoming events animation expand
    }

    private void animateCollapse() {
        //TODO Incoming events animation collapse
    }
}
