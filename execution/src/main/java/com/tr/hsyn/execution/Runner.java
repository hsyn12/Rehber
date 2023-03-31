package com.tr.hsyn.execution;


import org.jetbrains.annotations.NotNull;


/**
 * Çalıştırılabilir bir işi tanımlar.<br>
 * Ancak işin kendisi olmak yerine, işe sahip olmayı gerektirir.
 * Bu yüzden {@link Runnable} arayüzünü genişletmektedir.
 * {@code Runnable} arayüzü her ne kadar işin kendisi olsa da
 * {@code Runner} arayüzü eklediği {@link #getRunnable()} metodu ile
 * işin kendisi değil sahibi olduğuna işaret eder.
 * Bu anlamda {@code Runner} bir iş değil, işin çalıştırıcısıdır diyebiliriz.
 */
public interface Runner extends Runnable {

    /**
     * @return çalıştırılacak kod
     */
    @NotNull
    Runnable getRunnable();
}