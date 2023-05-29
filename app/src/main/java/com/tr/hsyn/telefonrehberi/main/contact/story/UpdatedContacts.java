package com.tr.hsyn.telefonrehberi.main.contact.story;


import com.tr.hsyn.telefonrehberi.dev.registery.Register;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class UpdatedContacts {

    private final String   BOOK = "updated_contacts";
    private final Register book = Register.openFile(BOOK);

    /**
     * @return updated contacts ids
     */
    @NotNull
    public final List<String> getContactIds() {

        return book.getKeys();
    }

    @NotNull
    public final List<ContactUpdateInfo> getUpdatedContacts() {

        var keys = getContactIds();

        List<ContactUpdateInfo> list = new ArrayList<>(keys.size());

        for (var k : keys)
            list.add(getUpdatedContact(k));

        return list;
    }

    public ContactUpdateInfo getUpdatedContact(String id) {

        return book.read(id, ContactUpdateInfo.class);
    }

    public void add(ContactUpdateInfo contact) {

        book.write(String.valueOf(contact.getId()), contact);
    }

    public void delete(String id) {

        book.delete(id);
    }

}
