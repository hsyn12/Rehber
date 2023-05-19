package com.tr.hsyn.telefonrehberi.main.activity.call.search;


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

import com.google.common.base.CharMatcher;
import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.files.Files;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.regex.dev.regex.Regex;
import com.tr.hsyn.regex.dev.regex.character.Character;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.activity.contact.search.TextChangeListener;
import com.tr.hsyn.telefonrehberi.main.code.call.act.Calls;
import com.tr.hsyn.telefonrehberi.main.code.call.cast.CallKey;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.text.Spans;
import com.tr.hsyn.textdrawable.TextDrawable;
import com.tr.hsyn.time.Time;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Holder> implements TextChangeListener {
	
	/**
	 * Tüm aramaların bulunduğu liste
	 */
	private final List<Call>        calls;
	/**
	 * Filtrelenmiş sonuçların bulunduğu liste.
	 * Bu liste adapter'ın ana listedir ve hiçbir filtreleme yoksa tüm kayıtları tutar.
	 */
	private final List<Call>        filteredCalls = new ArrayList<>();
	/**
	 * Liste seçim olayı
	 */
	private final ItemIndexListener selectListener;
	/**
	 * Arama olayı
	 */
	private final ItemIndexListener onActionCall;
	private final int               markColor     = Colors.lighter(Colors.getPrimaryColor(), 0.3f);
	/**
	 * Arama yazısı bir numara ise {@code true} değerini alır
	 */
	private       boolean           isNumber;
	/**
	 * Arama yazısı
	 */
	private       String            searchText    = "";
	/**
	 * Filtreleme işleminin tamamlandığını haber almak isteyen kişi
	 */
	private       Runnable          filterCompleteListener;
	/**
	 * Filtreleme olayının başladığını haber almak isteyen kişi
	 */
	private       Runnable          filterStartListener;
	
	public SearchAdapter(@NotNull List<Call> calls, @NotNull ItemIndexListener selectListener, @NotNull ItemIndexListener onActionCall) {
		
		this.calls          = calls;
		this.selectListener = selectListener;
		this.onActionCall   = onActionCall;
		filteredCalls.addAll(calls);
	}
	
	public void setFilterCompleteListener(Runnable filterCompleteListener) {
		
		this.filterCompleteListener = filterCompleteListener;
	}
	
	public void setFilterStartListener(Runnable filterStartListener) {
		
		this.filterStartListener = filterStartListener;
	}
	
	public List<Call> getCalls() {
		
		return calls;
	}
	
	public List<Call> getFilteredCalls() {
		
		return filteredCalls;
	}
	
	public String getSearchText() {
		
		return searchText;
	}
	
	@SuppressLint("NotifyDataSetChanged")
	@Override
	public void onTextChanged(String text) {
		
		//- Filtreleme işlemi başlıyor
		if (filterStartListener != null) filterStartListener.run();
		
		Runny.run(() -> {
			
			searchText = CharMatcher.whitespace().removeFrom(text);
			
			//- Burada aranan yazıdan boşlukları çıkarmak
			//- işaretleme sırasında herhangi bir etki yapmıyor.
			//- Çünkü işaretleme, görsel elemanların üzerindeki isim ve numara
			//- alınarak tespit ediliyor.
			
			if (searchText.isEmpty()) {
				
				filteredCalls.clear();
				filteredCalls.addAll(calls);
			}
			else {
				
				if (Stringx.test(searchText).isNot(Regex.NUMBER)) {
					
					isNumber = false;
					filteredCalls.clear();
					filteredCalls.addAll(searchByName(searchText.toLowerCase()));
				}
				else {
					
					isNumber = true;
					filteredCalls.clear();
					filteredCalls.addAll(searchByNumber(searchText));
				}
			}
			
			Runny.run(() -> {
				
				notifyDataSetChanged();
				
				if (filterCompleteListener != null) filterCompleteListener.run();
			});
			
		}, false);
	}
	
	private List<Call> searchByName(String searchText) {
		
		return calls.stream().filter(c -> Stringx.isMatch(c.getName() != null ? c.getName() : "", searchText)).collect(Collectors.toList());
	}
	
	private List<Call> searchByNumber(String searchText) {
		
		return calls.stream().filter(c -> Stringx.isMatch(c.getNumber(), searchText)).collect(Collectors.toList());
	}
	
	@NonNull
	@Override
	public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		
		return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.call_item, parent, false), selectListener, onActionCall);
	}
	
	@Override
	public void onBindViewHolder(@NonNull Holder holder, int position) {
		
		Call   call   = filteredCalls.get(position);
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
		
		setHightlight(holder);
	}
	
	private int getTypeIcon(int type) {
		
		return Calls.getCallTypeIcon(type);
	}
	
	@NonNull
	private String getLeter(String str) {
		
		var l = Stringx.getFirstChar(str);
		
		if (l.isEmpty()) return "?";
		
		if (String.valueOf(l.charAt(0)).matches(Character.LETTER))
			return l.toUpperCase(Locale.ROOT);
		
		return "?";
	}
	
	private void setHightlight(Holder holder) {
		
		if (searchText.isEmpty()) {return;}
		
		String name   = holder.name.getText().toString();
		String number = holder.number.getText().toString();
		
		if (!isNumber) {//- Arama sırasında set edilmiş olmalı
			
			Integer[] indexes = Stringx.indexOfMatches(name, searchText);
			
			//xlog.d(Arrays.toString(indexes));
			
			Spanner spanner = new Spanner(name);
			
			for (int i = 0; i < indexes.length - 1; i += 2) {
				
				spanner.setSpans(indexes[i], indexes[i + 1], Spans.background(markColor));
			}
			
			holder.name.setText(spanner);
			holder.number.setText(PhoneNumbers.beautifyNumber(number));
		}
		else {
			
			Integer[] indexes = Stringx.indexOfMatches(number, searchText);
			//xlog.d(Arrays.toString(indexes));
			Spanner spanner = new Spanner(number);
			
			for (int i = 0; i < indexes.length - 1; i += 2) {
				
				spanner.setSpans(indexes[i], indexes[i + 1], Spans.background(markColor));
			}
			
			holder.name.setText(name);
			holder.number.setText(spanner);
		}
		
	}
	
	@Override
	public int getItemCount() {
		
		return filteredCalls.size();
	}
	
	public Call get(int index) {
		
		return filteredCalls.get(index);
	}
	
	protected static final class Holder extends RecyclerView.ViewHolder {
		
		TextView name, number, speakDuration, ringingDuration, date;
		ImageView image, type, action;
		
		public Holder(@NonNull View itemView, ItemIndexListener selectListener, ItemIndexListener onAction) {
			
			super(itemView);
			
			name            = itemView.findViewById(R.id.name);
			number          = itemView.findViewById(R.id.number);
			speakDuration   = itemView.findViewById(R.id.callDuration);
			ringingDuration = itemView.findViewById(R.id.ringingDuration);
			date            = itemView.findViewById(R.id.date);
			image           = itemView.findViewById(R.id.image);
			type            = itemView.findViewById(R.id.type);
			action          = itemView.findViewById(R.id.action);
			
			action.setOnClickListener(v -> {
				if (onAction != null) onAction.onItemIndex(getAdapterPosition());
			});
			
			var root = itemView.findViewById(R.id.call_item);
			
			root.setBackgroundResource(Colors.getRipple());
			
			root.setOnClickListener(v -> selectListener.onItemIndex(getAdapterPosition()));
		}
	}
}
