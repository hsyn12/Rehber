package com.tr.hsyn.telefonrehberi.main.code.comment.dialog;


import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.dev.Phone;
import com.tr.hsyn.xrelativelayout.RelativeLayoutx;

import java.util.List;


public class MostCallDialog {
	
	private final AlertDialog dialog;
	
	@SuppressLint("InflateParams")
	public MostCallDialog(Activity activity, List<MostCallItemViewData> mostCallItemViewDataList) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(true);
		
		RelativeLayoutx view = (RelativeLayoutx) activity.getLayoutInflater().inflate(R.layout.most_call_dialog, null, false);
		view.setMaxHeight((int) (Phone.getDisplaySize().y / 1.75f));
		RecyclerView list = view.findViewById(R.id.most_call_list);
		
		list.setAdapter(new MostCallDialogAdapter(mostCallItemViewDataList));
		
		builder.setView(view);
		
		dialog = builder.create();
		
		var window = dialog.getWindow();
		
		if (window != null)
			window.getAttributes().windowAnimations = R.style.DialogAnimationBounce;
	}
	
	public void show() {
		
		dialog.show();
	}
	
}