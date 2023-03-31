package com.tr.hsyn.telefonrehberi.main.dev.backup;


import java.util.List;


/**
 * Yedekleyici.<br>
 * Sadece nesne listelerini yedekler.
 *
 * @param <T> Yedeklenecek nesne türü
 */
public interface BackupDirectory<T> {

    /**
     * @return Backup
     */
    Backup<T> newBackup();

    /**
     * @param items yedeklenecek nesneler
     * @return Backup
     */
    Backup<T> newBackup(List<T> items);

    void override(String name, List<T> items);

    /**
     * Yedeği siler.
     *
     * @param name silinecek yedeğin ismi
     */
    void deleteBackup(String name);

    /**
     * @return tüm yedekler
     */
    List<Backup<T>> getBackups();

    /**
     * Yedekleri nesne olarak döndürür.
     *
     * @param name istenen yedeğin adı
     * @return nesneler
     */
    List<T> getItems(String name);
}
