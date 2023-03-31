package com.tr.hsyn.page;


import com.tr.hsyn.selection.ItemIndexListener;


public interface HaveCallAction {

    default void setOnCallAction(ItemIndexListener onCallAction) {}
}
