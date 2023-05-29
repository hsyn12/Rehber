package com.tr.hsyn.telefonrehberi.dev.android.dialog;


import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.tr.hsyn.telefonrehberi.R;

import java.util.List;


/**
 * Sadece onaylama butonu olan bir dialog
 */
public interface Dialog {
	
	static Dialog newDialog(Activity activity) {
		
		return new ConfirmDialog(activity);
	}
	
	static Dialog newDialog_YesNo(Activity activity) {
		
		return new Oscar(activity);
	}
	
	static View inflate(@NonNull Activity activity, int id) {
		
		return activity.getLayoutInflater().inflate(id, null, false);
	}
	
	static void openConfirmDialog(
			Activity activity,
			CharSequence message,
			DialogInterface.OnClickListener okeyListener,
			DialogInterface.OnClickListener cancelListener) {
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(true);
		
		builder.setMessage(message);
		builder.setPositiveButton(activity.getString(R.string.okey), okeyListener);
		builder.setNegativeButton(activity.getString(R.string.cancel), cancelListener);
		
		AlertDialog alertDialog = builder.create();
		alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationBounce;
		
		alertDialog.show();
	}
	
	static void openMessageDialog(Activity activity, CharSequence message) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(true);
		
		DialogInterface.OnClickListener listener = (DialogInterface di, int w) -> di.dismiss();
		
		builder.setMessage(message);
		builder.setPositiveButton(activity.getString(R.string.okey), listener);
		
		AlertDialog alertDialog = builder.create();
		alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationBounce;
		
		alertDialog.show();
	}
	
	static void openConfirmDialog(
			Activity activity,
			CharSequence message,
			DialogInterface.OnClickListener okeyListener) {
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(true);
		
		builder.setMessage(message);
		builder.setPositiveButton(activity.getString(R.string.okey), okeyListener);
		builder.setNegativeButton(activity.getString(R.string.cancel), (di, w) -> di.dismiss());
		
		AlertDialog alertDialog = builder.create();
		alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationBounce;
		
		alertDialog.show();
	}
	
	static void openInputDialog(
			Activity activity,
			CharSequence title,
			InputListener inputListener,
			DialogInterface.OnClickListener cancelListener) {
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(true);
		builder.setTitle(title);
		
		EditText editText = new EditText(activity);
		builder.setView(editText);
		
		builder.setPositiveButton(activity.getString(R.string.okey), (d, w) -> inputListener.onInput(editText.getText().toString()));
		builder.setNegativeButton(activity.getString(R.string.cancel), cancelListener);
		
		AlertDialog alertDialog = builder.create();
		alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationBounce;
		
		alertDialog.show();
	}
	
	static void openInputDialog(
			Activity activity,
			CharSequence title,
			CharSequence edittextValue,
			InputListener inputListener,
			DialogInterface.OnClickListener cancelListener) {
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(true);
		builder.setTitle(title);
		
		EditText editText = new EditText(activity);
		builder.setView(editText);
		editText.setText(edittextValue);
		
		builder.setPositiveButton(activity.getString(R.string.okey), (d, w) -> inputListener.onInput(editText.getText().toString()));
		builder.setNegativeButton(activity.getString(R.string.cancel), cancelListener);
		
		AlertDialog alertDialog = builder.create();
		alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationBounce;
		
		alertDialog.show();
	}
	
	@NonNull
	static AlertDialog createDialog(Activity activity, View layout) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(true).setView(layout);
		AlertDialog alertDialog = builder.create();
		alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationBounce;
		
		return alertDialog;
	}
	
	static boolean hasOpenedDialogs(@NonNull AppCompatActivity activity) {
		
		List<Fragment> fragments = activity.getSupportFragmentManager().getFragments();
		
		for (Fragment fragment : fragments) {
			
			if (fragment instanceof DialogFragment) {
				
				return true;
			}
		}
		
		return false;
	}
	
	Activity getActivity();
	
	int getLayoutRecourceId();
	
	Dialog show();
	
	void dismiss();
	
	Dialog title(CharSequence title);
	
	Dialog message(CharSequence message);
	
	Dialog confirmText(CharSequence message);
	
	Dialog cancelable(boolean cancelable);
	
	Dialog onConfirm(Runnable onConfirm);
	
	Dialog animationResource(int animationResource);
	
	default Dialog onReject(Runnable onReject) {return this;}
	
	default Dialog rejectText(CharSequence message) {return this;}
	
}
