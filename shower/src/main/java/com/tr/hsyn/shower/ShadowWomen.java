package com.tr.hsyn.shower;


import org.jetbrains.annotations.NotNull;


public class ShadowWomen implements ShowWomen {

    private final ShowTimer[] showTimer;
	
    public ShadowWomen(@NotNull ShowTimer... showTimer) {

        this.showTimer = showTimer;
    }

    @Override
    public final void show(int index) {

        for (int i = 0; i < showTimer.length; i++) showTimer[i].showTime(i == index);
    }


}
