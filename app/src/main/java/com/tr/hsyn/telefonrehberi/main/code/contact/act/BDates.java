package com.tr.hsyn.telefonrehberi.main.code.contact.act;


import androidx.annotation.NonNull;

import java.util.Objects;


class BDates implements Dates {

    private long savedDate;
    private long updatedDate;
    private long deletedDate;
    private long lastLookDate;

    public BDates(long savedDate, long updatedDate, long deletedDate, long lastLookDate) {

        this.savedDate    = savedDate;
        this.updatedDate  = updatedDate;
        this.deletedDate  = deletedDate;
        this.lastLookDate = lastLookDate;
    }

    @Override
    public long getSavedDate() {

        return savedDate;
    }

    @Override
    public Dates setSavedDate(long date) {

        this.savedDate = date;
        return this;
    }

    @Override
    public long getUpdatedDate() {

        return updatedDate;
    }

    @Override
    public void setUpdatedDate(long date) {

        updatedDate = date;
    }

    @Override
    public long getDeletedDate() {

        return deletedDate;
    }

    @Override
    public void setDeletedDate(long date) {

        deletedDate = date;
    }

    @Override
    public long getLastLookDate() {

        return lastLookDate;
    }

    @Override
    public void setLastLookDate(long date) {

        lastLookDate = date;
    }

    @NonNull @Override
    public String toString() {

        return "Dates{" +
               "savedDate=" + savedDate +
               ", updatedDate=" + updatedDate +
               ", deletedDate=" + deletedDate +
               ", lastLookDate=" + lastLookDate +
               '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BDates bDates = (BDates) o;
        return savedDate == bDates.savedDate && updatedDate == bDates.updatedDate && deletedDate == bDates.deletedDate && lastLookDate == bDates.lastLookDate;
    }

    @Override
    public int hashCode() {

        return Objects.hash(savedDate, updatedDate, deletedDate, lastLookDate);
    }
}
