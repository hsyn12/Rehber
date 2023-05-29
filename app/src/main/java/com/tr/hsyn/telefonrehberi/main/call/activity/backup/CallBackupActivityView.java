package com.tr.hsyn.telefonrehberi.main.call.activity.backup;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.activity.ActivityView;
import com.tr.hsyn.bungee.Bungee;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.dev.ViewFinder;
import com.tr.hsyn.telefonrehberi.main.call.activity.backup.adapter.AdapterCallBackup;
import com.tr.hsyn.vanimator.ViewAnimator;


public abstract class CallBackupActivityView extends ActivityView implements ItemIndexListener {
	
	protected final ViewFinder<RecyclerView> list     = new ViewFinder<>(R.id.list_backups);
	protected final AdapterCallBackup        adapter  = new AdapterCallBackup(this, this::deleteBackup);
	private final   ViewFinder<ProgressBar>  progress = new ViewFinder<>(R.id.progress_backup_call);
	private final   ViewFinder<View>         empty    = new ViewFinder<>(R.id.empty_view_call_backup);
	
	abstract protected void deleteBackup(int index);
	
	protected abstract void addNewBackup();
	
	@Override
	protected final int getLayoutId() {
		
		return R.layout.activity_backup_call;
	}
	
	@Override
	protected void onCreate() {
		
		Colors.setProgressColor(progress.findView(this));
		
		list.findView(this).setAdapter(adapter);
	}
	
	protected final void checkEmpty() {
		
		showEmptyView(adapter.getCallBackups().isEmpty());
	}
	
	protected void showEmptyView(boolean isShow) {
		
		View v       = empty.findView(this);
		var  visable = v.getVisibility() != View.GONE;
		
		if (isShow == visable) return;
		
		var anim =
				ViewAnimator.on(v)
						.duration(400)
						.interpolator(new AccelerateInterpolator());
		
		if (isShow) {
			
			anim.alpha(0.5f, 1)
					.scale(0.7f, 1)
					.onStart(() -> v.setVisibility(View.VISIBLE));
		}
		else {
			
			anim.alpha(0)
					.scale(0.7f)
					.onStop(() -> v.setVisibility(View.GONE));
		}
		
		anim.start();
	}
	
	protected void showProgress(boolean show) {
		
		progress.findView(this).setVisibility(show ? View.VISIBLE : View.GONE);
	}
	
	@Override
	protected boolean hasToolbar() {
		
		return true;
	}
	
	@Override
	protected int getToolbarResourceId() {
		
		return R.id.toolbar_activity_backup;
	}
	
	@Override
	protected Runnable getNavigationClickListener() {
		
		return this::onBackPressed;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.activity_call_backup_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		
		if (item.getItemId() == R.id.menu_backup_add) {
			
			addNewBackup();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		Bungee.slideLeft(this);
	}
}
