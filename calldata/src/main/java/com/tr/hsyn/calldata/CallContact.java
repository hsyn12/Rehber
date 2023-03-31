package com.tr.hsyn.calldata;


/**
 * Numarası olan kişi
 */
public interface CallContact {

    /**
     * @return kişinin ismi
     */
    String getName();

    void setName(String name);

    /**
     * @return kişinin numarası
     */
    String getNumber();
}
