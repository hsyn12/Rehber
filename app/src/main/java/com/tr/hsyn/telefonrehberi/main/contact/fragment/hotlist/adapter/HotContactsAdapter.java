package com.tr.hsyn.telefonrehberi.main.contact.fragment.hotlist.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.telefonrehberi.R;

import org.jetbrains.annotations.NotNull;


public class HotContactsAdapter extends RecyclerView.Adapter<HotContactsAdapter.Holder> {
	
	
	@NotNull
	@Override
	public Holder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
		
		return null;
	}
	
	@Override
	public void onBindViewHolder(@NotNull HotContactsAdapter.Holder holder, int position) {
		
	}
	
	@Override
	public int getItemCount() {
		
		return 0;
	}
	
	public static final class Holder extends RecyclerView.ViewHolder {
		
		public final TextView  name;
		public final ImageView image;
		public final TextView  type;
		public final ImageView typeIcon;
		
		public Holder(@NonNull @NotNull View itemView, ItemIndexListener selectListener) {
			
			super(itemView);
			
			name     = itemView.findViewById(R.id.text_name);
			image    = itemView.findViewById(R.id.image);
			type     = itemView.findViewById(R.id.text_type);
			typeIcon = itemView.findViewById(R.id.img_type);
			
			itemView.setBackgroundResource(Colors.getRipple());
			itemView.setOnClickListener(v -> selectListener.onItemIndex(getAdapterPosition()));
		}
	}
}
