package com.tr.hsyn.telefonrehberi.main.code.page.adapter;


import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.fastscroller.FastScrollRecyclerView;
import com.tr.hsyn.perfectsort.PerfectSort;
import com.tr.hsyn.rsectiondecorator.SectionsAdapterInterface;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.Res;
import com.tr.hsyn.telefonrehberi.main.cast.ListAdapter;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.Holder> implements FastScrollRecyclerView.SectionedAdapter, ListAdapter<Contact>, SectionsAdapterInterface {
	
	private final ItemIndexListener        selectListener;
	private final List<CallRank>           callRanks;
	private       List<Contact>            contacts;
	private       LayoutInflater           inflater;
	private       SectionsAdapterInterface secAdapter;
	private       boolean                  detailed;
	
	public ContactAdapter(@NonNull List<Contact> contacts, @NonNull ItemIndexListener selectListener) {
		
		this.contacts       = contacts;
		this.selectListener = selectListener;
		callRanks           = null;
		setHasStableIds(true);
		setSections();
	}
	
	public ContactAdapter(@NonNull ItemIndexListener selectListener, @NonNull List<CallRank> contacts) {
		
		this.contacts       = contacts.stream().map(CallRank::getContact).collect(Collectors.toList());
		this.selectListener = selectListener;
		callRanks           = contacts;
		setHasStableIds(true);
		setSections();
	}
	
	@NonNull
	@Override
	public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		
		if (inflater != null)
			return new Holder(inflater.inflate(R.layout.contact, parent, false), selectListener);
		
		inflater = LayoutInflater.from(parent.getContext());
		
		return new Holder(inflater.inflate(R.layout.contact, parent, false), selectListener);
	}
	
	@Override
	public void onBindViewHolder(@NonNull Holder holder, int position) {
		
		Contact contact = contacts.get(position);
		String  name    = contact.getName();
		
		if (name == null || name.trim().isEmpty())
			name = holder.itemView.getContext().getString(R.string.no_name);
		
		holder.name.setText(name);
		
		String pic = contact.getPic();
		
		if (pic == null)
			holder.image.setImageDrawable(Res.textDrawable(holder.itemView.getContext(), contact.getName()));
		else holder.image.setImageURI(Uri.parse(pic));
		
		if (detailed)
			setDetails(holder, position, contact);
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
	@Override
	public String getSectionName(int position) {return Stringx.getLetter(contacts.get(position).getName());}
	
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
	
	private void setDetails(Holder holder, int position, Contact contact) {
		//Warn: aynı sayıda kişi olacak mı?
		CallRank rank = callRanks.get(position);
		
	}
	
	public boolean isDetailed() {
		
		return detailed;
	}
	
	public void setDetailed(boolean detailed) {
		
		this.detailed = detailed;
	}
	
	private void setSections() {
		
		secAdapter = new TopSectionAdapter(contacts.stream().map(Contact::getName).collect(Collectors.toList()));
	}
	
	public static class Holder extends RecyclerView.ViewHolder {
		
		TextView  name;
		ImageView image;
		ViewGroup details;
		
		public Holder(@NonNull View itemView, ItemIndexListener selectListener) {
			
			super(itemView);
			
			name  = itemView.findViewById(R.id.name);
			image = itemView.findViewById(R.id.image);
			
			itemView.setBackgroundResource(Colors.getRipple());
			itemView.setOnClickListener(v -> selectListener.onItemIndex(getAdapterPosition()));
		}
	}
	
	private static final class TopSectionAdapter implements SectionsAdapterInterface {
		
		private Map<String, List<String>> sections;
		
		public TopSectionAdapter(List<String> items) {
			
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
		
		private void setSections(List<String> items) {
			
			sections = new HashMap<>();
			
			for (String n : items) {
				
				String m = Stringx.removeAllWhiteSpaces(n);
				
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
	}
	
}
