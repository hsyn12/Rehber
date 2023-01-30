package com.tr.hsyn.telefonrehberi.main.activity.contact.detail;


import android.annotation.SuppressLint;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.gate.AutoGate;
import com.tr.hsyn.gate.Gate;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.code.comment.contact.ContactCommentator;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.ContactKey;
import com.tr.hsyn.telefonrehberi.main.dev.Over;
import com.tr.hsyn.vanimator.ViewAnimator;
import com.tr.hsyn.xlog.xlog;

import java.util.List;


public class ContactDetailsAbout extends ContactDetailsMenu {
	
	//- Burada kişi hakkında bazı kayda değer bilgileri
	//- kompozisyon halinde sunmak istiyoruz.
	
	private       boolean      isOpenAboutView;
	private       ViewGroup    view_about_content;
	private       TextView     text_about;
	private final Gate         gateAbout = AutoGate.newGate(1000L);
	private       CharSequence comment;
	private       boolean      reComment = true;
	
	@Override
	protected void onHistoryUpdate() {
		
		super.onHistoryUpdate();
		
		List<String> numbers = contact.getData(ContactKey.NUMBERS);
		
		if (numbers == null || numbers.isEmpty()) return;
		
		if (Over.CallLog.exist()) {
			
			reComment = true;
			setupCommentViews();
		}
	}
	
	private void comment() {
		
		if (reComment) {
			
			reComment = false;
			ContactCommentator commentator = ContactCommentator.createCommentator(this);
			comment = commentator.commentate(contact);
			text_about.setText(comment);
		}
		
		xlog.d(comment);
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
		
		comment();
		
		if (gateAbout.enter()) {
			animateAboutView();
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
