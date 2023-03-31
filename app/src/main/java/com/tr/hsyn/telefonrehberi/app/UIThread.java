package com.tr.hsyn.telefonrehberi.app;

import com.tr.hsyn.executors.MainExecutor;

import java.util.concurrent.Executor;


public interface UIThread {

    default Executor getUIThreadExecutor() {

        return MainExecutor.HOLDER.getMainExecutor();
    }

}
