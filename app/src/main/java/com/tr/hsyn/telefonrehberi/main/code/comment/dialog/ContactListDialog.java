package com.tr.hsyn.telefonrehberi.main.code.comment.dialog;


import android.app.Activity;
import android.content.DialogInterface;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.dev.Phone;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ContactListDialog {
	
	private final AlertDialog dialog;
	
	
	public ContactListDialog(@NotNull Activity activity, @NotNull List<Contact> contactList, String title, String subtitle) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(true);
		
		RelativeLayout view = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.most_call_dialog, null, false);
		//view.setMaxHeight((int) (Phone.getDisplaySize().y / 1.75f));
		RecyclerView list = view.findViewById(R.id.list_contacts);
		list.setAdapter(new ContactListAdapter(contactList));
		var param = (RelativeLayout.LayoutParams) list.getLayoutParams();
		param.height = (int) Phone.getDialogProperHeight();
		list.setLayoutParams(param);
		
		builder.setView(view);
		builder.setOnCancelListener(this::onCancel);
		((TextView) view.findViewById(R.id.title)).setText(title);
		((TextView) view.findViewById(R.id.sub_title)).setText(subtitle);
		view.findViewById(R.id.header_include).setBackgroundColor(Colors.getPrimaryColor());
		dialog = builder.create();
		
		android.view.Window window = dialog.getWindow();
		
		if (window != null) {
			window.getAttributes().windowAnimations = R.style.DialogAnimationBounce;
			
			
		}
		
	}
	
	public void show() {
		
		dialog.show();
	}
	
	private void onCancel(DialogInterface dialog) {
		
		dialog.dismiss();
	}
}
