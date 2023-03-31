package com.tr.hsyn.telefonrehberi.main.code.call.act;


import androidx.annotation.NonNull;

import com.tr.hsyn.htext.base.HTextView;
import com.tr.hsyn.telefonrehberi.main.code.page.Header;


public final class MainHeader implements Header {

    private final HTextView title;
    private final HTextView subTitle;

    public MainHeader(@NonNull HTextView title, @NonNull HTextView subTitle) {

        this.title    = title;
        this.subTitle = subTitle;
        title.setText("");
        subTitle.setText("");
    }

    @Override
    public CharSequence getTitle() {

        return title.getText();
    }

    @Override
    public void setTitle(CharSequence title) {

        this.title.animateText(title != null ? title : "");
    }

    @Override
    public CharSequence getSubTitle() {

        return subTitle.getText();
    }

    @Override
    public void setSubTitle(CharSequence subTitle) {

        this.subTitle.animateText(subTitle != null ? subTitle : "");
    }
}
