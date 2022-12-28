package com.tr.hsyn.selector;


/**
 * Long Selector
 */
public interface LongSelector {

    /**
     * {@code [0-} {@link Long#MAX_VALUE}{@code )} aralığında rastgele bir sayı seçer.
     *
     * @return rastgele bir sayı
     */
    static long selectLong() {

        return (long) (Math.random() * Long.MAX_VALUE);
    }

    /**
     * {@code 0} ile verilen sayı aralığında rastgele bir sayı seçer.
     *
     * @param end bitiş sınırı (dahil değil)
     * @return rastgele bir sayı
     */
    static long selectLong(long end) {

        return selectLong() % end;
    }

    /**
     * Verilen iki sayı aralığında bir sayı seçer. sırayla
     *
     * @param start başlangıç
     * @param end   bitiş
     * @return seçilen sayı
     */
    static long selectLong(long start, long end) {

        return selectLong(end - start) + start;
    }
}
