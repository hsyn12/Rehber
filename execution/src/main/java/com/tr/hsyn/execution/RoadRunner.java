package com.tr.hsyn.execution;


import org.jetbrains.annotations.NotNull;


/**
 * En temel iş çalıştırıcısı sınıfı.<br>
 * {@link #run()} metodu ile iş çalıştırma emri verilir.
 * İş çalışmaya başlamadan hemen önce {@link #setOnStart(Runnable)}
 * metodu ile verilen başlangıç görevi çağrılır, işin tamamlanmasından hemen sonra ise
 * {@link #setOnComplete(Runnable)} metodu ile verilen sonlanma görevi çağrılır.
 */
public class RoadRunner implements Runner {

    private final Runnable runnable;
    private Runnable onStart;
    private Runnable onComplete;

    public RoadRunner(@NotNull Runnable runnable) {

        this.runnable = () -> {

            onStart();
            runnable.run();
            onComplete();
        };
    }

    protected void onStart() {

        if (onStart != null) onStart.run();
    }

    protected void onComplete() {

        if (onComplete != null) onComplete.run();
    }

    public void setOnStart(Runnable onStart) {

        this.onStart = onStart;
    }

    public void setOnComplete(Runnable onComplete) {

        this.onComplete = onComplete;
    }

    @NotNull
    @Override
    public Runnable getRunnable() {

        return runnable;
    }

    @Override
    public void run() {

        runnable.run();
    }
}
