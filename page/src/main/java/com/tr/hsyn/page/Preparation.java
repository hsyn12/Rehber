package com.tr.hsyn.page;


/**
 * Hazırlık
 */
public interface Preparation {

    /**
     * @return Hazır ise {@code true}
     */
    boolean isReady();

    /**
     * Hazır olduğunda haber al.
     *
     * @param onReady Hazır olunca çağrılacak
     */
    void setOnReady(Runnable onReady);
}
