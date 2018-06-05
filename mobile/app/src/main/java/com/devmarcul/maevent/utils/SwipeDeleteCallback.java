package com.devmarcul.maevent.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.devmarcul.maevent.R;

public class SwipeDeleteCallback extends ItemTouchHelper.Callback {

    private SwipedCallback swipedCallback;

    private Drawable leftIcon;
    private Drawable rightIcon;
    private int deleteIntrinsicWidth;
    private int deleteIntrinsicHeight;
    private ColorDrawable background;
    private int colorDelete;
    private Paint clearPaint;

    public interface SwipedCallback {
        void onDelete(RecyclerView.ViewHolder viewHolder);
    }

    public SwipeDeleteCallback(Context context, SwipedCallback callback) {
        swipedCallback = callback;

        int iconRes = R.drawable.ic_delete;

        leftIcon = ContextCompat.getDrawable(context, iconRes);
        rightIcon = ContextCompat.getDrawable(context, iconRes);
        deleteIntrinsicWidth = leftIcon.getIntrinsicWidth();
        deleteIntrinsicHeight = leftIcon.getIntrinsicHeight();

        background = new ColorDrawable();
        colorDelete = ContextCompat.getColor(context, R.color.colorWarning);
        clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.START) {
            swipedCallback.onDelete(viewHolder);
        }
        else if (direction == ItemTouchHelper.END) {
            swipedCallback.onDelete(viewHolder);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {

        View itemView = viewHolder.itemView;
        boolean isCancelled = dX == 0f && !isCurrentlyActive;

        final int itemLeft = itemView.getLeft();
        final int itemTop = itemView.getTop();
        final int itemRight = itemView.getRight();
        final int itemBottom = itemView.getBottom();
        final int itemHeight = itemView.getHeight();

        if (isCancelled) {
            clearCanvas(c, itemRight + dX, itemTop, itemRight, itemBottom);
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }

        boolean directionRight = dX > 0;
        if (directionRight) {
            final int leftIconMargin = (itemHeight - deleteIntrinsicHeight) / 2;
            final int leftIconLeft = itemLeft + leftIconMargin;
            final int leftIconTop = itemTop + leftIconMargin;
            final int leftIconRight = itemLeft + leftIconMargin + deleteIntrinsicHeight;
            final int leftIconBottom = leftIconTop + deleteIntrinsicHeight;

            background.setColor(colorDelete);
            background.setBounds(itemLeft, itemTop, (int) (itemLeft + dX), itemBottom);
            background.draw(c);

            leftIcon.setBounds(leftIconLeft, leftIconTop, leftIconRight, leftIconBottom);
            leftIcon.draw(c);
        }
        else {
            final int rightIconMargin = (itemHeight - deleteIntrinsicHeight) / 2;
            final int rightIconLeft = itemRight - rightIconMargin - deleteIntrinsicWidth;
            final int rightIconTop = itemTop + rightIconMargin;
            final int rightIconRight = itemRight - rightIconMargin;
            final int rightIconBottom = rightIconTop + deleteIntrinsicHeight;

            background.setColor(colorDelete);
            background.setBounds((int) (itemRight + dX), itemTop, itemRight, itemBottom);
            background.draw(c);

            rightIcon.setBounds(rightIconLeft, rightIconTop, rightIconRight, rightIconBottom);
            rightIcon.draw(c);
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void clearCanvas(Canvas c, float left, float top, float right, float bottom) {
        c.drawRect(left, top, right, bottom, clearPaint);
    }
}
