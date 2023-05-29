package com.tr.hsyn.telefonrehberi.main.call.activity.showcalls;


import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.calldata.CallType;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.files.Files;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.dev.fastadapter.FastAdapter;
import com.tr.hsyn.telefonrehberi.main.call.data.CallKey;
import com.tr.hsyn.textdrawable.TextDrawable;
import com.tr.hsyn.time.Time;

import java.util.List;
import java.util.Locale;


public class ShowCallsAdapter extends FastAdapter<Call> {
	
	private final ItemIndexListener selectListener;
	private final ItemIndexListener actionListener;
	
	public ShowCallsAdapter(
			@NonNull List<Call> calls,
			ItemIndexListener selectListener,
			ItemIndexListener actionListener) {
		
		super(calls);
		this.selectListener = selectListener;
		this.actionListener = actionListener;
	}
	
	
	@Override
	protected FastHolder createHolder(@NonNull ViewGroup parent, int viewType) {
		
		return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.call_show_calls, parent, false), selectListener, actionListener);
	}
	
	@Override
	protected void bindHolder(@NonNull FastHolder _holder, int position) {
		
		Holder holder = (Holder) _holder;
		
		Call   call   = list.get(position);
		String name   = call.getName();
		String number = PhoneNumbers.beautifyNumber(call.getNumber());
		
		name = name == null || name.trim().isEmpty() ? number : name;
		
		holder.name.setText(name);
		holder.number.setText(number);
		
		Drawable type = ContextCompat.getDrawable(holder.itemView.getContext(), getTypeIcon(call.getCallType()));
		
		holder.type.setImageDrawable(type);
		holder.speakDuration.setText(Files.formatSeconds(call.getDuration()));
		holder.ringingDuration.setText(Files.formatMilliSeconds(call.getLong(CallKey.RINGING_DURATION, 0L)));
		holder.date.setText(Time.toString(call.getTime(), "d MMMM yyyy HH:mm"));
		
		String letter = getLeter(name);
		int    color  = Colors.getRandomColor();
		
		Colors.setTintDrawable(holder.action.getDrawable(), color);
		
		Drawable image = TextDrawable.builder()
				.beginConfig()
				.useFont(ResourcesCompat.getFont(holder.itemView.getContext(), com.tr.hsyn.resfont.R.font.z))
				.endConfig()
				.buildRound(letter, color);
		
		holder.image.setImageDrawable(image);
	}
	
	private int getTypeIcon(int type) {
		
		switch (type) {
			
			case CallType.INCOMING:
			case CallType.INCOMING_WIFI:
				return R.drawable.incoming_call;
			case CallType.OUTGOING:
			case CallType.OUTGOING_WIFI:
				return R.drawable.outgoing_call;
			case CallType.MISSED:
				return R.drawable.missed_call;
			case CallType.REJECTED:
				return R.drawable.rejected_call;
			case CallType.BLOCKED:
				return R.drawable.blocked_call;
			case CallType.GET_REJECTED:
				return R.drawable.get_rejected_call;
			case CallType.UNREACHED:
				return R.drawable.un_reached_call;
			case CallType.UNRECEIVED:
				return R.drawable.un_recieved_call;
		}
		
		return R.drawable.incoming_call;
	}
	
	@NonNull
	private String getLeter(String str) {
		
		var l = Stringx.getFirstChar(str);
		
		if (l.isEmpty()) return "?";
		
		if (Character.isAlphabetic(l.charAt(0))) return l.toUpperCase(Locale.ROOT);
		
		return "?";
	}
	
	public static final class Holder extends FastHolder {
		
		TextView name, number, speakDuration, ringingDuration, date;
		ImageView image, type, action;
		
		public Holder(@NonNull View itemView, ItemIndexListener selectListener, ItemIndexListener actionListener) {
			
			super(itemView, selectListener);
			
			name            = itemView.findViewById(R.id.name);
			number          = itemView.findViewById(R.id.number);
			speakDuration   = itemView.findViewById(R.id.callDuration);
			ringingDuration = itemView.findViewById(R.id.ringingDuration);
			date            = itemView.findViewById(R.id.date);
			image           = itemView.findViewById(R.id.image);
			type            = itemView.findViewById(R.id.type);
			action          = itemView.findViewById(R.id.action);
			
			action.setOnClickListener(v -> {
				if (actionListener != null) actionListener.onItemIndex(getAdapterPosition());
			});
		}
		
		@Override
		protected View getRoot() {
			
			return itemView.findViewById(R.id.call_item);
		}
	}
}
