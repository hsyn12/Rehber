package com.tr.hsyn.telefonrehberi.main.contact.activity.search;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.CharMatcher;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.contact.data.ContactKey;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.text.Spans;
import com.tr.hsyn.textdrawable.TextDrawable;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ContactSearchAdapter extends RecyclerView.Adapter<ContactSearchAdapter.Holder> implements TextChangeListener {
	
	private final List<Contact>     contacts;
	private final ItemIndexListener selectListener;
	private final int               markColor        = Colors.lighter(Colors.getPrimaryColor(), 0.2f);
	private       List<Contact>     filteredContacts = new ArrayList<>();
	private       String            searchText       = "";
	private       boolean           isNumber;
	private       Runnable          filterCompleteListener;
	private       Runnable          filterStartListener;
	
	public ContactSearchAdapter(@NotNull List<Contact> contacts, ItemIndexListener selectListener) {
		
		this.contacts       = contacts;
		this.selectListener = selectListener;
		filteredContacts.addAll(contacts);
	}
	
	@NonNull
	@Override
	public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		
		return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_search, parent, false), selectListener);
	}
	
	@Override
	public void onBindViewHolder(@NonNull Holder holder, int position) {
		
		Contact contact = filteredContacts.get(position);
		
		String name = contact.getName();
		
		if (name == null || name.trim().isEmpty()) {
			
			name = "(adsız)";
		}
		
		holder.name.setText(name);
		
		List<String> numbers = contact.getData(ContactKey.NUMBERS);
		
		if (numbers != null && !numbers.isEmpty()) {
			
			holder.number.setVisibility(View.VISIBLE);
			holder.number.setText(numbers.get(0));
		}
		else {
			
			holder.number.setVisibility(View.GONE);
		}
		
		
		String pic   = contact.getPic();
		int    color = Colors.getRandomColor();
		
		if (pic == null) {
			
			Drawable image = TextDrawable.builder()
					.beginConfig()
					.useFont(ResourcesCompat.getFont(holder.itemView.getContext(), com.tr.hsyn.resfont.R.font.z))
					.endConfig()
					.buildRound(Stringx.getLetter(contact.getName()), color);
			
			holder.image.setImageDrawable(image);
		}
		else {
			
			holder.image.setImageURI(Uri.parse(pic));
		}
		
		setHighlight(holder);
	}
	
	@Override
	public int getItemCount() {
		
		return filteredContacts.size();
	}
	
	@SuppressLint("NotifyDataSetChanged")
	@Override
	public void onTextChanged(String text) {
		
		if (filterStartListener != null) filterStartListener.run();
		
		Runny.run(() -> {
			
			searchText = CharMatcher.whitespace().removeFrom(text);
			
			if (searchText.isEmpty()) {
				
				filteredContacts.clear();
				filteredContacts.addAll(contacts);
			}
			else {
				
				if (Stringx.isNotNumber(searchText)) {
					
					isNumber         = false;
					filteredContacts = searchByName(searchText.toLowerCase());
				}
				else {
					isNumber         = true;
					filteredContacts = searchByNumber(searchText);
				}
			}
			
			Runny.run(() -> {
				
				notifyDataSetChanged();
				
				if (filterCompleteListener != null) filterCompleteListener.run();
			});
		}, false);
	}
	
	public List<Contact> getFilteredContacts() {
		
		return filteredContacts;
	}
	
	public void setFilterCompleteListener(Runnable filterCompleteListener) {
		
		this.filterCompleteListener = filterCompleteListener;
	}
	
	public void setFilterStartListener(Runnable filterStartListener) {
		
		this.filterStartListener = filterStartListener;
	}
	
	private void setHighlight(Holder holder) {
		
		if (searchText.isEmpty()) {return;}
		
		String name   = holder.name.getText().toString();
		String number = holder.number.getText().toString();
		
		if (!isNumber) {
			
			var indexes = Stringx.findIndexes(name, searchText);
			
			//xlog.d(Arrays.toString(indexes));
			
			Spanner spanner = new Spanner(name);
			
			for (var index : indexes) {
				
				spanner.setSpans(index.start, index.end, Spans.background(markColor));
			}
			
			holder.name.setText(spanner);
			holder.number.setText(PhoneNumbers.beautifyNumber(number));
		}
		else {
			
			var     indexes = Stringx.findIndexes(number, searchText);
			Spanner spanner = new Spanner(number);
			
			for (var index : indexes) {
				
				spanner.setSpans(index.start, index.end, Spans.background(markColor));
			}
			
			holder.name.setText(name);
			holder.number.setText(spanner);
		}
		
	}
	
	private List<Contact> searchByName(String searchText) {
		
		return contacts.stream()
				.filter(c -> c.getName() != null)
				.filter(c -> Stringx.isMatch(c.getName(), searchText))
				.collect(Collectors.toList());
	}
	
	@SuppressWarnings("ConstantConditions")
	private List<Contact> searchByNumber(String searchText) {
		
		xlog.d("searchByNumber: " + searchText);
		return contacts.stream().filter(c -> c.getData(ContactKey.NUMBERS) != null && !ContactKey.getNumbers(c).isEmpty() && PhoneNumbers.containsNumber(ContactKey.getNumbers(c), searchText)).collect(Collectors.toList());
	}
	
	static final class Holder extends RecyclerView.ViewHolder {
		
		TextView  name;
		TextView  number;
		ImageView image;
		
		public Holder(@NonNull View itemView, @NotNull ItemIndexListener selectListener) {
			
			super(itemView);
			
			name   = itemView.findViewById(R.id.text_name);
			number = itemView.findViewById(R.id.text_number);
			image  = itemView.findViewById(R.id.image_contact_search);
			
			itemView.setBackgroundResource(Colors.getRipple());
			
			itemView.setOnClickListener(v -> selectListener.onItemIndex(getAdapterPosition()));
		}
	}
}
