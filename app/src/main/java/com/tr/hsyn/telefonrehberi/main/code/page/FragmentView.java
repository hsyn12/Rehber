package com.tr.hsyn.telefonrehberi.main.code.page;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tr.hsyn.fastscroller.FastScrollRecyclerView;
import com.tr.hsyn.page.ListPage;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;


public abstract class FragmentView<T> extends ListPage<T> implements IHaveAHeader, SwipeRefreshLayout.OnRefreshListener {
	
	protected Header                 header;
	protected View                   emptyView;
	protected FastScrollRecyclerView recyclerView;
	protected ProgressBar            progressBar;
	protected SwipeRefreshLayout     refreshLayout;
	
	protected abstract int getAdapterSize();
	
	/**
	 * Returns the title of the call log.
	 *
	 * @return the title
	 */
	protected abstract CharSequence getTitle();
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view = super.onCreateView(inflater, container, savedInstanceState);
		
		if (view != null) {
			
			recyclerView  = view.findViewById(R.id.recycler_view);
			progressBar   = view.findViewById(R.id.progress);
			emptyView     = view.findViewById(R.id.empty);
			refreshLayout = view.findViewById(R.id.refresh);
			
			final int distance = 440;
			final int slings   = 330;
			
			refreshLayout.setDistanceToTriggerSync(distance);
			refreshLayout.setSlingshotDistance(slings);
			refreshLayout.setOnRefreshListener(this);
		}
		
		return view;
	}
	
	@Override
	public void showTime(boolean showTime) {
		
		super.showTime(showTime);
		
		if (showTime) {
			
			if (header != null) {
				
				header.setTitle(getTitle());
				header.setSubTitle(String.valueOf(getAdapterSize()));
			}
			else {
				
				xlog.d("Header nesnesi henüz atanmadı");
			}
		}
	}
	
	@Override
	public void setHeader(@NotNull Header header) {
		
		this.header = header;
	}
	
	
}
