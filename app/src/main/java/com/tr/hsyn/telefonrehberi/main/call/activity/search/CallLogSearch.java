package com.tr.hsyn.telefonrehberi.main.call.activity.search;


import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.activity.ActivityView;
import com.tr.hsyn.bungee.Bungee;
import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.gate.AutoGate;
import com.tr.hsyn.gate.DigiGate;
import com.tr.hsyn.gate.Gate;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.page.SwipeListener;
import com.tr.hsyn.searchview.MaterialSearchView;
import com.tr.hsyn.searchview.OnSearchViewListener;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.dev.ResourceUtil;
import com.tr.hsyn.telefonrehberi.dev.android.ui.swipe.SwipeCallBack;
import com.tr.hsyn.telefonrehberi.main.call.story.CallStory;
import com.tr.hsyn.telefonrehberi.main.dev.tost.Tost;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xlog.xlog;

import java.util.List;
import java.util.Objects;


/**
 * Arama kayıtları üzerinde arama yapmayı sağlar.
 * Arama kayıtlarını {@link Key#CALL_LOG_FILTER} anahtarı ile alır.
 *
 * @see Blue
 * @see Key
 */
public class CallLogSearch extends ActivityView implements OnSearchViewListener, SwipeListener {
	
	protected final long               TIME_PASS_INTERVAL = 1000L;
	protected final Gate               keepItemSelection  = AutoGate.newGate(TIME_PASS_INTERVAL);
	private final   DigiGate           gateFilter         = (DigiGate) DigiGate.newGate(1500L).loop();
	protected       CallStory          callStory;
	protected       CallLogSearchInfo  info;
	protected       List<Call>         calls;
	protected       MaterialSearchView searchView;
	protected       RecyclerView       list;
	protected       ProgressBar        progressBar;
	protected       View               emptyView;
	protected       SearchAdapter      searchAdapter;
	protected       String             searchText;
	protected       View               infoLayout;
	protected       TextView           info_1;
	protected       TextView           info_2;
	
	@Override
	protected int getLayoutId() {
		
		return R.layout.activity_call_log_search;
	}
	
	@Override
	protected void onCreate() {
		
		searchView  = findView(R.id.search_call_log);
		list        = findView(R.id.list_search_call);
		progressBar = findView(R.id.progress_search_call);
		emptyView   = findView(R.id.empty);
		infoLayout  = findView(R.id.layout_info);
		info_1      = infoLayout.findViewById(R.id.text_search_info_1);
		info_2      = infoLayout.findViewById(R.id.text_search_info_2);
		
		info      = Objects.requireNonNull(Blue.getObject(Key.CALL_LOG_FILTER));
		callStory = Objects.requireNonNull(Blue.getObject(Key.CALL_STORY));
		calls     = Objects.requireNonNull(info).getCalls();
		
		SwipeCallBack swipeCallBack = new SwipeCallBack(ItemTouchHelper.LEFT, this, ResourceUtil.getBitmap(this, R.drawable.delete_white));
		swipeCallBack.setBgColor(Colors.getPrimaryColor());
		
		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallBack);
		
		itemTouchHelper.attachToRecyclerView(list);
		
		searchView.setOnSearchViewListener(this);
		
		Colors.changeStatusBarColor(this);
		Colors.setProgressColor(progressBar);
		
		list.setAdapter(searchAdapter = new SearchAdapter(calls, this::onItemSelected, this::onItemAction));
		
		updateEmpty();
		
		searchAdapter.setFilterStartListener(() -> progressBar.setVisibility(View.VISIBLE));
		
		searchAdapter.setFilterCompleteListener(this::onSearchCompleted);
		searchView.showSearch();
		
	}
	
	protected void updateEmpty() {
		
		emptyView.setVisibility(getAdapterCalls().isEmpty() ? View.VISIBLE : View.GONE);
	}
	
	protected List<Call> getAdapterCalls() {
		
		return searchAdapter.getFilteredCalls();
	}
	
	private void onSearchCompleted() {
		
		if (!searchAdapter.getFilteredCalls().isEmpty()) {
			
			Tost.show(getString(R.string.search_result, searchAdapter.getFilteredCalls().size()));
		}
		
		progressBar.setVisibility(View.GONE);
		updateEmpty();
	}
	
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		
		Bungee.zoomFast(this);
	}
	
	private void onItemAction(int index) {
		
		if (keepItemSelection.enter()) {
			
			xlog.d("Call to : %s", searchAdapter.get(index));
		}
	}
	
	private void onItemSelected(int index) {
		
		if (keepItemSelection.enter()) {
			
			xlog.d("Selected : %s", searchAdapter.get(index));
		}
	}
	
	@Override
	public void onSearchViewShown() {}
	
	@Override
	public void onSearchViewClosed() {
		
		onBackPressed();
	}
	
	@Override
	public boolean onQueryTextSubmit(String query) {return true;}
	
	@Override
	public void onQueryTextChange(String newText) {
		
		if (newText == null) return;
		
		searchText = newText;
		
		gateFilter.enter(this::filter);
	}
	
	@Override
	public void onSwipe(int index) {
		
		//- Arama kayıtlarında bu olay kaydın silinmesi ile sonuçlanır
		
		var call = searchAdapter.get(index);
		searchAdapter.getFilteredCalls().remove(index);
		searchAdapter.notifyItemRemoved(index);
		
		int _index = calls.indexOf(call);
		
		if (_index != -1) {
			
			calls.remove(_index);
			
			Runny.run(() -> {
				
				if (callStory.delete(call)) {
					
					xlog.d("Silindi : %s", Stringx.overWrite(call.getNumber()));
				}
				else {
					
					xlog.d("Arama kaydı listeden çıkarıldı ancak silinemedi");
				}
				
				//- Bu olay ana listeyi tam olarak düzenler
				Blue.box(Key.REFRESH_CALL_LOG, true);
			}, false);
		}
		else {
			
			xlog.d("Silinecek arama kaydı bulunamadı : %s", Stringx.overWrite(call.getNumber()));
		}
		
		updateEmpty();
	}
	
	private void filter() {
		
		searchAdapter.onTextChanged(searchText);
	}
	
}
