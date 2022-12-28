package com.tr.hsyn.telefonrehberi.main.activity.contact.detail;


import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.telefonrehberi.main.code.comment.contact.ContactCommentator;
import com.tr.hsyn.telefonrehberi.main.dev.Over;
import com.tr.hsyn.xlog.xlog;

import java.util.Locale;


public class ContactDetailsAbout extends ContactDetailsMenu {


    //- Burada kişi hakkında bazı kayda değer bilgileri
    //- kompozisyon halinde sunmak istiyoruz.

    @Override
    protected void onCreate() {

        super.onCreate();

        //! Arama kayıtları alınmamış ise yorum yapamayız
        if (Over.CallLog.Calls.getCalls() != null) {

            Runny.run(this::onComments, false);
        }
        else {

            xlog.d("Arama kayıtları henüz alınmış olmadığı için bu kişi hakkında bir yorum yapamıyorum");
        }

    }

    protected String getLocaleLanguage() {

        return Locale.getDefault().getLanguage();
    }

    protected boolean isTurkishLocale() {

        return getLocaleLanguage().equals("tr");
    }

    private void onComments() {

        xlog.d("Locale : %s", getLocaleLanguage());

        var commentator = ContactCommentator.createCommentator(this);

        var comments = commentator.commentate(contact);
        xlog.d(comments);

    }

}
