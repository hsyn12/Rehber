package com.tr.hsyn.telefonrehberi.main.code.page.calllog;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.fastscroller.FastScrollListener;
import com.tr.hsyn.page.MenuShower;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.activity.city.MainActivity;
import com.tr.hsyn.telefonrehberi.main.code.page.FragmentEvents;
import com.tr.hsyn.xlog.xlog;


/**
 * Görsel olarak hazırlan
 */
public abstract class CallLogView extends FragmentEvents<Call> {
	
	@Override
	protected int getLayoutId() {
		
		return R.layout.fragment_call_log;
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		recyclerView.setFastScrollListener(this);
	}
	
	@Override
	public void onFastScrollStart() {
		
		if (getActivity() instanceof FastScrollListener) {
			
			((FastScrollListener) getActivity()).onFastScrollStart();
		}
	}
	
	@Override
	public void onFastScrollStop() {
		
		if (getActivity() instanceof FastScrollListener) {
			
			((FastScrollListener) getActivity()).onFastScrollStop();
		}
	}
	
	@Override
	public void onRefresh() {
		
		androidx.fragment.app.FragmentActivity activity = getActivity();
		
		if (activity instanceof SwipeRefreshLayout.OnRefreshListener) {
			
			((SwipeRefreshLayout.OnRefreshListener) activity).onRefresh();
		}
		else {
			
			xlog.w("Activity must be SwipeRefreshLayout.OnRefreshListener");
			
			refreshLayout.setRefreshing(false);
		}
	}
	
	@Override
	public void showProgress() {
		
		if (progressBar != null)
			progressBar.setVisibility(View.VISIBLE);
		
	}
	
	@Override
	public void hideProgress() {
		
		progressBar.setVisibility(View.GONE);
		refreshLayout.setRefreshing(false);
	}
	
	protected final MainActivity getMainActivity() {
		
		return (MainActivity) getActivity();
	}
	
	protected final MenuShower getMainMenu() {
		
		return (MenuShower) getActivity();
	}
	
	protected final int getCurrentPage() {
		
		return getMainActivity().getCurrentPage();
	}
	
}
