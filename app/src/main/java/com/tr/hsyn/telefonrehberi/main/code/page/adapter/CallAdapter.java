package com.tr.hsyn.telefonrehberi.main.code.page.adapter;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.colors.ColorHolder;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.fastscroller.FastScrollRecyclerView;
import com.tr.hsyn.files.Files;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.selection.SelectionListener;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.code.call.act.Calls;
import com.tr.hsyn.telefonrehberi.main.code.call.cast.CallKey;
import com.tr.hsyn.telefonrehberi.main.code.cast.ItemAdapter;
import com.tr.hsyn.textdrawable.TextDrawable;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CallAdapter extends RecyclerView.Adapter<CallAdapter.Holder> implements FastScrollRecyclerView.SectionedAdapter, ItemAdapter<Call> {
	
	private final List<Call>        selectedCalls = new ArrayList<>();
	private final ItemIndexListener selectListener;
	private final ItemIndexListener onAction;
	private final ItemIndexListener longClickListener;
	private final ColorHolder       themeInfo;
	private final SelectionListener selectionListener;
	private       List<Call>        calls;
	private       LayoutInflater    inflater;
	private       boolean           selectionMode;
	
	public CallAdapter(List<Call> calls, ItemIndexListener selectListener, ColorHolder themeInfo, ItemIndexListener onAction, ItemIndexListener longClickListener, SelectionListener selectionListener) {
		
		this.calls             = calls;
		this.selectListener    = selectListener;
		this.themeInfo         = themeInfo;
		this.onAction          = onAction;
		this.longClickListener = longClickListener;
		this.selectionListener = selectionListener;
		setHasStableIds(true);
	}
	
	public boolean isSelectionMode() {
		
		return selectionMode;
	}
	
	public void cancelSelection() {
		
		selectionMode = false;
		selectedCalls.clear();
	}
	
	public void selectAllItem(boolean select) {
		
		selectedCalls.clear();
		
		if (select) selectedCalls.addAll(calls);
	}
	
	private void makeSelection(Call call, boolean select) {
		
		if (select) selectedCalls.add(call);
		else selectedCalls.remove(call);
		
		if (selectionListener != null) selectionListener.onSelection(select);
	}
	
	@NonNull
	@Override
	public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		
		if (inflater != null)
			return new Holder(inflater.inflate(R.layout.call_item, parent, false));
		
		inflater = LayoutInflater.from(parent.getContext());
		
		return new Holder(inflater.inflate(R.layout.call_item, parent, false));
	}
	
	@Override
	public void onBindViewHolder(@NonNull Holder holder, int position) {
		
		Call   call   = calls.get(position);
		String name   = call.getName();
		String number = PhoneNumbers.beautifyNumber(call.getNumber());
		
		name = name == null || name.trim().isEmpty() ? number : name;
		
		holder.name.setText(name);
		holder.number.setText(number);
		
		Drawable type = ContextCompat.getDrawable(holder.itemView.getContext(), getTypeIcon(call.getType()));
		
		holder.type.setImageDrawable(type);
		holder.speakDuration.setText(Files.formatSeconds(call.getDuration()));
		holder.ringingDuration.setText(Files.formatMilliSeconds(call.getLong(CallKey.RINGING_DURATION, 0L)));
		holder.date.setText(Time.toString(call.getTime(), "d MMMM yyyy HH:mm"));
		
		String letter = getLetter(name);
		int    color  = Colors.getRandomColor();
		
		Colors.setTintDrawable(holder.action.getDrawable(), color);
		
		Drawable image = TextDrawable.builder()
				.beginConfig()
				.useFont(ResourcesCompat.getFont(holder.itemView.getContext(), com.tr.hsyn.resfont.R.font.z))
				.endConfig()
				.buildRound(letter, color);
		
		holder.image.setImageDrawable(image);
		
		if (selectionMode) {
			
			holder.selection.setVisibility(View.VISIBLE);
			holder.action.setVisibility(View.GONE);
			holder.selection.setChecked(selectedCalls.contains(call));
		}
		else {
			
			holder.selection.setVisibility(View.GONE);
			holder.action.setVisibility(View.VISIBLE);
		}
		
	}
	
	@Override
	public long getItemId(int position) {
		
		return calls.get(position).getTime();
	}
	
	@Override
	public int getItemCount() {
		
		if (calls != null) return calls.size();
		
		return 0;
	}
	
	@Override
	public void onViewAttachedToWindow(@NonNull @NotNull Holder holder) {
		
		super.onViewAttachedToWindow(holder);
		
		var selection = holder.selection;
		var action    = holder.action;
		
		if (selectionMode) {
			
			selection.setChecked(selectedCalls.contains(calls.get(holder.getAdapterPosition())));
			
			selection.setVisibility(View.VISIBLE);
			action.setVisibility(View.GONE);
		}
		else {
			
			selection.setVisibility(View.GONE);
			action.setVisibility(View.VISIBLE);
		}
	}
	
	private int getTypeIcon(int type) {
		
		return Calls.getCallTypeIcon(type);
	}
	
	@NonNull
	private String getLetter(String str) {
		
		var l = Stringx.getFirstChar(str);
		
		if (l.isEmpty()) return "?";
		
		if (Character.isAlphabetic(l.charAt(0))) return l.toUpperCase(Locale.ROOT);
		
		return "?";
	}
	
	@NonNull
	@Override
	public String getSectionName(int position) {
		
		return Time.toString(calls.get(position).getTime(), "d MMMM yyyy EEEE");
	}
	
	@Override
	public int getSize() {
		
		return calls.size();
	}
	
	@Override
	public List<Call> getItems() {
		
		return calls;
	}
	
	@SuppressLint("NotifyDataSetChanged")
	@Override
	public void setItems(List<Call> items) {
		
		calls = items;
		notifyDataSetChanged();
	}
	
	@SuppressLint("NotifyDataSetChanged")
	@Override
	public void clearItems() {
		
		calls.clear();
		notifyDataSetChanged();
	}
	
	public final class Holder extends RecyclerView.ViewHolder {
		
		public TextView name, number, speakDuration, ringingDuration, date;
		public ImageView image, type, action;
		public CheckBox selection;
		
		public Holder(@NonNull View itemView) {
			
			super(itemView);
			
			name            = itemView.findViewById(R.id.name);
			number          = itemView.findViewById(R.id.number);
			speakDuration   = itemView.findViewById(R.id.callDuration);
			ringingDuration = itemView.findViewById(R.id.ringingDuration);
			date            = itemView.findViewById(R.id.date);
			image           = itemView.findViewById(R.id.image);
			type            = itemView.findViewById(R.id.type);
			action          = itemView.findViewById(R.id.action);
			selection       = itemView.findViewById(R.id.selection);
			
			/*if (selectionMode) {
				
				selection.setVisibility(View.VISIBLE);
				action.setVisibility(View.GONE);
				selection.setChecked(selectedCalls.contains(calls.get(getAdapterPosition())));
			}
			else {
				
				selection.setVisibility(View.GONE);
				action.setVisibility(View.VISIBLE);
			}*/
			
			action.setOnClickListener(v -> {
				if (onAction != null) onAction.onItemIndex(getAdapterPosition());
			});
			
			var root = itemView.findViewById(R.id.call_item);
			root.setBackgroundResource(themeInfo.getRipple());
			root.setOnClickListener(v -> onSelected(getAdapterPosition()));
			root.setOnLongClickListener(this::onLongClick);
		}
		
		private void onSelected(int position) {
			
			xlog.w("Selection mode : %s", selectionMode);
			
			var call = calls.get(getAdapterPosition());
			
			if (selectionMode) {
				
				var check = !selection.isChecked();
				selection.setChecked(check);
				makeSelection(call, check);
			}
			else {
				
				//- Sadece seçim modu aktif değilse çağrılacak
				selectListener.onItemIndex(position);
			}
		}
		
		private boolean onLongClick(View v) {
			
			xlog.w("Selection mode : %s", selectionMode);
			
			
			var call = calls.get(getAdapterPosition());
			
			if (selectionMode) {
				
				var check = !selection.isChecked();
				selection.setChecked(check);
				makeSelection(call, check);
			}
			else {
				
				makeSelection(call, selectionMode = true);
				selection.setChecked(true);
				//- Sadece seçim modu aktif olursa çağrılacak
				longClickListener.onItemIndex(getAdapterPosition());
			}
			
			return true;
		}
		
	}
}
