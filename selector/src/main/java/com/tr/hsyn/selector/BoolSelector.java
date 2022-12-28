package com.tr.hsyn.selector;


public interface BoolSelector {

    static boolean selectBool() {

        return IntSelector.selectInt(2) == 0;
    }
}
