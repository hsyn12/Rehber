package com.tr.hsyn.telefonrehberi.main.code.contact.cast;

import org.jetbrains.annotations.Nullable;


/**
 * Base information of contact
 */
public interface ContactIdentity {

    /**
     * @return The contact id.
     */
    long getContactId();

    /**
     * @return The name of the contact
     */
    @Nullable
    String getName();
}
