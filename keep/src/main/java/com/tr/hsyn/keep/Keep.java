package com.tr.hsyn.keep;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Derleme sırasında isim değişikliğine uğramak istemeyen nesneleri bildirir.
 */
@Keep
@Retention(RetentionPolicy.RUNTIME)
public @interface Keep {}