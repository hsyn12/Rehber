package com.tr.hsyn.telefonrehberi.main.code.comment.dialog;


import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;

import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.dev.Phone;

import org.jetbrains.annotations.NotNull;


public abstract class Dialog {
	
	protected abstract AlertDialog getDialog();
	
	protected void setHeight(@NotNull View view, ViewGroup.@NotNull LayoutParams params) {
		
		params.height = (int) Phone.getDialogProperHeight();
		view.setLayoutParams(params);
	}
	
	public void show() {
		
		getDialog().show();
	}
	
	protected void onCancel(@NotNull DialogInterface dialog) {
		
		dialog.dismiss();
	}
	
	protected void setAnimation(int animationResId) {
		
		android.view.Window window = getDialog().getWindow();
		
		if (window != null) {
			window.getAttributes().windowAnimations = R.style.DialogAnimationBounce;
		}
	}
	
	protected AlertDialog.Builder getBuilder(@NotNull Activity activity, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(cancelable);
		builder.setOnCancelListener(cancelListener != null ? cancelListener : this::onCancel);
		return builder;
	}
	
	protected ViewGroup inflateLayout(@NotNull Activity activity, int layoutResId) {
		
		return (ViewGroup) activity.getLayoutInflater().inflate(layoutResId, null, false);
	}
	
	protected <T> T findView(@NotNull ViewGroup view, int resId) {
		
		//noinspection unchecked
		return (T) view.findViewById(resId);
	}
	
	
}
