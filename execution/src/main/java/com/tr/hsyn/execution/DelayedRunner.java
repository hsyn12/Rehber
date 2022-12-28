package com.tr.hsyn.execution;


import org.jetbrains.annotations.NotNull;


/**
 * Bir işin geçikmeli olarak çalıştırılmasına ve iptal edilebilmesine imkan sağlar.
 */
public interface DelayedRunner extends Runner {

    /**
     * Yeni bir çalıştırıcı oluşturur.
     *
     * @param runnable çalıştırılacak iş
     * @return DelayedRunner
     */
    @NotNull
    static DelayedRunner newRunner(@NotNull Runnable runnable) {

        return new RunTimer(runnable);
    }

    /**
     * İşi çalıştırır.
     *
     * @param delay gecikme süresi
     */
    void run(long delay);

    /**
     * İşi iptal eder
     */
    void cancel();
}
