package com.example.xtoolbar;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.recolor.ReColor;


public class Toolbarx {
	
	/**
	 * Toolbar'ı activity'ye set et.
	 * Status bar ve tollbar rengini primary color değerine göre ayarla.
	 *
	 * @param activity Activity
	 * @param toolbar  Toolbar
	 */
	public static void setToolbar(@NonNull AppCompatActivity activity, @NonNull Toolbar toolbar) {
		
		activity.setSupportActionBar(toolbar);
		int primaryColor = Colors.getPrimaryColor();
		toolbar.setBackgroundColor(primaryColor);
		
		new ReColor(activity).setStatusBarColor(Colors.colorToString(Colors.getLastColor()), Colors.colorToString(Colors.darken(primaryColor, .8F)), 200);
	}
	
	public static void setToolbar(@NonNull AppCompatActivity activity, @NonNull Toolbar toolbar, boolean status) {
		
		activity.setSupportActionBar(toolbar);
		int primaryColor = Colors.getPrimaryColor();
		toolbar.setBackgroundColor(primaryColor);
	}
	
	public static void setToolbar(@NonNull AppCompatActivity activity, @NonNull Toolbar toolbar, Runnable onClickNavigationIcon) {
		
		activity.setSupportActionBar(toolbar);
		int primaryColor = Colors.getPrimaryColor();
		toolbar.setBackgroundColor(primaryColor);
		toolbar.setNavigationOnClickListener(v -> onClickNavigationIcon.run());
		new ReColor(activity).setStatusBarColor(Colors.colorToString(Colors.getLastColor()), Colors.colorToString(Colors.darken(primaryColor, .8F)), 200);
	}
	
	public static void setToolbar(@NonNull AppCompatActivity activity, int toolbarResId) {
		
		Toolbar toolbar = activity.findViewById(toolbarResId);
		activity.setSupportActionBar(toolbar);
		
		int primaryColor = Colors.getPrimaryColor();
		toolbar.setBackgroundColor(primaryColor);
		
		new ReColor(activity).setStatusBarColor(Colors.colorToString(Colors.getLastColor()), Colors.colorToString(Colors.darken(primaryColor, .9F)), 400);
	}
	
	public static void setToolbar(@NonNull AppCompatActivity activity, int toolbarResId, Runnable navigationClickListener) {
		
		Toolbar toolbar = activity.findViewById(toolbarResId);
		activity.setSupportActionBar(toolbar);
		
		int primaryColor = Colors.getPrimaryColor();
		toolbar.setBackgroundColor(primaryColor);
		toolbar.setNavigationOnClickListener(v -> navigationClickListener.run());
		new ReColor(activity).setStatusBarColor(Colors.colorToString(Colors.getLastColor()), Colors.colorToString(Colors.darken(primaryColor, .9F)), 400);
	}
	
}
