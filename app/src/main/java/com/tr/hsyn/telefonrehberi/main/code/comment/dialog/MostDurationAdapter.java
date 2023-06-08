package com.tr.hsyn.telefonrehberi.main.code.comment.dialog;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.textdrawable.TextDrawable;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class MostDurationAdapter extends RecyclerView.Adapter<MostDurationAdapter.Holder> {
	
	private final List<MostDurationData> mostDurationDataList;
	
	public MostDurationAdapter(List<MostDurationData> mostDurationDataList) {this.mostDurationDataList = mostDurationDataList;}
	
	@NonNull
	@Override
	public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		
		return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.most_calls_item_view, parent, false));
	}
	
	@Override
	public void onBindViewHolder(@NonNull Holder holder, int position) {
		
		int              color = Colors.COLOR_GENERATOR.getRandomColor();
		MostDurationData item  = mostDurationDataList.get(position);
		holder.name.setText(item.getName());
		holder.text.setText(item.getText());
		holder.order.setImageDrawable(TextDrawable.builder().buildRound(String.valueOf(item.getOrder()), color));
	}
	
	@Override
	public int getItemCount() {
		
		return mostDurationDataList.size();
	}
	
	public static final class Holder extends RecyclerView.ViewHolder {
		
		TextView  name;
		TextView  text;
		ImageView order;
		
		public Holder(@NotNull View itemView) {
			
			super(itemView);
			
			text = itemView.findViewById(R.id.most_calls_item_text_type);
			ImageView type = itemView.findViewById(R.id.most_calls_item_img_type);
			name  = itemView.findViewById(R.id.most_calls_item_text_name);
			order = itemView.findViewById(R.id.most_calls_item_img_order_number);
			
			type.setImageDrawable(AppCompatResources.getDrawable(name.getContext(), com.tr.hsyn.resarrowdrawable.R.drawable.clock));
			itemView.setBackgroundResource(Colors.getRipple());
		}
	}
}
