package com.tr.hsyn.telefonrehberi.main.call.activity.history;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.calldata.CallType;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.files.Files;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.data.CallKey;
import com.tr.hsyn.textdrawable.TextDrawable;
import com.tr.hsyn.time.Time;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class CallHistoryAdapter extends RecyclerView.Adapter<CallHistoryAdapter.Holder> {
	
	private final List<? extends Call> calls;
	private final ItemIndexListener    deleteListener;
	
	public CallHistoryAdapter(@NotNull List<? extends Call> calls, @NotNull ItemIndexListener deleteListener) {
		
		this.calls          = calls;
		this.deleteListener = deleteListener;
	}
	
	@NonNull
	@Override
	public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		
		return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.call_show_calls, parent, false), deleteListener);
	}
	
	@Override
	public void onBindViewHolder(@NonNull Holder holder, int position) {
		
		Call   call   = calls.get(position);
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
		
		String letter = Stringx.getLetter(name);
		int    color  = Colors.getRandomColor();
		
		Colors.setTintDrawable(holder.action.getDrawable(), color);
		
		Drawable image = TextDrawable.builder()
				.beginConfig()
				.useFont(ResourcesCompat.getFont(holder.itemView.getContext(), com.tr.hsyn.resfont.R.font.z))
				.endConfig()
				.buildRound(letter, color);
		
		holder.image.setImageDrawable(image);
	}
	
	@Override
	public int getItemCount() {
		
		return calls.size();
	}
	
	@SuppressLint("NotifyDataSetChanged")
	public void clear() {
		
		calls.clear();
		notifyDataSetChanged();
	}
	
	private static int getTypeIcon(int type) {
		
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
	
	public static final class Holder extends RecyclerView.ViewHolder {
		
		TextView name, number, speakDuration, ringingDuration, date;
		ImageView image, type, action;
		
		public Holder(@NonNull View itemView, ItemIndexListener actionListener) {
			
			super(itemView);
			
			name            = itemView.findViewById(R.id.name);
			number          = itemView.findViewById(R.id.number);
			speakDuration   = itemView.findViewById(R.id.callDuration);
			ringingDuration = itemView.findViewById(R.id.ringingDuration);
			date            = itemView.findViewById(R.id.date);
			image           = itemView.findViewById(R.id.image);
			type            = itemView.findViewById(R.id.type);
			action          = itemView.findViewById(R.id.action);
			
			itemView.findViewById(R.id.call_item).setBackgroundResource(Colors.getRipple());
			
			action.setOnClickListener(v -> {
				if (actionListener != null) actionListener.onItemIndex(getAdapterPosition());
			});
		}
	}
}
