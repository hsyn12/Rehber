package com.tr.hsyn.selector;


/**
 * Integer Selector
 */
public interface IntSelector {


    /**
     * {@code [0-} {@link Integer#MAX_VALUE}{@code )} aralığında rastgele bir sayı seçer.
     *
     * @return rastgele bir sayı
     */
    static int selectInt() {

        return (int) (Math.random() * Integer.MAX_VALUE);
    }

    /**
     * {@code 0} ile verilen sayı aralığında rastgele bir sayı seçer.
     *
     * @param end bitiş sınırı (dahil değil)
     * @return rastgele bir sayı
     */
    static int selectInt(int end) {

        return selectInt() % end;
    }

    /**
     * Verilen iki sayı aralığında bir sayı seçer.
     *
     * @param start başlangıç
     * @param end   bitiş
     * @return seçilen sayı
     */
    static int selectInt(int start, int end) {

        return selectInt(end - start) + start;
    }


}
