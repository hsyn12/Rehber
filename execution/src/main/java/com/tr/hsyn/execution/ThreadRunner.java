package com.tr.hsyn.execution;


/**
 * {@link DelayedRunner} arayüzünü genişterek,
 * bir işin arka planda çalıştırılabilmesi için metotlar ekler.
 */
public interface ThreadRunner extends DelayedRunner {

    /**
     * Bu çağrı işin arka planda çalıştırılmasına sebep olur.
     *
     * @return ThreadRunner
     */
    ThreadRunner onBackground();

    /**
     * @param onBackground arka planda çalıştırılması için {@code true}, ana thread üzerinde çalıştırılması için {@code false}
     * @return ThreadRunner
     */
    ThreadRunner onBackground(boolean onBackground);
}
