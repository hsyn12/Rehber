package com.tr.hsyn.telefonrehberi.main.code.contact.cast;

import java.util.List;


public interface ContactGroup {

    /**
     * @return Kişinin dahil olduğu grup id listesi
     */
    List<String> getGroups();

    void setGroups(List<String> groups);
}
