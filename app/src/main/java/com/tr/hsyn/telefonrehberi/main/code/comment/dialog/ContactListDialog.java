package com.tr.hsyn.telefonrehberi.main.code.comment.dialog;


import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * Created by hsyn on 11.12.2021.
 */
public class ContactListDialog extends Dialog {
	
	private final AlertDialog dialog;
	
	public ContactListDialog(@NotNull Activity activity, @NotNull List<Contact> contactList, String title, String subtitle) {
		
		AlertDialog.Builder builder = getBuilder(activity, true, null);
		ViewGroup           view    = inflateLayout(activity, R.layout.contact_list);
		RecyclerView        list    = findView(view, R.id.list_contacts);
		list.setAdapter(new ContactListAdapter(contactList));
		setHeight(list, list.getLayoutParams());
		
		builder.setView(view);
		((TextView) view.findViewById(R.id.title)).setText(title);
		((TextView) view.findViewById(R.id.sub_title)).setText(subtitle);
		view.findViewById(R.id.header_include).setBackgroundColor(Colors.getPrimaryColor());
		dialog = builder.create();
		
		setAnimation(R.style.DialogAnimationBounce);
	}
	
	@Override
	protected AlertDialog getDialog() {
		
		return dialog;
	}
	
	
}
