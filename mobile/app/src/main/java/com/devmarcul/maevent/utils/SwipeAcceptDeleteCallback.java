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

public class SwipeAcceptDeleteCallback extends ItemTouchHelper.Callback {

    private SwipedCallback swipedCallback;

    private Drawable deleteIcon;
    private Drawable acceptIcon;
    private int deleteIntrinsicWidth;
    private int deleteIntrinsicHeight;
    private int acceptIntrinsicWidth;
    private int acceptIntrinsicHeight;
    private ColorDrawable background;
    private int colorDelete;
    private int colorAccept;
    private Paint clearPaint;

    public interface SwipedCallback {
        void onAccept(RecyclerView.ViewHolder viewHolder);
        void onDelete(RecyclerView.ViewHolder viewHolder);
    }

    public SwipeAcceptDeleteCallback(Context context, SwipedCallback callback) {
        swipedCallback = callback;

        deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete);
        deleteIntrinsicWidth = deleteIcon.getIntrinsicWidth();
        deleteIntrinsicHeight = deleteIcon.getIntrinsicHeight();

        acceptIcon = ContextCompat.getDrawable(context, R.drawable.ic_done);
        acceptIntrinsicWidth = deleteIcon.getIntrinsicWidth();
        acceptIntrinsicHeight = deleteIcon.getIntrinsicHeight();

        background = new ColorDrawable();
        colorDelete = ContextCompat.getColor(context, R.color.colorWarning);
        colorAccept = ContextCompat.getColor(context, R.color.colorOk);
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
            swipedCallback.onAccept(viewHolder);
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
            final int acceptIconMargin = (itemHeight - acceptIntrinsicHeight) / 2;
            final int acceptIconLeft = itemLeft + acceptIconMargin;
            final int acceptIconTop = itemTop + acceptIconMargin;
            final int acceptIconRight = itemLeft + acceptIconMargin + acceptIntrinsicWidth;
            final int acceptIconBottom = acceptIconTop + acceptIntrinsicHeight;

            background.setColor(colorAccept);
            background.setBounds(itemLeft, itemTop, (int) (itemLeft + dX), itemBottom);
            background.draw(c);

            acceptIcon.setBounds(acceptIconLeft, acceptIconTop, acceptIconRight, acceptIconBottom);
            acceptIcon.draw(c);
        }
        else {
            final int deleteIconMargin = (itemHeight - deleteIntrinsicHeight) / 2;
            final int deleteIconLeft = itemRight - deleteIconMargin - deleteIntrinsicWidth;
            final int deleteIconTop = itemTop + deleteIconMargin;
            final int deleteIconRight = itemRight - deleteIconMargin;
            final int deleteIconBottom = deleteIconTop + deleteIntrinsicHeight;

            background.setColor(colorDelete);
            background.setBounds((int) (itemRight + dX), itemTop, itemRight, itemBottom);
            background.draw(c);

            deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
            deleteIcon.draw(c);
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void clearCanvas(Canvas c, float left, float top, float right, float bottom) {
        c.drawRect(left, top, right, bottom, clearPaint);
    }
}
