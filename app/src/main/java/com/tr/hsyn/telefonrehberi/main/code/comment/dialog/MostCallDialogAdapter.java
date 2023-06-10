package com.tr.hsyn.telefonrehberi.main.code.comment.dialog;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.textdrawable.TextDrawable;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;


public class MostCallDialogAdapter extends RecyclerView.Adapter<MostCallDialogAdapter.Holder> {
	
	private final List<MostCallItemViewData> mostCallItemViewDataList;
	
	public MostCallDialogAdapter(List<MostCallItemViewData> mostCallItemViewDataList) {
		
		this.mostCallItemViewDataList = mostCallItemViewDataList;
	}
	
	@NotNull
	@Override
	public Holder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
		
		return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.most_calls_item_view, parent, false));
	}
	
	@Override
	public void onBindViewHolder(@NotNull Holder holder, int position) {
		
		MostCallItemViewData item  = mostCallItemViewDataList.get(position);
		int                  color = Colors.COLOR_GENERATOR.getRandomColor();
		int                  order = item.getOrder();
		
		holder.name.setText(item.getName());
		
		holder.count.setImageDrawable(TextDrawable.builder().buildRound(String.valueOf(order != 0 ? order : position + 1), color));
		holder.txtType.setText(String.format(Locale.getDefault(), "%d %s", item.getCallSize(), holder.itemView.getContext().getString(R.string.call_record)));
		
		if (item.isSelected()) {
			int _color = Colors.getColor(holder.itemView.getContext(), com.tr.hsyn.rescolors.R.color.c_lightmediumorchid);
			holder.itemView.setBackgroundColor(Colors.lighter(_color, 0.5f));
		}
		else {
			holder.itemView.setBackgroundResource(Colors.getRipple());
		}
		
	}
	
	@Override
	public int getItemCount() {
		
		return mostCallItemViewDataList.size();
	}
	
	public static final class Holder extends RecyclerView.ViewHolder {
		
		TextView  name;
		TextView  txtType;
		ImageView count;
		
		public Holder(@NotNull View itemView) {
			
			super(itemView);
			
			txtType = itemView.findViewById(R.id.most_calls_item_text_type);
			ImageView type = itemView.findViewById(R.id.most_calls_item_img_type);
			name  = itemView.findViewById(R.id.most_calls_item_text_name);
			count = itemView.findViewById(R.id.most_calls_item_img_order_number);
			
			type.setImageDrawable(AppCompatResources.getDrawable(name.getContext(), R.drawable.all_calls));
			itemView.setBackgroundResource(Colors.getRipple());
		}
	}
}
