package com.tr.hsyn.telefonrehberi.main.code.contact.cast;

import androidx.annotation.Nullable;


public interface ContactNote {

    /**
     * @return Kişi için kaydedilmiş not
     */
    @Nullable String getNote();

    void setNote(@Nullable String note);
}
