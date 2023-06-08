package com.tr.hsyn.telefonrehberi.main.contact.activity.detail;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.message.Show;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.dev.Phone;
import com.tr.hsyn.telefonrehberi.main.contact.data.ContactKey;
import com.tr.hsyn.telefonrehberi.main.dev.Over;
import com.tr.hsyn.textdrawable.TextDrawable;
import com.tr.hsyn.treadedwork.ThreadedWork;
import com.tr.hsyn.xlog.xlog;

import java.util.List;


/**
 * This abstract class is a head way for contact details.
 * It prepares details for the selected contact by the user,
 * and then calls {@link #prepare()} method.
 * All subclasses of this class need to work after {@link #prepare()},
 * please attention to the order.
 */
public abstract class ContactDetailsHeadWay extends ContactDetailsView implements ThreadedWork {
	
	/** The selected contact */
	protected Contact      contact;
	protected List<String> phoneNumbers;
	
	@Override
	protected void onCreate() {
		
		super.onCreate();
		
		//! We have to have the selected contact
		
		contact = Over.Contacts.getSelectedContact();
		
		if (contact == null) {
			
			Show.tost(this, getString(R.string.contact_details_contact_not_found));
			onBackPressed();
		}
		else prepare();
	}
	
	/**
	 * Writes base information about the contact,
	 * phone numbers and image, for example.
	 * Subclasses of this class have to override this method,
	 * <u><strong>call super first</strong></u>,
	 * and start their all work from there.
	 */
	@CallSuper
	protected void prepare() {
		
		setImage();
		setNumbers();
	}
	
	/**
	 * Sets the image of the contact into the image view
	 */
	private void setImage() {
		
		android.graphics.Typeface font  = ResourcesCompat.getFont(this, com.tr.hsyn.resfont.R.font.nunito_regular);
		int                       color = Colors.getPrimaryColor();
		
		collapsingToolbarLayout.setContentScrimColor(color);
		collapsingToolbarLayout.setCollapsedTitleTypeface(font);
		collapsingToolbarLayout.setExpandedTitleTypeface(font);
		collapsingToolbarLayout.setTitle(contact.getName());
		
		String bPic = contact.getData(ContactKey.BIG_PIC);
		
		if (bPic != null) {
			
			image.setImageURI(Uri.parse(bPic));
		}
		else {
			
			final int fontSize = 256;
			
			TextDrawable drawable = TextDrawable.builder().beginConfig().useFont(font).fontSize(fontSize).endConfig().buildRect(Stringx.toUpper(Stringx.getFirstChar(contact.getName())), color);
			
			image.setImageDrawable(drawable);
		}
	}
	
	/**
	 * Sets the numbers of the contact into the text view
	 */
	private void setNumbers() {
		
		phoneNumbers = ContactKey.getNumbers(contact);
		
		if (phoneNumbers != null && !phoneNumbers.isEmpty()) {
			
			for (String number : phoneNumbers) {
				
				View view = getLayoutInflater().inflate(R.layout.number_item, numbersLayout, false);
				
				TextView numberView = view.findViewById(R.id.number);
				
				numberView.setText(number);
				
				numbersLayout.addView(view);
				
				ImageView iconMessage = view.findViewById(R.id.contact_details_icon_message);
				ImageView iconCall    = view.findViewById(R.id.contact_details_icon_call);
				
				final float factor = .2f;
				int         color  = Colors.lighter(Colors.getPrimaryColor(), factor);
				
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
	
	/**
	 * Listener for the 'sent message' view click event.
	 *
	 * @param view sent message view
	 */
	private void onClickMessage(@NonNull View view) {
		
		xlog.d("Message to : %s", view.getTag());
		openMessages((String) view.getTag());
	}
	
	/**
	 * Listener for the call view click event.
	 *
	 * @param view call view
	 */
	private void onClickCall(@NonNull View view) {
		
		xlog.d("Call to : %s", view.getTag());
		
		Phone.makeCall(this, (String) view.getTag());
	}
	
	/**
	 * Listener for the no number view click event.
	 *
	 * @param view no number view
	 */
	private void onClickNoNumber(@NonNull View view) {
		
		xlog.d("Telefon numarası ekle");
		
		onClickEdit(view);
	}
	
	/**
	 * Opens the messages for sending a message to the contact
	 *
	 * @param number the phone number to send a message
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
	
	/**
	 * Listener for the email view click event.
	 *
	 * @param view email view
	 */
	private void onClickEmail(@NonNull View view) {
		
		String email = (String) view.getTag();
		
		xlog.d("Email clicked : %s", email);
		
		sendEmailIntent(email);
	}
	
	/**
	 * Sends an email to the given email address
	 *
	 * @param email the email address
	 */
	private void sendEmailIntent(String email) {
		
		Uri    uri = Uri.parse("mailto:" + email).buildUpon().build();
		Intent i   = new Intent(Intent.ACTION_SENDTO, uri);
		
		try {
			
			startActivity(Intent.createChooser(i, ""));
		}
		catch (Exception e) {
			
			Show.tost(this, "Bu işlemi gerçekleştirecek yüklü bir uygulama bulunamadı");
		}
		
	}
}
