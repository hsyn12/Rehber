package com.tr.hsyn.telefonrehberi.main.code.comment.dialog;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.dev.Phone;
import com.tr.hsyn.xrelativelayout.RelativeLayoutx;

import java.util.List;


public class MostCallDialog {
	
	private final AlertDialog dialog;
	
	@SuppressLint("InflateParams")
	public MostCallDialog(Activity activity, List<MostCallItemViewData> mostCallItemViewDataList, String title, String subTitle) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(true);
		
		RelativeLayoutx view = (RelativeLayoutx) activity.getLayoutInflater().inflate(R.layout.most_call_dialog, null, false);
		view.setMaxHeight((int) (Phone.getDisplaySize().y / 1.75f));
		RecyclerView list = view.findViewById(R.id.most_call_list);
		
		list.setAdapter(new MostCallDialogAdapter(mostCallItemViewDataList));
		
		builder.setView(view);
		builder.setOnCancelListener(this::onCancel);
		((TextView) view.findViewById(R.id.title)).setText(title);
		((TextView) view.findViewById(R.id.sub_title)).setText(subTitle);
		view.setBackgroundColor(Colors.getPrimaryColor());
		dialog = builder.create();
		
		android.view.Window window = dialog.getWindow();
		
		if (window != null)
			window.getAttributes().windowAnimations = R.style.DialogAnimationBounce;
	}
	
	public void show() {
		
		dialog.show();
	}
	
	private void onCancel(DialogInterface dialog) {
		
		dialog.dismiss();
	}
}
