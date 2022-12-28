package com.tr.hsyn.telefonrehberi.main.code.story.contact;


import com.tr.hsyn.telefonrehberi.main.code.contact.cast.Contact;
import com.tr.hsyn.telefonrehberi.main.code.database.contact.DBContact;
import com.tr.hsyn.telefonrehberi.main.dev.Story;


/**
 * Kişilere özel veri tabanı için işlemler tanımlar.
 */
public interface ContactDataStory extends Story<Contact> {

    DBContact getDatabase();
}
