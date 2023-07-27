package com.tr.hsyn.telefonrehberi.main.call.activity.random.listener;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.textdrawable.TextDrawable;

import java.util.List;
import java.util.Set;


interface CheckListener {
	
	void onChecked(boolean isCheck, int index);
}

public class AdapterSelectContacts extends RecyclerView.Adapter<AdapterSelectContacts.Holder> implements CheckListener {
	
	private final List<Contact> contacts;
	private final Set<String>   selectedIndexes;
	
	public AdapterSelectContacts(@NonNull List<Contact> contacts, @NonNull Set<String> selectedIndexes) {
		
		this.contacts        = contacts;
		this.selectedIndexes = selectedIndexes;
	}
	
	@Override
	public void onChecked(boolean isCheck, int index) {
		
		if (isCheck) {
			
			selectedIndexes.add(String.valueOf(contacts.get(index).getContactId()));
		}
		else {
			
			selectedIndexes.remove(String.valueOf(contacts.get(index).getContactId()));
		}
	}
	
	@NonNull
	@Override
	public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		
		return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_selectable, parent, false), this);
	}
	
	@Override
	public void onBindViewHolder(@NonNull Holder holder, int position) {
		
		Contact contact = contacts.get(position);
		
		holder.name.setText(contact.getName());
		holder.selected.setChecked(selectedIndexes.contains(String.valueOf(contact.getContactId())));
		
		TextDrawable img = TextDrawable.builder()
				.beginConfig()
				.useFont(ResourcesCompat.getFont(holder.itemView.getContext(), com.tr.hsyn.resfont.R.font.z))
				.endConfig()
				.buildRound(Stringx.getLetter(contact.getName()), Colors.getRandomColor());
		
		holder.image.setImageDrawable(img);
	}
	
	@Override
	public int getItemCount() {
		
		return contacts.size();
	}
	
	public void selectAll(boolean isSelect) {
		
		if (isSelect) {
			
			for (int i = 0; i < contacts.size(); i++) {
				
				Contact c = contacts.get(i);
				
				if (!selectedIndexes.contains(String.valueOf(c.getContactId()))) {
					
					selectedIndexes.add(String.valueOf(c.getContactId()));
					notifyItemChanged(i);
				}
			}
		}
		else {
			
			for (int i = 0; i < contacts.size(); ) {
				
				Contact c = contacts.get(i);
				
				if (selectedIndexes.contains(String.valueOf(c.getContactId()))) {
					
					selectedIndexes.remove(String.valueOf(c.getContactId()));
					notifyItemChanged(i);
				}
				else i++;
			}
		}
	}
	
	static class Holder extends RecyclerView.ViewHolder {
		
		public final TextView  name;
		public final ImageView image;
		public final CheckBox  selected;
		
		public Holder(@NonNull View itemView, @NonNull CheckListener listener) {
			
			super(itemView);
			
			name     = itemView.findViewById(R.id.name);
			image    = itemView.findViewById(R.id.image);
			selected = itemView.findViewById(R.id.check_box_contact);
			
			itemView.setBackgroundResource(Colors.getRipple());
			
			itemView.setOnClickListener(v -> selected.setChecked(!selected.isChecked()));
			selected.setOnCheckedChangeListener((button, check) -> listener.onChecked(check, getAdapterPosition()));
		}
	}
}
