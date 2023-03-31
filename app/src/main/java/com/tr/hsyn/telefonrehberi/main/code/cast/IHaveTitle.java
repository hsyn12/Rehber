package com.tr.hsyn.telefonrehberi.main.code.cast;


import com.tr.hsyn.holder.Holder;


public interface IHaveTitle {

    Holder<CharSequence> titleHolder = Holder.newHolder("");

    default CharSequence getTitle() {

        return titleHolder.get();
    }
}
