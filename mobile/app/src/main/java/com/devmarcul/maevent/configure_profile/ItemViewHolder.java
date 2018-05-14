package com.devmarcul.maevent.configure_profile;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.devmarcul.maevent.R;

public class ItemViewHolder {
    private View view;
    private View child;
    private ImageView icon;
    private TextView label;
    private ImageView arrow;
    private boolean collapsed;

    public ItemViewHolder(View view, View child, String title, int imageResource, boolean collapsed) {
        this.view = view;
        this.child = child;
        icon = view.findViewById(R.id.iv_configure_profile_item_icon);
        label = view.findViewById(R.id.tv_configure_profile_item_label);
        arrow = view.findViewById(R.id.iv_configure_profile_item_arrow);
        icon.setImageResource(imageResource);
        label.setText(title);
        this.collapsed = collapsed;
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
        RotateAnimation animation =
                new RotateAnimation(360, 180, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(300);
        animation.setFillAfter(true);
        arrow.startAnimation(animation);
    }

    private void animateCollapse() {
        RotateAnimation animation =
                new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.05f);
        animation.setDuration(300);
        animation.setFillAfter(true);
        arrow.startAnimation(animation);
    }
}
