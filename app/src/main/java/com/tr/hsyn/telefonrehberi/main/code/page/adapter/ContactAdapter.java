package com.tr.hsyn.telefonrehberi.main.code.page.adapter;


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

import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.colors.ColorHolder;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.fastscroller.FastScrollRecyclerView;
import com.tr.hsyn.perfectsort.PerfectSort;
import com.tr.hsyn.rsectiondecorator.SectionsAdapterInterface;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.cast.ItemAdapter;
import com.tr.hsyn.textdrawable.TextDrawable;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.Holder> implements FastScrollRecyclerView.SectionedAdapter, ItemAdapter<Contact>, SectionsAdapterInterface {
	
	private final ItemIndexListener        selectListener;
	private final ColorHolder              colorHolder;
	private       List<Contact>            contacts;
	private       LayoutInflater           inflater;
	private       SectionsAdapterInterface secAdapter;
	
	public ContactAdapter(@NonNull List<Contact> contacts, @NonNull ItemIndexListener selectListener, ColorHolder colorHolder) {
		
		this.contacts       = contacts;
		this.selectListener = selectListener;
		this.colorHolder    = colorHolder;
		setHasStableIds(true);
		setSections();
	}
	
	private void setSections() {
		
		secAdapter = new TopSectionAdapter(contacts.stream().map(Contact::getName).collect(Collectors.toList()));
	}
	
	@NonNull
	@Override
	public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		
		if (inflater != null)
			return new Holder(inflater.inflate(R.layout.contact, parent, false), selectListener, colorHolder);
		
		inflater = LayoutInflater.from(parent.getContext());
		
		return new Holder(inflater.inflate(R.layout.contact, parent, false), selectListener, colorHolder);
	}
	
	@Override
	public void onBindViewHolder(@NonNull Holder holder, int position) {
		
		Contact contact = contacts.get(position);
		
		String name = contact.getName();
		
		if (name == null || name.trim().isEmpty()) {
			
			name = "(adsız)";
		}
		
		holder.name.setText(name);
		
		String pic   = contact.getPic();
		int    color = Colors.getRandomColor();
		
		if (pic == null) {
			
			Drawable image = TextDrawable.builder()
					.beginConfig()
					.useFont(ResourcesCompat.getFont(holder.itemView.getContext(), com.tr.hsyn.resfont.R.font.z))
					.endConfig()
					.buildRound(getLetter(contact.getName()), color);
			
			holder.image.setImageDrawable(image);
		}
		else {
			
			holder.image.setImageURI(Uri.parse(pic));
		}
	}
	
	@Override
	public long getItemId(int position) {
		
		return Long.parseLong(String.valueOf(contacts.get(position).getContactId()));
	}
	
	@Override
	public int getItemCount() {
		
		if (contacts != null) return contacts.size();
		
		return 0;
	}
	
	@NonNull
	private String getLetter(String str) {
		
		@org.jetbrains.annotations.NotNull String l = Stringx.getFirstChar(str);
		
		if (l.isEmpty()) return "?";
		
		if (Character.isAlphabetic(l.charAt(0))) return l.toUpperCase(Locale.ROOT);
		
		return "?";
	}
	
	@NonNull
	@Override
	public String getSectionName(int position) {return getLetter(contacts.get(position).getName());}
	
	@Override
	public int getSize() {
		
		return contacts.size();
	}
	
	@Override
	public List<Contact> getItems() {
		
		return contacts;
	}
	
	@SuppressLint("NotifyDataSetChanged")
	@Override
	public void setItems(List<Contact> items) {
		
		contacts = items;
		setSections();
		notifyDataSetChanged();
	}
	
	@Override
	public void clearItems() {
		
	}
	
	@Override
	public int getSectionsCount() {
		
		return secAdapter.getSectionsCount();
	}
	
	@NonNull
	@Override
	public String getSectionTitleAt(int sectionIndex) {
		
		return secAdapter.getSectionTitleAt(sectionIndex);
	}
	
	@Override
	public int getItemCountForSection(int sectionIndex) {
		
		return secAdapter.getItemCountForSection(sectionIndex);
	}
	
	public static class Holder extends RecyclerView.ViewHolder {
		
		TextView  name;
		ImageView image;
		
		public Holder(@NonNull View itemView, ItemIndexListener selectListener, @NonNull ColorHolder colorHolder) {
			
			super(itemView);
			
			name  = itemView.findViewById(R.id.contact_name);
			image = itemView.findViewById(R.id.contact_image);
			
			itemView.setBackgroundResource(colorHolder.getRipple());
			itemView.setOnClickListener(v -> selectListener.onItemIndex(getAdapterPosition()));
		}
	}
	
	private static final class TopSectionAdapter implements SectionsAdapterInterface {
		
		private Map<String, List<String>> sections;
		
		public TopSectionAdapter(List<String> items) {
			
			setSections(items);
		}
		
		private void setSections(List<String> items) {
			
			sections = new HashMap<>();
			
			for (String n : items) {
				
				String m = Stringx.trimWhiteSpaces(n);
				
				if (!m.isEmpty()) {
					
					String L = String.valueOf(Stringx.toUpper(m).charAt(0));
					
					List<String> list = sections.get(L);
					
					if (list != null) list.add(n);
					else sections.put(L, Lister.listOf(n));
				}
				else {
					
					List<String> list = sections.get("?");
					
					if (list != null) list.add(n);
					else sections.put("?", Lister.listOf(n));
				}
			}
		}
		
		public void setItems(List<String> items) {
			
			setSections(items);
		}
		
		@Override
		public int getSectionsCount() {
			
			return sections.size();
		}
		
		@NonNull
		@Override
		public String getSectionTitleAt(int sectionIndex) {
			
			return PerfectSort.sort(Lister.listOf(sections.keySet())).get(sectionIndex);
		}
		
		@Override
		public int getItemCountForSection(int sectionIndex) {
			
			String key = Lister.listOf(sections.keySet()).get(sectionIndex);
			
			List<String> sec = sections.get(key);
			
			return sec != null ? sec.size() : 0;
		}
	}
	
}
