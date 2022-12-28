package com.tr.hsyn.telefonrehberi.main.activity.contact.detail;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.execution.Work;
import com.tr.hsyn.message.Show;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.Dates;
import com.tr.hsyn.telefonrehberi.main.code.contact.cast.Contact;
import com.tr.hsyn.telefonrehberi.main.code.story.contact.ContactDataStory;
import com.tr.hsyn.telefonrehberi.main.dev.Over;
import com.tr.hsyn.telefonrehberi.main.dev.Story;
import com.tr.hsyn.textdrawable.TextDrawable;
import com.tr.hsyn.xlog.xlog;


public abstract class ContactDetailsHeadWay extends ContactDetailsView {

    protected Story<Contact> story;
    protected Contact        contact;
    protected boolean        isDatesSet;

    @Override
    protected void onCreate() {

        super.onCreate();

        story   = Over.Contacts.getContactManager();
        contact = Over.Contacts.getSelectedContact();

        if (contact == null) {

            Show.tost(this, getString(R.string.contact_details_contact_not_found));
            onBackPressed();
            return;
        }

        Work.on(() -> getDates(story))
                .onResult(this::setDates)
                .execute();

        setImage();
        setNumbers();
    }

    /**
     * Verilen numara için mesajları aç.
     *
     * @param number Numara
     */
    protected void openMessages(String number) {

        try {

            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("sms:" + Uri.encode(number))));
            Toast.makeText(getApplicationContext(), number, Toast.LENGTH_SHORT).show();
        }
        catch (ActivityNotFoundException e) {

            Show.tost(this, "İşlemi gerçekleştirecek bir uygulama yok");
        }
    }

    protected Dates getDates(Story<Contact> story) {

        return ((ContactDataStory) story).getDatabase().getDates(contact.getContactId());
    }

    private void setImage() {

        var font  = ResourcesCompat.getFont(this, com.tr.hsyn.resfont.R.font.nunito_regular);
        int color = Colors.getPrimaryColor();

        collapsingToolbarLayout.setContentScrimColor(color);
        collapsingToolbarLayout.setCollapsedTitleTypeface(font);
        collapsingToolbarLayout.setExpandedTitleTypeface(font);
        collapsingToolbarLayout.setTitle(contact.getName());

        if (contact.getBigPic() != null) {

            image.setImageURI(Uri.parse(contact.getBigPic()));
        }
        else {

            final int fontSize = 256;

            var drawable = TextDrawable.builder()
                    .beginConfig()
                    .useFont(font)
                    .fontSize(fontSize)
                    .endConfig()
                    .buildRect(Stringx.toUpper(Stringx.getFirstChar(contact.getName())), color);

            image.setImageDrawable(drawable);
        }
    }

    private void setNumbers() {

        if (contact.getNumbers() != null && !contact.getNumbers().isEmpty()) {

            for (var number : contact.getNumbers()) {

                View view = getLayoutInflater().inflate(R.layout.number_item, numbersLayout, false);

                TextView numberView = view.findViewById(R.id.number);

                numberView.setText(number);

                numbersLayout.addView(view);

                ImageView iconMessage = view.findViewById(R.id.contact_details_icon_message);
                ImageView iconCall    = view.findViewById(R.id.contact_details_icon_call);

                final float factor = .2f;
                var         color  = Colors.lighter(Colors.getPrimaryColor(), factor);

                Colors.setTintDrawable(iconMessage.getDrawable(), color);
                Colors.setTintDrawable(iconCall.getDrawable(), color);

                iconMessage.setTag(number);
                iconCall.setTag(number);
                //view.setTag(number);

                iconMessage.setOnClickListener(this::onClickMessage);
                iconCall.setOnClickListener(this::onClickCall);
                //view.setOnClickListener(this::onClickCall);
            }
        }
        else {

            View noNumber = getLayoutInflater().inflate(R.layout.contact_detail_no_number, numbersLayout, false);

            numbersLayout.addView(noNumber);

            noNumber.setBackgroundResource(Colors.getRipple());
            noNumber.setOnClickListener(this::onClickNoNumber);
        }
    }

    private void setDates(Dates dates, Throwable error) {

        if (error == null) {

            contact.setDates(dates);
            isDatesSet = true;
        }
        else xlog.e(error);
    }

    private void onClickNoNumber(@NonNull View view) {

        xlog.d("Telefon numarası ekle");
    }

    private void onClickMessage(@NonNull View view) {

        xlog.d("Message to : %s", view.getTag());
    }

    private void onClickCall(@NonNull View view) {

        xlog.d("Call to : %s", view.getTag());
    }

    private void onClickEmail(@NonNull View view) {

        String email = (String) view.getTag();

        xlog.d("Email clicked : %s", email);

        sendEmailIntent(email);

    }

    private void sendEmailIntent(String email) {

        var    uri = Uri.parse("mailto:" + email).buildUpon().build();
        Intent i   = new Intent(Intent.ACTION_SENDTO, uri);

        try {

            startActivity(Intent.createChooser(i, ""));
        }
        catch (Exception e) {

            Show.tost(this, "Bu işlemi gerçekleştirecek yüklü bir uygulama bulunamadı");
        }

    }
}
