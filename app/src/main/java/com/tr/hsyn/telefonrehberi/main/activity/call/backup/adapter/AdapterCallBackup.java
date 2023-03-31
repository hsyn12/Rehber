package com.tr.hsyn.telefonrehberi.main.activity.call.backup.adapter;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.dev.backup.Backup;
import com.tr.hsyn.time.Time;

import java.util.ArrayList;
import java.util.List;


public final class AdapterCallBackup extends RecyclerView.Adapter<AdapterCallBackup.Holder> {
	
	private final ItemIndexListener  selectListener;
	private final ItemIndexListener  deleteListener;
	private       List<Backup<Call>> callBackups = new ArrayList<>();
	
	public AdapterCallBackup(@NonNull ItemIndexListener selectListener, @NonNull ItemIndexListener deleteListener) {
		
		this.selectListener = selectListener;
		this.deleteListener = deleteListener;
	}
	
	public List<Backup<Call>> getCallBackups() {
		
		return callBackups != null ? callBackups : new ArrayList<>(0);
	}
	
	@SuppressLint("NotifyDataSetChanged")
	public void setCallBackups(@NonNull List<Backup<Call>> callBackups) {
		
		this.callBackups = callBackups;
		notifyDataSetChanged();
	}
	
	public void addBackup(@NonNull Backup<Call> backup) {
		
		if (!callBackups.isEmpty()) {
			
			this.callBackups.add(0, backup);
			notifyItemInserted(0);
		}
		else {
			
			this.callBackups.add(backup);
			notifyItemInserted(getCallBackups().size() - 1);
		}
		
	}
	
	public void removeBackup(int index) {
		
		if (Lister.isIndex(this.callBackups, index)) {
			
			this.callBackups.remove(index);
			
			notifyItemRemoved(index);
		}
	}
	
	public Backup<Call> getBackup(int index) {
		
		return callBackups.get(index);
	}
	
	@NonNull
	@Override
	public final Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		
		return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.backup_item, parent, false), selectListener, deleteListener);
	}
	
	@Override
	public final void onBindViewHolder(@NonNull Holder holder, int position) {
		
		holder.bind(callBackups.get(position));
	}
	
	@Override
	public final int getItemCount() {
		
		return callBackups.size();
	}
	
	static final class Holder extends RecyclerView.ViewHolder {
		
		final TextView  name;
		final TextView  date;
		final TextView  size;
		final ImageView iconDelete;
		
		public Holder(@NonNull View itemView, @NonNull ItemIndexListener selectListener, @NonNull ItemIndexListener deleteListener) {
			
			super(itemView);
			
			name       = itemView.findViewById(R.id.text_name_backup);
			date       = itemView.findViewById(R.id.text_date_backup);
			size       = itemView.findViewById(R.id.text_size);
			iconDelete = itemView.findViewById(R.id.icon_delete);
			
			var root = itemView.findViewById(R.id.root_backup);
			
			root.setBackgroundResource(Colors.getRipple());
			root.setOnClickListener(v -> selectListener.onItemIndex(getAdapterPosition()));
			iconDelete.setOnClickListener(v -> deleteListener.onItemIndex(getAdapterPosition()));
		}
		
		void bind(@NonNull Backup<Call> callBackup) {
			
			name.setText(callBackup.getName());
			date.setText(Time.toString(callBackup.getDate(), "d MMMM yyyy HH:mm"));
			size.setText(String.valueOf(callBackup.getSize()));
		}
		
	}
	
}
