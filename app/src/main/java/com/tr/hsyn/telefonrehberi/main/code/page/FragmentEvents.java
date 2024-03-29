package com.tr.hsyn.telefonrehberi.main.code.page;


import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tr.hsyn.fastscroller.FastScrollListener;
import com.tr.hsyn.page.SwipeListener;


public abstract class FragmentEvents<T> extends FragmentView<T> implements PageEventListener, SwipeListener, FastScrollListener {
	
	protected FastScrollListener                   fastScrollListener;
	protected SwipeListener                        swipeListener;
	protected SwipeRefreshLayout.OnRefreshListener refreshListener;
	
	protected Runnable onReady;
	
	
	@Override
	public void onFastScrollStart() {
		
		if (fastScrollListener != null) fastScrollListener.onFastScrollStart();
	}
	
	@Override
	public void onFastScrollStop() {
		
		if (fastScrollListener != null) fastScrollListener.onFastScrollStop();
	}
	
	@Override
	public void setSwipeListener(SwipeListener swipeListener) {
		
		this.swipeListener = swipeListener;
	}
	
	@Override
	public void setScrollListener(FastScrollListener listener) {
		
		fastScrollListener = listener;
	}
	
	@Override
	public void setRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
		
		refreshListener = listener;
	}
	
	
	@Override
	public void setOnReady(Runnable onReady) {
		
		this.onReady = onReady;
	}
	
	@Override
	public void onRefresh() {
		
		if (refreshListener != null) refreshListener.onRefresh();
	}
	
	@Override
	public void onSwipe(int index) {
		
		if (swipeListener != null) swipeListener.onSwipe(index);
	}
}
