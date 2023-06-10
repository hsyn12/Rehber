package com.tr.hsyn.telefonrehberi.main.code.comment.dialog;


import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.fastscroller.FastScrollRecyclerView;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.dev.Phone;
import com.tr.hsyn.xrelativelayout.RelativeLayoutx;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * Arama kayıtları listesini dialog olarak göstermek için
 */
public class ShowCallsDialog {
	
	private final AlertDialog dialog;
	
	@SuppressLint("InflateParams")
	public ShowCallsDialog(@NotNull Activity activity, @NotNull List<Call> calls) {
		
		this(activity, calls, null, null);
	}
	
	public ShowCallsDialog(@NotNull Activity activity, @NotNull List<Call> calls, String title, String subTitle) {
		
		RelativeLayoutx view = (RelativeLayoutx) activity.getLayoutInflater().inflate(R.layout.call_list, null, false);
		
		view.setMaxHeight((int) (Phone.getDisplaySize().y / 1.75f));
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		
		builder.setCancelable(true);
		builder.setView(view);
		builder.setOnCancelListener(d -> onCancel());
		
		dialog                                              = builder.create();
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationBounce;
		
		int color = Colors.getPrimaryColor();
		
		FastScrollRecyclerView recyclerView = view.findViewById(R.id.list_show_calls);
		recyclerView.setAdapter(new ShowCallsAdapter(calls));
		recyclerView.getFastScrollBar().setThumbActiveColor(color);
		
		Toolbar toolbar = view.findViewById(R.id.toolbar_show_calls);
		toolbar.setBackgroundColor(color);
		
		toolbar.setSubtitleTextColor(activity.getColor(com.tr.hsyn.rescolors.R.color.white_trans68));
		
		if (title != null) {
			
			toolbar.setTitle(title);
		}
		
		//noinspection ReplaceNullCheck
		if (subTitle != null) toolbar.setSubtitle(subTitle);
		else toolbar.setSubtitle(activity.getString(R.string.word_calls, calls.size()));
	}
	
	private void onCancel() {
		
		dialog.dismiss();
	}
	
	public void show() {
		
		dialog.show();
	}
	
}
