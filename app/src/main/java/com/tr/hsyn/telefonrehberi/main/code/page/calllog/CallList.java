package com.tr.hsyn.telefonrehberi.main.code.page.calllog;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.page.SwipeListener;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.dev.ResourceUtil;
import com.tr.hsyn.telefonrehberi.dev.android.ui.swipe.SwipeCallBack;
import com.tr.hsyn.telefonrehberi.main.code.page.adapter.CallAdapter;

import org.jetbrains.annotations.NotNull;


public abstract class CallList extends CallLogMenu {

    protected CallAdapter   adapter;
    protected SwipeCallBack swipeCallBack;

    @Override
    protected CallAdapter getAdapter() {

        return adapter;
    }

    protected void checkEmpty() {

        if (adapter != null) {

            emptyView.setVisibility(adapter.getSize() > 0 ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void setSwipeListener(SwipeListener swipeListener) {

        this.swipeListener = swipeListener;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        swipeCallBack = new SwipeCallBack(ItemTouchHelper.LEFT, this, ResourceUtil.getBitmap(view.getContext(), R.drawable.delete_white));
        swipeCallBack.setBgColor(Colors.getPrimaryColor());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallBack);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onSwipe(int index) {

        adapter.notifyItemChanged(index);

        super.onSwipe(index);

    }

    @Override
    public void showTime(boolean showTime) {

        super.showTime(showTime);
    }
}
