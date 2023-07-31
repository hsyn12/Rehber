package com.tr.hsyn.telefonrehberi.main.code.comment.dialog;


import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;

import com.tr.hsyn.telefonrehberi.dev.Phone;

import org.jetbrains.annotations.NotNull;


/**
 * Provides the base class for dialogs.
 */
public abstract class Dialog {
	
	protected ViewGroup rootView;
	
	/**
	 * @return the dialog
	 */
	protected abstract AlertDialog getDialog();
	
	/**
	 * Sets the height.
	 *
	 * @param view   view
	 * @param params the params of the view
	 */
	protected void setHeight(@NotNull View view, ViewGroup.@NotNull LayoutParams params) {
		
		params.height = (int) Phone.getDialogProperHeight();
		view.setLayoutParams(params);
	}
	
	/**
	 * Shows the dialog.
	 */
	public void show() {
		
		getDialog().show();
	}
	
	/**
	 * Called when the dialog is canceled.
	 *
	 * @param dialog the dialog
	 */
	protected void onCancel(@NotNull DialogInterface dialog) {
		
		dialog.dismiss();
	}
	
	/**
	 * Sets the animation.
	 *
	 * @param animationResId the animation res ID
	 */
	protected void setAnimation(int animationResId) {
		
		android.view.Window window = getDialog().getWindow();
		
		if (window != null) {
			window.getAttributes().windowAnimations = animationResId;
		}
	}
	
	/**
	 * Creates a new instance of the {@link AlertDialog.Builder} class.
	 *
	 * @param activity       the activity
	 * @param cancelable     the cancelable
	 * @param cancelListener the cancel listener
	 * @return the builder
	 */
	protected AlertDialog.Builder getBuilder(@NotNull Activity activity, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(cancelable);
		builder.setOnCancelListener(cancelListener != null ? cancelListener : this::onCancel);
		return builder;
	}
	
	protected AlertDialog.Builder getBuilder(@NotNull Activity activity, int layout, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(cancelable);
		builder.setOnCancelListener(cancelListener != null ? cancelListener : this::onCancel);
		
		rootView = inflateLayout(activity, layout);
		builder.setView(rootView);
		
		return builder;
	}
	
	protected AlertDialog.Builder getBuilder(@NotNull Activity activity, int layout) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(true);
		builder.setOnCancelListener(this::onCancel);
		
		rootView = inflateLayout(activity, layout);
		builder.setView(rootView);
		
		return builder;
	}
	
	/**
	 * Creates the layout view.
	 *
	 * @param activity    the activity
	 * @param layoutResId the layout res ID
	 * @return the view
	 */
	protected ViewGroup inflateLayout(@NotNull Activity activity, int layoutResId) {
		
		return (ViewGroup) activity.getLayoutInflater().inflate(layoutResId, null, false);
	}
	
	/**
	 * Returns the view by ID.
	 *
	 * @param view  the view to search in
	 * @param resId the res ID
	 * @param <T>   the type
	 * @return the view
	 */
	protected <T> T findView(@NotNull ViewGroup view, int resId) {
		
		//noinspection unchecked
		return (T) view.findViewById(resId);
	}
	
	protected <T> T findView(int resId) {
		
		//noinspection unchecked
		return (T) rootView.findViewById(resId);
	}
	
	
}
