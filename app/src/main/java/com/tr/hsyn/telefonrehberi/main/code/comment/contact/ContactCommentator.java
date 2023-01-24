package com.tr.hsyn.telefonrehberi.main.code.comment.contact;


import android.app.Activity;

import com.tr.hsyn.telefonrehberi.main.code.comment.Commentator;
import com.tr.hsyn.telefonrehberi.main.code.comment.ContactCommentStore;
import com.tr.hsyn.telefonrehberi.main.code.comment.Moody;
import com.tr.hsyn.telefonrehberi.main.code.comment.contact.defaults.DefaultContactCommentator;
import com.tr.hsyn.telefonrehberi.main.code.contact.cast.Contact;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;


/**
 * Kişi yorumlayıcısı.<br>
 */
public interface ContactCommentator extends Commentator<Contact> {
	
	@NotNull
	CharSequence commentSavedDate();
	
	@NotNull
	CharSequence commentHistory();
	
	static ContactCommentator createCommentator(Activity activity) {
		
		Moody moody = Moody.getMood();
		
		var store = ContactCommentStore.createCommentStore(activity, moody);
		
		switch (moody) {
			
			case DEFAULT:
				
				var commentator = new DefaultContactCommentator(store);
				xlog.d("Default Commentator");
				return commentator;
			case HAPPY:
				xlog.d("not yet happy");
		}
		
		xlog.d("Wrong moody : %d", moody.ordinal());
		return new DefaultContactCommentator(store);
	}
}
