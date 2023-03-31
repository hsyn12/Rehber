package com.tr.hsyn.telefonrehberi.main.dev;


import java.util.List;


/**
 * Kayıt yöneticisi.
 *
 * @param <T> Yönettiği kaydın türü
 */
public interface Story<T> {

    /**
     * @return Tüm kayıtlar
     */
    List<T> load();

    /**
     * @return Veri tabanındaki tüm kayıtlar
     */
    List<T> loadFromDatabase();

    /**
     * @return Tüm sistem kayıtları
     */
    List<T> loadFromSystem();

    /**
     * Veri tabanına ekle.
     *
     * @param item Item
     * @return Başarılı ise {@code true}
     */
    boolean addIntoDatabase(T item);

    /**
     * Hem sistemden hem veri tabanından sil.
     *
     * @param item Item
     * @return Her iki yerden de silinirse {@code true}
     */
    boolean delete(T item);

    boolean deleteFromDatabase(T item);

    int deleteFromDatabase(List<? extends T> items);

    /**
     * Veri tabanında güncelle.
     *
     * @param item Item
     * @return Başarılı ise {@code true}
     */
    boolean updateFromDatabase(T item);

    int updateFromDatabase(List<? extends T> items);

    /**
     * Sistemden güncelle.
     *
     * @param item Item
     * @return Başarılı ise {@code true}
     */
    boolean updateFromSystem(T item);

    /**
     * Sisteme ekle.
     *
     * @param item Item
     * @return Başarılı ise {@code uri}, değilse {@code null}
     */
    boolean addIntoSystem(T item);

    /**
     * Veri tabanına ekle.
     *
     * @param items Items
     * @return Başarılı şekilde eklenen kayıt sayısı
     */
    int addIntoDatabase(List<? extends T> items);

    /**
     * Sisteme ekle.
     *
     * @param items Items
     * @return Başarılı şekilde eklenen kayıt sayısı
     */
    int addIntoSystem(List<? extends T> items);

    /**
     * Sistemden sil.
     *
     * @param items Items
     * @return Başarılı şekilde silinen kayıt sayısı
     */
    int deleteFromSystem(List<? extends T> items);

    boolean deleteFromSystem(T item);

    int delete(List<? extends T> items);


}
