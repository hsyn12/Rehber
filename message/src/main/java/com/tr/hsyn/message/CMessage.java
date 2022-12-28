package com.tr.hsyn.message;


import android.app.Activity;

import com.tr.hsyn.message.cookie.CookieBar;
import com.tr.hsyn.message.cookie.OnActionClickListener;

//@off

/**
 * <h1>CMessage</h1>
 *
 * <p>
 * Cookie mesaj.
 *
 * @author hsyn 19 Aralık 2019 Perşembe 18:22:42
 */
//@on
@SuppressWarnings("FieldMayBeFinal")

public class CMessage implements IMessage<CookieBar> {
	
	
	private static int                   primaryColor;
	/**
	 * Title
	 */
	private        CharSequence          title          = "Yönetici";
	private        long                  duration       = DURATION_DEFAULT;
	private        long                  delay          = DELAY;
	private        int                   position       = CookieBar.TOP;
	private        boolean               autoDismiss    = true;
	private        int                   backgroundColor;
	/**
	 * Message
	 */
	private        CharSequence          message;
	/**
	 * Tür
	 */
	private        int                   type;
	/**
	 * Buton tıklama olayını dinleyecek vatandaş
	 */
	private        OnActionClickListener action;
	/**
	 * Buton yazısı
	 */
	private        CharSequence          actionMessage;
	/**
	 * Icon
	 */
	private        int                   icon           = R.drawable.info;
	private        int                   relationDegree = 1;
	
	public static void setPrimaryColor(int color) {
		
		primaryColor = color;
	}
	
	public static CMessageBuilder builder() {
		
		return CMessageBuilder.aCMessage();
	}
	
	@Override
	public int getRelationDegree() {
		
		return relationDegree;
	}
	
	@Override public CharSequence getMessage() {
		
		return message;
	}
	
	@Override
	public CookieBar showOn(Activity activity) {
		
		if (isNotRelated()) return null;
		
		if (activity == null) return null;
		
		if (message != null && message.length() != 0) {
			
			if (type != GlobalMessage.INFO) {
				
				if (type == GlobalMessage.WARN) {
					
					icon = R.drawable.warning;
				}
				else {
					
					icon = R.drawable.error;
				}
			}
			
			
			return CookieBar.builder(activity)
					.title(title)
					.message(message)
					.enableAutoDismiss(autoDismiss)
					.delay(delay)
					.duration(duration)
					.cookiePosition(position)
					.icon(icon)
					.actionColor(R.color.white)
					.action(actionMessage, action)
					.swipeToDismiss(true)
					.backgroundColor(backgroundColor == 0 ? primaryColor : backgroundColor)
					.show();
			
		}
		
		return null;
	}
	
	public static final class CMessageBuilder {
		
		private static int                   primaryColor;
		private        CharSequence          title          = "Yönetici";
		private        long                  duration       = DURATION_DEFAULT;
		private        long                  delay          = DELAY;
		private        int                   position       = CookieBar.TOP;
		private        boolean               autoDismiss    = true;
		private        int                   backgroundColor;
		private        CharSequence          message;
		private        int                   type;
		private        OnActionClickListener action;
		private        CharSequence          actionMessage;
		private        int                   icon           = R.drawable.info;
		private        int                   relationDegree = 1;
		
		private CMessageBuilder() {}
		
		public static CMessageBuilder aCMessage() { return new CMessageBuilder(); }
		
		public CMessageBuilder title(CharSequence title) {
			
			this.title = title;
			return this;
		}
		
		public CMessageBuilder duration(long duration) {
			
			this.duration = duration;
			return this;
		}
		
		public CMessageBuilder delay(long delay) {
			
			this.delay = delay;
			return this;
		}
		
		public CMessageBuilder position(int position) {
			
			this.position = position;
			return this;
		}
		
		public CMessageBuilder autoDismiss(boolean autoDismiss) {
			
			this.autoDismiss = autoDismiss;
			return this;
		}
		
		public CMessageBuilder backgroundColor(int backgroundColor) {
			
			this.backgroundColor = backgroundColor;
			return this;
		}
		
		public CMessageBuilder message(CharSequence message) {
			
			this.message = message;
			return this;
		}
		
		public CMessageBuilder type(int type) {
			
			this.type = type;
			return this;
		}
		
		public CMessageBuilder action(OnActionClickListener action) {
			
			this.action = action;
			return this;
		}
		
		public CMessageBuilder actionMessage(CharSequence actionMessage) {
			
			this.actionMessage = actionMessage;
			return this;
		}
		
		public CMessageBuilder icon(int icon) {
			
			this.icon = icon;
			return this;
		}
		
		public CMessageBuilder relationDegree(int relationDegree) {
			
			this.relationDegree = relationDegree;
			return this;
		}
		
		public CMessage build() {
			
			CMessage cMessage = new CMessage();
			setPrimaryColor(primaryColor);
			cMessage.autoDismiss     = this.autoDismiss;
			cMessage.duration        = this.duration;
			cMessage.title           = this.title;
			cMessage.action          = this.action;
			cMessage.position        = this.position;
			cMessage.actionMessage   = this.actionMessage;
			cMessage.delay           = this.delay;
			cMessage.backgroundColor = this.backgroundColor;
			cMessage.message         = this.message;
			cMessage.icon            = this.icon;
			cMessage.type            = this.type;
			cMessage.relationDegree  = this.relationDegree;
			return cMessage;
		}
	}
}
