package com.tr.hsyn.telefonrehberi.main.contact.fragment.adapter;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.data.Contacts;

import org.jetbrains.annotations.NotNull;


public class ContactFilterAdapter extends RecyclerView.Adapter<ContactFilterAdapter.Holder> {
	
	private final String[]          filters;
	private final ItemIndexListener selectListener;
	private final int               selected;
	
	public ContactFilterAdapter(@NonNull String[] filters, @NonNull ItemIndexListener selectListener, int selected) {
		
		this.filters        = filters;
		this.selectListener = selectListener;
		this.selected       = selected;
	}
	
	@NonNull
	@NotNull
	@Override
	public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
		
		return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_filter_item, parent, false), selectListener);
	}
	
	@Override
	public void onBindViewHolder(@NonNull @NotNull ContactFilterAdapter.Holder holder, int position) {
		
		holder.filter.setText(filters[position]);
		
		switch (position) {
			case Contacts.FILTER_ALL:
				holder.type.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.groups_mycontacts, null));
				break;
			case Contacts.FILTER_MOST_INCOMING:
				holder.type.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.most_incomming, null));
				break;
			case Contacts.FILTER_MOST_OUTGOING:
				holder.type.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.most_outgoing, null));
				break;
			case Contacts.FILTER_MOST_MISSED:
				holder.type.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.most_missed, null));
				break;
			case Contacts.FILTER_MOST_REJECTED:
				holder.type.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.most_rejected, null));
				break;
			case Contacts.FILTER_MOST_INCOMING_DURATION:
				holder.type.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.most_incomming_duration, null));
				break;
			case Contacts.FILTER_MOST_OUTGOING_DURATION:
				holder.type.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.most_outgoing_duration, null));
				break;
			case Contacts.FILTER_MOST_TOTAL_DURATION:
				holder.type.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.all_calls, null));
				break;
		}
		
		if (position == selected) {
			
			holder.itemView.findViewById(R.id.filter_layout).setBackgroundColor(Colors.lighter(Color.YELLOW, 0.9f));
		}
		else {
			
			holder.itemView.findViewById(R.id.filter_layout).setBackgroundColor(Color.TRANSPARENT);
		}
	}
	
	@Override
	public int getItemCount() {
		
		return filters.length;
	}
	
	public static final class Holder extends RecyclerView.ViewHolder {
		
		public TextView  filter;
		public ImageView type;
		
		public Holder(@NonNull @NotNull View itemView, @NonNull ItemIndexListener selectListener) {
			
			super(itemView);
			
			filter = itemView.findViewById(R.id.text_filter);
			type   = itemView.findViewById(R.id.type);
			
			itemView.setBackgroundResource(Colors.getRipple());
			itemView.setOnClickListener(v -> selectListener.onItemIndex(getAdapterPosition()));
		}
	}
	
}
