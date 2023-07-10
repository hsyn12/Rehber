package com.tr.hsyn.telefonrehberi.main.code.comment.dialog;


import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.textdrawable.TextDrawable;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.Holder> {
	
	private final List<Contact> contacts;
	
	public ContactListAdapter(List<Contact> contacts) {this.contacts = contacts;}
	
	@NotNull
	@Override
	public Holder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
		
		return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact, parent, false));
	}
	
	@Override
	public void onBindViewHolder(@NotNull ContactListAdapter.Holder holder, int position) {
		
		Contact contact = contacts.get(position);
		
		String name = contact.getName();
		
		if (name == null || name.trim().isEmpty()) {
			
			name = holder.itemView.getContext().getString(R.string.no_name);
		}
		
		holder.name.setText(name);
		
		String pic   = contact.getPic();
		int    color = Colors.getRandomColor();
		
		if (pic == null) {
			
			Drawable image = TextDrawable.builder()
					.beginConfig()
					.useFont(ResourcesCompat.getFont(holder.itemView.getContext(), com.tr.hsyn.resfont.R.font.z))
					.endConfig()
					.buildRound(Stringx.getLetter(contact.getName()), color);
			
			holder.image.setImageDrawable(image);
		}
		else {
			
			holder.image.setImageURI(Uri.parse(pic));
		}
		
	}
	
	@Override
	public int getItemCount() {
		
		return contacts.size();
	}
	
	public static final class Holder extends RecyclerView.ViewHolder {
		
		TextView  name;
		ImageView image;
		
		public Holder(@NotNull View itemView) {
			
			super(itemView);
			name  = itemView.findViewById(R.id.contact_name);
			image = itemView.findViewById(R.id.contact_image);
			
			itemView.setBackgroundResource(Colors.getRipple());
		}
	}
}
