package com.tr.hsyn.telefonrehberi.main.code.comment.dialog;


import android.app.Activity;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.telefonrehberi.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class MostDurationDialog extends Dialog {
	
	private final AlertDialog dialog;
	
	public MostDurationDialog(Activity activity, List<MostDurationData> mostDurationDataList, @NotNull String title, @NotNull String subTitle) {
		
		AlertDialog.Builder builder = getBuilder(activity, R.layout.most_call_dialog, true, null);
		RecyclerView        list    = rootView.findViewById(R.id.most_call_list);
		list.setAdapter(new MostDurationAdapter(mostDurationDataList));
		setHeight(list, list.getLayoutParams());
		
		((TextView) rootView.findViewById(R.id.title)).setText(title);
		((TextView) rootView.findViewById(R.id.sub_title)).setText(subTitle);
		rootView.findViewById(R.id.header_include).setBackgroundColor(Colors.getPrimaryColor());
		dialog = builder.create();
		
		setAnimation(R.style.DialogAnimationBounce);
	}
	
	@Override
	protected AlertDialog getDialog() {
		
		return dialog;
	}
}
