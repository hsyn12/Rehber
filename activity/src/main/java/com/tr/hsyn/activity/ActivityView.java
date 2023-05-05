package com.tr.hsyn.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.xtoolbar.Toolbarx;

import org.jetbrains.annotations.NotNull;


/**
 * A basic activity class for the same style of activity objects used throughout the application.
 * Sets the view file to be exported with the abstract {@linkplain #getLayoutId()}} method.
 * Also, with the {@linkplain #hasToolbar()} method If returning true,
 * the {@linkplain #getToolbarResourceId()} method should return the resource id of the {@link Toolbar} view.
 * In this way, the toolbar is set, and its color is determined as the main color,
 * and the {@linkplain Activity#onBackPressed()} method is set for the click event of the toolbar icon.
 */
public abstract class ActivityView extends AppCompatActivity {
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(getLayoutId());
		
		if (hasToolbar()) {
			
			if (getToolbarResourceId() != -1) {
				
				Toolbarx.setToolbar(this, getToolbarResourceId(), getNavigationClickListener());
			}
		}
		
		onCreate();
	}
	
	/**
	 * @return The id of the view file
	 */
	@LayoutRes
	protected abstract int getLayoutId();
	
	/**
	 * Invoked immediately after the view file (and the toolbar, if any) has been set.
	 */
	protected abstract void onCreate();
	
	/**
	 * @return {@code true} if this activity has toolbar
	 */
	protected boolean hasToolbar() {
		
		return false;
	}
	
	/**
	 * @return if {@link #hasToolbar()} returns true, this method must return toolbar id, otherwise returns {@code -1}
	 */
	@IdRes
	protected int getToolbarResourceId() {
		
		return -1;
	}
	
	/**
	 * @return Toolbar icon click handler (Default {@link Activity#onBackPressed()})
	 */
	protected Runnable getNavigationClickListener() {
		
		return this::onBackPressed;
	}
	
	/**
	 * Returns the view with the given id in the given view.
	 *
	 * @param view The view to find the view in it
	 * @param id   The id of the view
	 * @param <T>  Type of the view
	 * @return View
	 */
	protected final <T extends View> T findView(@NotNull View view, int id) {
		
		return view.findViewById(id);
	}
	
	/**
	 * @return {@linkplain Toolbar}
	 */
	protected final Toolbar getToolbar() {
		
		return findView(getToolbarResourceId());
	}
	
	/**
	 * Find a view.
	 *
	 * @param id  Resource id for the view
	 * @param <T> Type of the view
	 * @return View
	 */
	protected final <T extends View> T findView(int id) {
		
		return findViewById(id);
	}
}
