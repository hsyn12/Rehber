package com.tr.hsyn.telefonrehberi.dev.android.ui.swipe;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.page.SwipeListener;


@SuppressWarnings("WeakerAccess")
public class SwipeCallBack extends ItemTouchHelper.SimpleCallback implements ISwipeCallBack {

    protected final Paint         paint   = new Paint();
    private final   SwipeListener swipeListener;
    private final   Bitmap        icon;
    private final   int           direction;
    protected       int           bgColor = 0xF44336;

    public SwipeCallBack(int swipeDirs, @NonNull final SwipeListener swipeListener, Bitmap icon) {

        super(0, swipeDirs);
        this.direction     = swipeDirs;
        this.swipeListener = swipeListener;
        this.icon          = icon;
    }

    @Override
    public void setBgColor(int bgColor) {

        this.bgColor = bgColor;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {

        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

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


            if (dX < 0) {

                paint.setColor(bgColor);
                RectF background = new RectF((float) itemView.getRight(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                //RectF background = new RectF((float) itemView.getRight(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                c.drawRect(background, paint);

                RectF icon_dest = new RectF(
                        (float) itemView.getRight() - width * 2,
                        (float) itemView.getTop() + width,
                        (float) itemView.getRight() - width,
                        (float) itemView.getBottom() - width);

                c.drawBitmap(icon, null, icon_dest, paint);
            }

            itemView.setAlpha(1.0F - (-dX / 900.0F));
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

        return makeMovementFlags(0, direction);
    }

}
