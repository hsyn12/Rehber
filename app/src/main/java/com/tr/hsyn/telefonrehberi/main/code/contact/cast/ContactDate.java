package com.tr.hsyn.telefonrehberi.main.code.contact.cast;


import com.tr.hsyn.telefonrehberi.main.code.contact.act.Dates;


public interface ContactDate {

    Dates getDates();

    void setDates(Dates dates);

    default long getSavedDate() {

        return getDates() != null ? getDates().getSavedDate() : 0L;
    }

    default long getDeletedDate() {

        return getDates().getDeletedDate();
    }

}
