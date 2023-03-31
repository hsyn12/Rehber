package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.dialog;


import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.code.Phone;
import com.tr.hsyn.xrelativelayout.RelativeLayoutx;

import java.util.List;


public class MostCallDialog {
	
	@SuppressLint("InflateParams")
	public MostCallDialog(Activity activity, List<MostCallItemViewData> mostCallItemViewDataList) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		
		builder.setCancelable(true);
		
		RelativeLayoutx view = (RelativeLayoutx) activity.getLayoutInflater().inflate(R.layout.most_call_dialog, null, false);
		view.setMaxHeight((int) (Phone.getDisplaySize().y / 1.75f));
		RecyclerView list = view.findViewById(R.id.most_call_list);
		
		list.setAdapter(new MostCallDialogAdapter(mostCallItemViewDataList));
		
		builder.setView(view);
		
		AlertDialog dialog = builder.create();
		
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationBounce;
		
		dialog.show();
	}
}
