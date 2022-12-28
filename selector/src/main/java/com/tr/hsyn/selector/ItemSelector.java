package com.tr.hsyn.selector;


import org.jetbrains.annotations.NotNull;


/**
 * <h2>ItemSelector</h2>
 * <p>
 * Rastgele yada istatistiksel olarak eleman seçmeyi sağlar.
 * Seçilen her eleman seçilme sayısını bir arttırır.
 * Bu şekilde en çok yada en az seçilen eleman talep edilebilir.
 */
public interface ItemSelector<T> {

    /**
     * @return Rastgele bir eleman
     */
    @NotNull
    T selectAny();

    /**
     * @return En az seçilmiş elemanlardan ilki
     */
    @NotNull
    T selectTheLeast();

    /**
     * @return En çok seçilmiş elemanlardan ilki
     */
    @NotNull
    T selectTheMost();

    /**
     * @return En az seçilmiş elemanlardan herhangi biri
     */
    @NotNull
    T selectFromTheLeast();

    /**
     * @return En çok seçilmiş elemanlardan herhangi biri
     */
    @NotNull
    T selectFromTheMost();
}
