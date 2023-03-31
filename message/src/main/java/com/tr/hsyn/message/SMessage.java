package com.tr.hsyn.message;


import android.app.Activity;
import android.view.View;

import com.tr.hsyn.message.snack.SnackBarItem;
import com.tr.hsyn.message.snack.SnackBarListener;


public class SMessage implements IMessage<SnackBarItem> {
	
	/**
	 * Mesaj türü
	 */
	private int                  messageType;
	/**
	 * Mesaj
	 */
	
	private CharSequence         message;
	/**
	 * Mesaj giriş çıkışlarını izleyecek vatandaş
	 */
	private SnackBarListener     snackBarListener;
	/**
	 * Buton tıklama olayını dinleyecek vatandaş
	 */
	private View.OnClickListener actionListener;
	/**
	 * Buton yazısı
	 */
	private CharSequence         actionMessage;
	/**
	 * Bu mesajdan önce gösterimde bir mesaj varsa o mesaj derhal iptal edilsin mi?
	 */
	private boolean              cancel;
	private int                  relationDegree = 1;
	/**
	 * Mesaj süresi
	 */
	private long                 duration       = DURATION_DEFAULT;
	/**
	 * Mesaj gösterilmeden önceki bekleme süresi
	 */
	private long                 delay          = DELAY;
	
	public static SMessageBuilder builder() {
		
		return SMessageBuilder.aSMessage();
	}
	
	@Override public int getRelationDegree() {
		
		return relationDegree;
	}
	
	@Override public CharSequence getMessage() {
		
		return message;
	}
	
	@Override
	public SnackBarItem showOn(Activity activity) {
		
		if (isNotRelated()) return null;
		
		if (activity == null)
			return null;
		
		
		if (snackBarListener == null) {
			
			if (activity instanceof SnackBarListener) snackBarListener = (SnackBarListener) activity;
			
		}
		
		if (message != null && message.length() != 0) {
			
			return Show.snake(
					activity,
					message,
					duration,
					delay,
					snackBarListener,
					actionListener,
					actionMessage,
					cancel,
					messageType
			);
		}
		
		return null;
	}
	
	public static final class SMessageBuilder {
		
		private int messageType;
		
		private CharSequence         message;
		private SnackBarListener     snackBarListener;
		private View.OnClickListener actionListener;
		private CharSequence         actionMessage;
		private boolean              cancel;
		private int                  relationDegree = 1;
		private long                 duration       = DURATION_DEFAULT;
		private long                 delay          = DELAY;
		
		private SMessageBuilder() {}
		
		public static SMessageBuilder aSMessage() { return new SMessageBuilder(); }
		
		public SMessageBuilder type(int messageType) {
			
			this.messageType = messageType;
			return this;
		}
		
		public SMessageBuilder message(CharSequence message) {
			
			this.message = message;
			return this;
		}
		
		public SMessageBuilder snackBarListener(SnackBarListener snackBarListener) {
			
			this.snackBarListener = snackBarListener;
			return this;
		}
		
		public SMessageBuilder actionListener(View.OnClickListener actionListener) {
			
			this.actionListener = actionListener;
			return this;
		}
		
		public SMessageBuilder actionMessage(CharSequence actionMessage) {
			
			this.actionMessage = actionMessage;
			return this;
		}
		
		public SMessageBuilder cancel(boolean cancel) {
			
			this.cancel = cancel;
			return this;
		}
		
		public SMessageBuilder relationDegree(int relationDegree) {
			
			this.relationDegree = relationDegree;
			return this;
		}
		
		public SMessageBuilder duration(long duration) {
			
			this.duration = duration;
			return this;
		}
		
		public SMessageBuilder delay(long delay) {
			
			this.delay = delay;
			return this;
		}
		
		public SMessage build() {
			
			SMessage sMessage = new SMessage();
			sMessage.duration         = this.duration;
			sMessage.relationDegree   = this.relationDegree;
			sMessage.snackBarListener = this.snackBarListener;
			sMessage.message          = this.message;
			sMessage.delay            = this.delay;
			sMessage.actionListener   = this.actionListener;
			sMessage.messageType      = this.messageType;
			sMessage.actionMessage    = this.actionMessage;
			sMessage.cancel           = this.cancel;
			return sMessage;
		}
	}
}
