package com.tr.hsyn.telefonrehberi.code.android.ui.swipe;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.page.SwipeListener;


public class ContactSwipeCallBack extends ItemTouchHelper.SimpleCallback {

    private final Paint         paint   = new Paint();
    private final Bitmap        icon;
    private final SwipeListener swipeListener;
    private       int           bgColor = 0xF44336;

    public ContactSwipeCallBack(int swipeDirs, @NonNull final SwipeListener swipeListener, Bitmap icon) {

        super(0, swipeDirs);
        this.swipeListener = swipeListener;
        this.icon          = icon;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

        return makeMovementFlags(0, ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        if (swipeListener != null)
            swipeListener.onSwipe(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(
            @NonNull Canvas c,
            @NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder,
            float dX, float dY,
            int actionState,
            boolean isCurrentlyActive) {


        View itemView = viewHolder.itemView;

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            float height = (float) itemView.getBottom() - (float) itemView.getTop();
            float width  = height / 3;

            if (dX > 0) {

                paint.setColor(bgColor);
                RectF background = new RectF(/*(float)itemView.getLeft()*/0.0F, (float) itemView.getTop(), dX, (float) itemView.getBottom());
                c.drawRect(background, paint);

                RectF icon_dest = new RectF(
                        (float) itemView.getLeft() + width,
                        (float) itemView.getTop() + width - 16,
                        (float) itemView.getLeft() + 2 * width + 32,
                        (float) itemView.getBottom() - width + 16);

                c.drawBitmap(icon, null, icon_dest, paint);
            }

            itemView.setAlpha(1.0F - (dX / 900.0F));
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    public void setBgColor(int color) {

        bgColor = color;

    }
}
