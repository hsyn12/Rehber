package com.tr.hsyn.objext;


/**
 * Bir nesne için üst katman oluşturur ve
 * nesneye erişim üst katmandan yapılır.
 * Farklı theread'ler üzerinde kullanıma elverişlidir.
 *
 * @param <T> Yönetilecek nesne türü
 */
public interface SafeObjex<T> extends Objex<T> {}
