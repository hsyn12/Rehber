package com.tr.hsyn.telefonrehberi.main.code.comment.dialog;


import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.telefonrehberi.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * The list that shows the quantity of calls.
 */
public class MostCallDialog extends Dialog {
	
	private final AlertDialog dialog;
	
	/**
	 * Creates a new instance of the {@link MostCallDialog} class.
	 *
	 * @param activity                 the activity.
	 * @param mostCallItemViewDataList the list of most call items.
	 * @param title                    the title
	 * @param subTitle                 the subtitle
	 */
	public MostCallDialog(@NotNull Activity activity, @NotNull List<MostCallItemViewData> mostCallItemViewDataList, @Nullable String title, @Nullable String subTitle) {
		
		AlertDialog.Builder builder = getBuilder(activity, true, null);
		
		View         view = inflateLayout(activity, R.layout.most_call_dialog);
		RecyclerView list = view.findViewById(R.id.most_call_list);
		list.setAdapter(new MostCallDialogAdapter(mostCallItemViewDataList));
		
		setHeight(list, list.getLayoutParams());
		builder.setView(view);
		
		((TextView) view.findViewById(R.id.title)).setText(title);
		((TextView) view.findViewById(R.id.sub_title)).setText(subTitle);
		view.findViewById(R.id.header_include).setBackgroundColor(Colors.getPrimaryColor());
		
		dialog = builder.create();
		setAnimation(R.style.DialogAnimationBounce);
	}
	
	@Override
	protected AlertDialog getDialog() {
		
		return dialog;
	}
}
