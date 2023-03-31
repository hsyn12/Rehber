package com.tr.hsyn.searchview.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.searchview.R;
import com.tr.hsyn.searchview.model.BaseElement;
import com.tr.hsyn.searchview.model.Filter;
import com.tr.hsyn.searchview.model.Section;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class SelectFilterRvAdapter extends RecyclerView.Adapter<SelectFilterRvAdapter.BaseViewHolder> implements Filterable {
	
	private final boolean           isContain;
	private final Context           mCtx;
	private final LayoutInflater    mInflater;
	private       List<BaseElement> mList;
	private       List<BaseElement> mListFiltered;
	
	public SelectFilterRvAdapter(Context context, boolean isContain) {
		
		this.mCtx      = context;
		this.mInflater = LayoutInflater.from(context);
		mList          = new ArrayList<>();
		mListFiltered  = mList;
		setHasStableIds(false);
		this.isContain = isContain;
	}
	
	@SuppressLint("NotifyDataSetChanged")
	public void addElement(BaseElement element) {
		
		mList.add(element);
		notifyDataSetChanged();
	}
	
	@SuppressLint("NotifyDataSetChanged")
	public void swapList(List<BaseElement> list) {
		
		mList         = list;
		mListFiltered = list;
		notifyDataSetChanged();
	}
	
	public BaseElement getItem(int position) {
		
		return mListFiltered.get(position);
	}
	
	@NonNull @Override
	public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		
		View           view;
		BaseViewHolder holder;
		
		switch (viewType) {
			case 0:
				view = mInflater.inflate(R.layout.msv_select_filter_section, parent, false);
				holder = new SectionViewHolder(view);
				return holder;
			
			case 1:
				view = mInflater.inflate(R.layout.msv_select_filter_item, parent, false);
				holder = new ItemViewHolder(view);
				return holder;
		}
		
		throw new IllegalArgumentException();
	}
	
	@Override
	public void onBindViewHolder(BaseViewHolder holder, int position) {
		
		final BaseElement element = mListFiltered.get(position);
		
		holder.bind(element);
	}
	
	@Override
	public int getItemCount() {
		
		if (mListFiltered == null) {
			return 0;
		}
		return mListFiltered.size();
	}
	
	@Override
	public int getItemViewType(int position) {
		
		BaseElement baseElement = mListFiltered.get(position);
		
		if (baseElement instanceof Section) {
			return 0;
		}
		
		return 1;
	}
	
	@Override
	public android.widget.Filter getFilter() {
		
		return new SelectFilter();
	}
	
	static class BaseViewHolder extends RecyclerView.ViewHolder {
		
		public BaseViewHolder(View itemView) {
			
			super(itemView);
		}
		
		public void bind(BaseElement element) {}
	}
	
	static class ItemViewHolder extends BaseViewHolder {
		
		TextView        mTextView;
		CircleImageView mCiv;
		ImageView       mIv;
		
		public ItemViewHolder(View itemView) {
			
			super(itemView);
			mTextView = itemView.findViewById(R.id.tv_name);
			mCiv      = itemView.findViewById(R.id.civ_icon);
			mIv       = itemView.findViewById(R.id.iv_icon);
		}
		
		@Override
		public void bind(BaseElement element) {
			
			mTextView.setText(element.getName());
			
			Filter filter = (Filter) element;
			
			// Colore
			int color = filter.getIconBgColor();
			
			if (color != View.NO_ID) {
				// mCiv.setBackgroundColor();
				mIv.setVisibility(View.GONE);
				mCiv.setVisibility(View.VISIBLE);
				
				//mCiv.setFillColor(color);
				if (filter.hasIconRefId()) {
					mCiv.setImageResource(filter.getIconRefId());
				}
				else {
					mCiv.setImageDrawable(filter.getIconDrawable());
				}
			}
			else {
				mIv.setVisibility(View.VISIBLE);
				mCiv.setVisibility(View.GONE);
				
				if (filter.hasIconRefId()) {
					mIv.setImageResource(filter.getIconRefId());
				}
				else {
					mIv.setImageDrawable(filter.getIconDrawable());
				}
			}
		}
	}
	
	static class SectionViewHolder extends BaseViewHolder {
		
		TextView mTextView;
		
		public SectionViewHolder(View itemView) {
			
			super(itemView);
			mTextView = (TextView) itemView;
		}
		
		@Override
		public void bind(BaseElement element) {
			
			mTextView.setText(element.getName());
			
			// Set del icona
		}
	}
	
	class SelectFilter extends android.widget.Filter {
		
		@Override
		protected FilterResults performFiltering(CharSequence charSequence) {
			
			if (mList == null) {
				return null;
			}
			
			FilterResults result = new FilterResults();
			
			List<BaseElement> filteredList;
			
			if (!TextUtils.isEmpty(charSequence)) {
				Section sezione = null;
				filteredList = new ArrayList<>();
				for (BaseElement filter : mList) {
					
					if (filter instanceof Filter) {
						final String charSequenceLowerCase = charSequence.toString().toLowerCase();
						final String filterNameLowerCase   = filter.getName().toLowerCase();
						
						if ((!isContain && filterNameLowerCase.startsWith(charSequenceLowerCase))
						    || isContain && filterNameLowerCase.contains(charSequenceLowerCase)) {
							if (sezione != null) {
								filteredList.add(sezione);
								sezione = null;
							}
							filteredList.add(filter);
						}
					}
					else {
						sezione = (Section) filter;
					}
				}
			}
			else {
				filteredList = mList;
			}
			
			result.values = filteredList;
			
			return result;
		}
		
		@SuppressWarnings("unchecked")
		@SuppressLint("NotifyDataSetChanged")
		@Override
		protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
			
			mListFiltered = (List<BaseElement>) filterResults.values;
			
			notifyDataSetChanged();
		}
	}
}
