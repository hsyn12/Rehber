package com.tr.hsyn.selector;


public interface DoubleSelector {

    /**
     * {@code [0-} {@link Double#MAX_VALUE}{@code )} aralığında rastgele bir sayı seçer.
     *
     * @return rastgele bir sayı
     */
    static double selectDouble() {

        return Math.random() * Double.MAX_VALUE;
    }

    /**
     * {@code 0} ile verilen sayı aralığında rastgele bir sayı seçer.
     *
     * @param end bitiş sınırı (dahil değil)
     * @return rastgele bir sayı
     */
    static double selectDouble(double end) {

        return selectDouble() % end;
    }

    /**
     * Verilen iki sayı aralığında bir sayı seçer. sırayla
     *
     * @param start başlangıç
     * @param end   bitiş
     * @return seçilen sayı
     */
    static double selectDouble(double start, double end) {

        return selectDouble(end - start) + start;
    }
}
