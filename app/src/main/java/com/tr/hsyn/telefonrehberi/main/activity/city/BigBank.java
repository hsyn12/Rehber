package com.tr.hsyn.telefonrehberi.main.activity.city;

import com.tr.hsyn.buildkeys.BuildKeys;
import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.telefonrehberi.main.code.contact.cast.Contact;
import com.tr.hsyn.telefonrehberi.main.code.database.call.CallDatabase;
import com.tr.hsyn.telefonrehberi.main.code.database.contact.ContactDatabase;
import com.tr.hsyn.telefonrehberi.main.code.story.call.CallStory;
import com.tr.hsyn.telefonrehberi.main.code.story.contact.ContactStory;
import com.tr.hsyn.telefonrehberi.main.dev.Loader;
import com.tr.hsyn.telefonrehberi.main.dev.Story;
import com.tr.hsyn.xbox.Blue;

import org.jetbrains.annotations.NotNull;


public abstract class BigBank extends NorthBridge {

    /**
     * Arama kayıtları yöneticisi
     */
    private Story<Call>    callStory;
    /**
     * Rehber yöneticisi
     */
    private Story<Contact> contactStory;

    /**
     * @return Rehber yöneticisi
     */
    protected final Story<Contact> getContactStory() {

        return contactStory;
    }

    /**
     * @return Arama kayıtları yöneticisi
     */
    protected final Story<Call> getCallStory() {

        return callStory;
    }

    @Override
    protected void onCreate() {

        super.onCreate();

        var contactDatabase = new ContactDatabase(this);
        var callDatabase    = new CallDatabase(this);
        contactStory = new ContactStory(this, contactDatabase);
        callStory    = new CallStory(callDatabase, getContentResolver());

        Blue.box(BuildKeys.CONTACT_STORY, contactStory);
        Blue.box(BuildKeys.CALL_STORY, callStory);

    }

    @NotNull
    protected final Loader<Call> getCallLogLoader() {

        return callStory::load;
    }

    @NotNull
    protected final Loader<Contact> getContactsLoader() {

        return contactStory::load;
    }
}
