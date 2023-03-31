package com.tr.hsyn.message;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.tr.hsyn.message.snack.SnackBar;
import com.tr.hsyn.message.snack.SnackBarItem;
import com.tr.hsyn.message.snack.SnackBarListener;


public class Show {
	
	private static       int  primaryColor;
	private static final long C_DURATION = 15000L;
	
	public static void setPrimaryColor(int primaryColor) {
		
		Show.primaryColor = primaryColor;
		CMessage.setPrimaryColor(primaryColor);
	}
	
	public static void tost(Context context, CharSequence message) {
		
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	
	public static SnackBarItem snake(final Activity activity, CharSequence message, long duration, long delay, SnackBarListener snackBarListener, View.OnClickListener clickListener, CharSequence actionMessage, boolean cancel, int messageType) {
		
		return SnackBar.sbi(
				activity,
				message,
				duration,
				cancel,
				delay,
				snackBarListener,
				clickListener,
				actionMessage,
				messageType);
	}
	
	public static void cookie(Activity activity, CharSequence mesage) {
		
		CMessage.builder()
				.message(mesage)
				.duration(C_DURATION)
				.backgroundColor(primaryColor)
				.build()
				.showOn(activity);
	}
	
	public static void cookie(Activity activity, CharSequence mesage, long duration) {
		
		CMessage.builder()
				.message(mesage)
				.duration(duration)
				.backgroundColor(primaryColor)
				.build()
				.showOn(activity);
	}
	
	public static void cookie(Activity activity, CharSequence title, CharSequence message) {
		
		CMessage.builder()
				.title(title)
				.message(message)
				.duration(C_DURATION)
				.backgroundColor(primaryColor)
				.build()
				.showOn(activity);
	}
	
	public static void cookie(Activity activity, CharSequence title, CharSequence message, long duration) {
		
		CMessage.builder()
				.title(title)
				.message(message)
				.duration(duration)
				.backgroundColor(primaryColor)
				.build()
				.showOn(activity);
	}
	
	public static void snake(Activity activity, CharSequence message) {
		
		SMessage.builder()
				.message(message)
				.snackBarListener(activity instanceof SnackBarListener ? (SnackBarListener) activity : null)
				.build().showOn(activity);
		
	}
	
	public static void snake(Activity activity, CharSequence message, int type) {
		
		SMessage.builder()
				.message(message)
				.type(type)
				.snackBarListener(activity instanceof SnackBarListener ? (SnackBarListener) activity : null)
				.build().showOn(activity);
		
	}
	
	public static void snake(Activity activity, CharSequence message, long duration) {
		
		SMessage.builder()
				.message(message)
				.duration(duration)
				.snackBarListener(activity instanceof SnackBarListener ? (SnackBarListener) activity : null)
				.build().showOn(activity);
		
	}
}
