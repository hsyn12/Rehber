package com.tr.hsyn.telefonrehberi.main.call.fragment;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.colors.Colors;

import org.jetbrains.annotations.NotNull;


public abstract class CallLogColor extends CallLogFilter {
	
	@Override
	public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		setColor();
	}
	
	@Override
	public void changeColor(int color) {
		
		swipeCallBack.setBgColor(color);
		
		recyclerView.setPopupBackgroundColor(color);
		Colors.setProgressColor(progressBar);
		
		Colors.runColorAnimation(Colors.getLastColor(), color, 15000L, i -> recyclerView.setThumbActiveColor(color));
		
	}
	
	private void setColor() {
		
		int color = colorHolder.getPrimaryColor();
		refreshLayout.setColorSchemeColors(color);
		recyclerView.setThumbActiveColor(color);
		//rv.getFastScrollBar().setThumbInactiveColor(color);
		recyclerView.setPopupBackgroundColor(color);
		refreshLayout.setColorSchemeColors(color);
		Colors.setProgressColor(progressBar);
	}
}
