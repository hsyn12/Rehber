package com.tr.hsyn.telefonrehberi.main.code.page.contacts;


import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tr.hsyn.fastscroller.FastScrollListener;
import com.tr.hsyn.page.Preparation;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.telefonrehberi.main.cast.PageOwner;


public interface PageEventListener extends Preparation {
	
	// void setSwipeListener(SwipeListener swipeListener);
	
	void setItemSelectListener(ItemIndexListener listener);
	
	void setScrollListener(FastScrollListener listener);
	
	void setRefreshListener(SwipeRefreshLayout.OnRefreshListener listener);
	
	void setPageOwner(PageOwner pageOwner);
	
}
