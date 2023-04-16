package com.tr.hsyn.telefonrehberi.main.activity.contact.detail;


import android.annotation.SuppressLint;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.gate.AutoGate;
import com.tr.hsyn.gate.Gate;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.code.call.CallOver;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.ContactCommentator;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.ContactKey;
import com.tr.hsyn.telefonrehberi.main.dev.Over;
import com.tr.hsyn.vanimator.ViewAnimator;
import com.tr.hsyn.xlog.xlog;


public class ContactDetailsAbout extends ContactDetailsMenu {
	
	//- Burada kişi hakkında bazı kayda değer bilgileri
	//- kompozisyon halinde sunmak istiyoruz.
	
	private final Gate      gateAbout = AutoGate.newGate(2000L);
	private       boolean   isOpenAboutView;
	private       ViewGroup view_about_content;
	private       TextView  text_about;
	private       boolean   reComment = true;
	
	/**
	 * Activity'nin başlangıç kodları
	 */
	@Override
	protected void onHistoryUpdate() {
		
		super.onHistoryUpdate();
		
		//! Kişi hakkında bilgi sağlayabilmek için
		//! arama kayıtları gerekli.
		//! Ayrıca kişiye ait bir telefon numarası.
		//! Bunlar yoksa 'Hakkında' bölümü olmayacak
		if (contact.exist(ContactKey.NUMBERS) && Over.CallLog.exist()) {
			
			Runny.run(this::setStatistics, false);
			
			reComment = true;
			setupCommentViews();
		}
	}
	
	private void setStatistics() {
		
		var callGroups = CallOver.groupByNumber();
		
	}
	
	private void comment() {
		
		if (reComment) {
			
			reComment = false;
			
			Runny.run(() -> {
				
				ContactCommentator commentator = ContactCommentator.createCommentator(this);
				
				var comment = commentator.commentate(contact);
				
				Runny.run(() -> onCommentReady(comment), true);
			}, false);
		}
		else {
			
			animateAboutView();
		}
	}
	
	private void onCommentReady(CharSequence comment) {
		
		text_about.setText(comment);
		animateAboutView();
	}
	
	@SuppressLint("InflateParams")
	private void setupCommentViews() {
		
		view_about_content = (ViewGroup) getLayoutInflater().inflate(R.layout.contact_about_content, mainContainer, false);
		text_about         = findView(view_about_content, R.id.text_about);
		text_about.setMovementMethod(new LinkMovementMethod());
		
		int       ripple            = Colors.getRipple();
		View      view_about_header = view_about_content.findViewById(R.id.about_header);
		ImageView image_about_icon  = findView(view_about_content, R.id.about_icon);
		
		Colors.setTintDrawable(image_about_icon.getDrawable(), Colors.lighter(Colors.getPrimaryColor(), 0.2f));
		view_about_header.setBackgroundResource(ripple);
		
		addDetailView(view_about_content);
		view_about_header.setOnClickListener(this::onClickHeader);
	}
	
	private void onClickHeader(View view) {
		
		if (history == null) {
			
			xlog.i("Kişinin geçmişi henüz alınmadı");
			return;
		}
		
		if (gateAbout.enter()) {
			comment();
		}
	}
	
	private void animateAboutView() {
		
		if (isOpenAboutView) {
			
			isOpenAboutView = false;
			
			ViewAnimator.on(text_about)
					.alpha(1, 0)
					.pivotY(0)
					.scaleY(1, 0)
					.translationY(0, -150)
					.duration(200)
					.onStop(() -> text_about.setVisibility(View.GONE))
					.start();
			
			ViewAnimator.on(findView(view_about_content, R.id.expand_indicator))
					.rotation(180, 0)
					.duration(500)
					.start();
			
		}
		else {
			
			isOpenAboutView = true;
			text_about.setVisibility(View.VISIBLE);
			
			ViewAnimator.on(text_about)
					.alpha(0, 1)
					.pivotY(0)
					.scaleY(0, 1)
					.translationY(-150, 0)
					.duration(200)
					.start();
			
			ViewAnimator.on(findView(view_about_content, R.id.expand_indicator))
					.rotation(0, 180)
					.duration(500)
					.start();
		}
	}
	
}
