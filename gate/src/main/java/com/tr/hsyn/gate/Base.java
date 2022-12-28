package com.tr.hsyn.gate;


import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Yapı.
 * Bu yapı bir odayı temsil eder.
 * Bu oda tek kapısı olan, yani tek bir giriş-çıkışa sahip basit bir yer.
 * Bu sınıfın en önemli özelliği alt sınıflar için kapıyı manuel olarak açıp kapatma imkanı sağlaması.
 * Böylece alt sınıflar istedikleri özellikte bir kapı tanımlayabilirler.
 * Bu sınıf temel sınıf olarak tanımlandı ancak tam anlamıyla işe yarar ve
 * kullanışlıdır.
 *
 * @see #openTheGate()
 * @see #closeTheGate()
 */
public class Base implements Room {
	
    /**
     * Kapı
     */
    private final Gate _gate;

    public Base() {_gate = Gate.createGate();}

    public Base(@NotNull Gate gate) {_gate = gate;}

    @Override
    public boolean enter() {

        if (isGateOpen()) {

            closeTheGate();
            return true;
        }

        return false;
    }

    @Override
    public void exit() {

        openTheGate();
    }

    /**
     * Kapıyı aç
     */
    protected void openTheGate() {

        _gate.openTheGate();
    }

    /**
     * Kapıyı kapat
     */
    protected void closeTheGate() {

        _gate.closeTheGate();
    }

    /**
     * @return Kapı açıksa {@code true}, değilse {@code false}
     */
    public boolean isGateOpen() {

        return _gate.isGateOpen();
    }
}
