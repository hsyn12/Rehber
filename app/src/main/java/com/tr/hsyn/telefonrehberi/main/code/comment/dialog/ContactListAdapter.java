package com.tr.hsyn.telefonrehberi.main.code.comment.dialog;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.Holder> {
	
	private final List<Contact> contacts;
	
	public ContactListAdapter(List<Contact> contacts) {this.contacts = contacts;}
	
	@NotNull
	@Override
	public Holder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
		
		return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list, parent, false));
	}
	
	@Override
	public void onBindViewHolder(@NotNull ContactListAdapter.Holder holder, int position) {
		
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
			
			
		}
	}
}
