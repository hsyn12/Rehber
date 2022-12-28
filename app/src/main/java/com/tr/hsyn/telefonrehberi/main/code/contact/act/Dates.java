package com.tr.hsyn.telefonrehberi.main.code.contact.act;


import org.jetbrains.annotations.NotNull;


public interface Dates {

    @NotNull
    static Dates newDates() {

        return new BDates(0, 0, 0, 0);
    }

    @NotNull
    static Dates newDates(long savedDate) {

        return new BDates(savedDate, 0, 0, 0);
    }

    @NotNull
    static Dates newDates(long savedDate, long updatedDate, long deletedDate, long lastLookDate) {

        return new BDates(savedDate, updatedDate, deletedDate, lastLookDate);
    }

    long getSavedDate();

    Dates setSavedDate(long date);

    long getUpdatedDate();

    void setUpdatedDate(long date);

    long getDeletedDate();

    void setDeletedDate(long date);

    long getLastLookDate();

    void setLastLookDate(long date);
}
