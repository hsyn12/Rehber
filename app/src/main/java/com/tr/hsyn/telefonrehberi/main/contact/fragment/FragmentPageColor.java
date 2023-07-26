package com.tr.hsyn.telefonrehberi.main.contact.fragment;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.colors.ColorHolder;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.page.ColorChanger;

import org.jetbrains.annotations.NotNull;


public abstract class FragmentPageColor extends FragmentContactsView implements ColorChanger {
	
	public final ColorHolder colorHolder = Colors.getColorHolder();
	
	@Override
	public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		setColor();
	}
	
	@Override
	public void changeColor(int color) {
		
		recyclerView.setPopupBackgroundColor(color);
		Colors.setProgressColor(progressBar);
		
		final long duration = 15000;
		Colors.runColorAnimation(Colors.getLastColor(), color, duration, i -> recyclerView.setThumbActiveColor(color));
	}
	
	protected void setColor() {
		
		int color = colorHolder.getPrimaryColor();
		refreshLayout.setColorSchemeColors(color);
		recyclerView.setThumbActiveColor(color);
		//rv.getFastScrollBar().setThumbInactiveColor(color);
		recyclerView.setPopupBackgroundColor(color);
		refreshLayout.setColorSchemeColors(color);
		Colors.setProgressColor(progressBar);
	}
}
