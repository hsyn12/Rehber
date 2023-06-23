package com.tr.hsyn.telefonrehberi.main.code.comment.dialog;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;


public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.Holder> {
	
	
	@NotNull
	@Override
	public Holder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
		
		return null;
	}
	
	@Override
	public void onBindViewHolder(@NotNull ContactListAdapter.Holder holder, int position) {
		
	}
	
	@Override
	public int getItemCount() {
		
		return 0;
	}
	
	public static final class Holder extends RecyclerView.ViewHolder {
		
		TextView  name;
		ImageView image;
		
		public Holder(@NotNull View itemView) {
			
			super(itemView);
			
			
		}
	}
}
