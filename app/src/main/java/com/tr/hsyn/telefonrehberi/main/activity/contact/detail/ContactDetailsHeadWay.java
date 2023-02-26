package com.tr.hsyn.telefonrehberi.main.activity.contact.detail;


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
import com.tr.hsyn.telefonrehberi.code.Phone;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.ContactKey;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.Contacts;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.handler.MimeTypeHandlers;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.handler.NumberHandler;
import com.tr.hsyn.telefonrehberi.main.dev.Over;
import com.tr.hsyn.textdrawable.TextDrawable;
import com.tr.hsyn.treadedwork.ThreadedWork;
import com.tr.hsyn.xlog.xlog;

import java.util.List;


public abstract class ContactDetailsHeadWay extends ContactDetailsView implements ThreadedWork {
	
	protected Contact contact;
	
	@Override
	protected void onCreate() {
		
		super.onCreate();
		
		contact = Over.Contacts.getSelectedContact();
		
		if (contact == null) {
			
			Show.tost(this, getString(R.string.contact_details_contact_not_found));
			onBackPressed();
		}
		else checkDetails();
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
	
	/**
	 * Kişi detayları alındıktan sonra çağrılır.
	 * Bu çağrıdan önce kişi ile ilgili yapılacak işlemler boşa çıkar.
	 */
	@CallSuper
	protected void prepare() {
		
		setImage();
		setNumbers();
	}
	
	private void checkDetails() {
		
		boolean detailsApplied = contact.getBool(ContactKey.DETAILS_APPLIED);
		
		if (!detailsApplied) {
			
			workOnBackground(() -> {
				
				MimeTypeHandlers handlers = new MimeTypeHandlers(new NumberHandler());
				
				Contacts.setContactDetails(getContentResolver(), contact, handlers);
				
				if (contact.getPic() != null) {
					
					contact.setData(ContactKey.BIG_PIC, Contacts.getBigPic(getContentResolver(), contact.getContactId()));
				}
				
				workOnMain(this::prepare);
				
			});
		}
		else prepare();
	}
	
	private void setImage() {
		
		var font  = ResourcesCompat.getFont(this, com.tr.hsyn.resfont.R.font.nunito_regular);
		int color = Colors.getPrimaryColor();
		
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
		
		List<String> numbers = contact.getData(ContactKey.NUMBERS);
		
		if (numbers != null && !numbers.isEmpty()) {
			
			for (var number : numbers) {
				
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
	
	private void onClickNoNumber(@NonNull View view) {
		
		xlog.d("Telefon numarası ekle");
		
		onClickEdit(view);
	}
	
	private void onClickMessage(@NonNull View view) {
		
		xlog.d("Message to : %s", view.getTag());
		openMessages((String) view.getTag());
	}
	
	private void onClickCall(@NonNull View view) {
		
		xlog.d("Call to : %s", view.getTag());
		
		Phone.makeCall(this, (String) view.getTag());
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
