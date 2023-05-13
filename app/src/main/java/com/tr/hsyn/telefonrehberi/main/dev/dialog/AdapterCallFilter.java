package com.tr.hsyn.telefonrehberi.main.dev.dialog;


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


/**
 * Arama kayıtları filtreleri için adapter.
 * Filtreleme seçeneklerini tutacak.
 */
public final class AdapterCallFilter extends RecyclerView.Adapter<AdapterCallFilter.Holder> {
	
	private final String[]          filters;
	private final ItemIndexListener selectListener;
	private final int               selected;
	
	public AdapterCallFilter(@NonNull String[] filters, @NonNull ItemIndexListener selectListener, int selected) {
		
		this.filters        = filters;
		this.selectListener = selectListener;
		this.selected       = selected;
	}
	
	@NonNull
	@Override
	public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		
		return new Holder(LayoutInflater.from(parent.getContext()).inflate(com.tr.hsyn.callfilter.R.layout.call_filter_dialog_item, parent, false), selectListener);
	}
	
	@Override
	public void onBindViewHolder(@NonNull Holder holder, int position) {
		
		holder.filter.setText(filters[position]);
		
		switch (position) {
			
			case 0:
				
				holder.type.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.all_calls, null));
				break;
			
			case 1:
				
				holder.type.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.incoming_call, null));
				break;
			
			case 2:
				
				holder.type.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.outgoing_call, null));
				break;
			
			case 3:
				
				holder.type.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.missed_call, null));
				break;
			
			case 4:
				
				holder.type.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.rejected_call, null));
				break;
			
			case 5:
				
				holder.type.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.no_name_calls, null));
				break;
			case 6:
				
				holder.type.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.random_call, null));
				break;
			case 7:
				
				holder.type.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.most_incomming, null));
				break;
			
			case 8:
				
				holder.type.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.most_outgoing, null));
				break;
			
			case 9:
				
				holder.type.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.most_missed, null));
				break;
			
			case 10:
				
				holder.type.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.most_rejected, null));
				break;
			
			case 11:
				
				holder.type.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.most_incomming_duration, null));
				break;
			
			case 12:
				
				holder.type.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.most_outgoing_duration, null));
				break;
		}
		
		if (position == selected) {
			
			holder.itemView.findViewById(com.tr.hsyn.callfilter.R.id.call_filter_inner_layout).setBackgroundColor(Colors.lighter(Color.YELLOW, 0.9f));
		}
		else {
			
			holder.itemView.findViewById(com.tr.hsyn.callfilter.R.id.call_filter_inner_layout).setBackgroundColor(Color.TRANSPARENT);
		}
	}
	
	@Override
	public int getItemCount() {
		
		return filters.length;
	}
	
	protected static final class Holder extends RecyclerView.ViewHolder {
		
		public TextView  filter;
		public ImageView type;
		
		public Holder(@NonNull View itemView, @NonNull ItemIndexListener selectListener) {
			
			super(itemView);
			
			filter = itemView.findViewById(com.tr.hsyn.callfilter.R.id.text_filter);
			type   = itemView.findViewById(com.tr.hsyn.callfilter.R.id.type);
			
			itemView.setBackgroundResource(Colors.getRipple());
			itemView.setOnClickListener(v -> selectListener.onItemIndex(getAdapterPosition()));
			
			
		}
	}
}
