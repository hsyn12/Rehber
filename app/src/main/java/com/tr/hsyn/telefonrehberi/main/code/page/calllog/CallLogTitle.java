package com.tr.hsyn.telefonrehberi.main.code.page.calllog;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.telefonrehberi.main.code.page.adapter.CallAdapter;
import com.tr.hsyn.telefonrehberi.main.data.Res;


/**
 * Başlığı ve alt başlığı yönet
 */
public abstract class CallLogTitle extends CallLogView {
	
	protected String[]     filters;
	private   CharSequence title;
	
	protected void setSubTitle(CharSequence subTitle) {
		
		if (isShowTime())
			header.setSubTitle(subTitle);
	}
	
	@Override
	public CharSequence getTitle() {
		
		return title;
	}
	
	public void setTitle(CharSequence title) {
		
		if (isShowTime())
			header.setTitle(title);
		
		this.title = title;
	}
	
	public void updateSubTitle() {
		
		if (isShowTime())
			header.setSubTitle(String.valueOf(getAdapter().getSize()));
	}
	
	/**
	 * Filtreleme seçeneği, ana listeden farklı listeler oluşturduğu için,
	 * o an geçerli olan liste adapter üzerinde olan listedir.
	 *
	 * @return Adapter
	 */
	protected abstract CallAdapter getAdapter();
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		
		filters = getResources().getStringArray(com.tr.hsyn.callfilter.R.array.call_filter_items);
		title   = getFilterName(Res.Calls.FILTER_ALL);
		super.onViewCreated(view, savedInstanceState);
	}
	
	public String getFilterName(int filter) {
		
		if (filters != null)
			return filters[filter];
		
		return "";
	}
	
	@Override
	public String getFilterName() {
		
		if (filters != null)
			return filters[getFilter()];
		
		return "";
	}
	
	protected abstract int getFilter();
	
	
}
