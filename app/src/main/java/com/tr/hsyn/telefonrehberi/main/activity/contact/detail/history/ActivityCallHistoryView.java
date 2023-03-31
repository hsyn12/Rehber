package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.history;


import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.example.xtoolbar.Toolbarx;
import com.tr.hsyn.activity.ActivityView;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.fastscroller.FastScrollRecyclerView;
import com.tr.hsyn.telefonrehberi.R;


public class ActivityCallHistoryView extends ActivityView {

    protected FastScrollRecyclerView list;
    protected View                   emptyView;
    private   Toolbar                toolbar;

    @Override
    protected int getLayoutId() {

        return R.layout.activity_call_history;
    }

    @Override
    protected void onCreate() {

        list      = findView(R.id.list_call_history);
        emptyView = findView(R.id.empty);
        toolbar   = findView(R.id.toolbar_call_history);

        int color = Colors.getPrimaryColor();

        //xlog.d("Primary Color : %d", color);

        //list.getFastScrollBar().setPopupBackgroundColor(color);
        list.getFastScrollBar().setThumbActiveColor(color);

        Toolbarx.setToolbar(this, toolbar, this::onBackPressed);
    }

    protected void setTitle(String title) {

        toolbar.setTitle(title);
    }

    protected void setSubtitle(String subTitle) {

        toolbar.setSubtitle(String.valueOf(subTitle));
    }

}
