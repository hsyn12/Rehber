package com.tr.hsyn.telefonrehberi.main.activity.call.showcalls;


import android.view.View;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.CallSuper;

import com.tr.hsyn.activity.ActivityView;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.fastscroller.FastScrollRecyclerView;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.vanimator.ViewAnimator;
import com.tr.hsyn.xlog.xlog;


public class ShowCallsActivityView extends ActivityView {

    protected FastScrollRecyclerView list;
    protected View                   empty;

    @Override
    protected int getLayoutId() {

        return R.layout.activity_show_calls;
    }

    @CallSuper
    @Override
    protected void onCreate() {

        list  = findView(R.id.list_show_calls);
        empty = findView(R.id.empty);

        int color = Colors.getPrimaryColor();

        xlog.d("Primary Color : %d", color);

        //list.getFastScrollBar().setPopupBackgroundColor(color);
        list.getFastScrollBar().setThumbActiveColor(color);
    }

    protected void checkEmpty() {

        if (list.getAdapter() != null) {

            showEmptyView(list.getAdapter().getItemCount() == 0);
        }
    }

    protected void showEmptyView(boolean isShow) {

        View v       = empty;
        var  visable = v.getVisibility() != View.GONE;

        if (isShow == visable) return;

        var anim =
                ViewAnimator.on(v)
                        .duration(400)
                        .interpolator(new AccelerateInterpolator());

        if (isShow) {

            anim.alpha(0.5f, 1)
                    .scale(0.7f, 1)
                    .onStart(() -> v.setVisibility(View.VISIBLE));
        }
        else {

            anim.alpha(0)
                    .scale(0.7f)
                    .onStop(() -> v.setVisibility(View.GONE));
        }

        anim.start();
    }

    @Override
    protected boolean hasToolbar() {

        return true;
    }

    @Override
    protected int getToolbarResourceId() {

        return R.id.toolbar_show_calls;
    }

    @Override
    protected Runnable getNavigationClickListener() {

        return this::onBackPressed;
    }

}