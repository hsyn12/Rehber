package com.tr.hsyn.telefonrehberi.main.activity.contact.detail;


import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tr.hsyn.activity.ActivityView;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.vanimator.ViewAnimator;

import org.jetbrains.annotations.NotNull;


/**
 * This class sets up main views of the contact details.
 */
public abstract class ContactDetailsView extends ActivityView {
	
	/** Contact image */
	protected ImageView               image;
	/**
	 * All views of the contact details will be added to this container
	 */
	protected ViewGroup               mainContainer;
	/**
	 * Phone numbers layout
	 */
	protected ViewGroup               numbersLayout;
	/**
	 * Collapsing toolbar
	 */
	protected CollapsingToolbarLayout collapsingToolbarLayout;
	/**
	 * Progress bar
	 */
	protected ProgressBar             progressBar;
	
	@Override
	protected int getLayoutId() {return R.layout.activity_contact_details;}
	
	@Override
	protected void onCreate() {
		
		image                   = findView(R.id.image);
		mainContainer           = findView(R.id.contact_details_main_container);
		numbersLayout           = findView(R.id.numbers);
		collapsingToolbarLayout = findView(R.id.collapsing_toolbar);
		progressBar             = findView(R.id.progress_contact_details);
		
		int primaryColor    = Colors.getPrimaryColor();
		int backgroundColor = Colors.lighter(primaryColor, 0.91f);
		var scrollView      = findView(R.id.contact_details_nested_scroll_view);
		var main            = findView(R.id.coordinator);
		
		scrollView.setBackgroundColor(backgroundColor);
		main.setBackgroundColor(backgroundColor);
		
		Toolbar toolbar = findView(R.id.toolbar_contact_details);
		
		setSupportActionBar(toolbar);
		toolbar.setNavigationOnClickListener(v -> onBackPressed());
		Colors.changeStatusBarColor(this);
		
		Runny.run(this::showActionButton, 700L);
	}
	
	/**
	 * Show the action button
	 */
	private void showActionButton() {
		
		FloatingActionButton actionButton = findView(R.id.contact_details_action_button);
		
		actionButton.setBackgroundTintList(ColorStateList.valueOf(Colors.getPrimaryColor()));
		actionButton.setVisibility(View.VISIBLE);
		
		actionButton.setOnClickListener(this::onClickEdit);
		
		ViewAnimator.on(actionButton)
				.translationX(200, 0)
				.rotation(180, 0)
				.duration(600L)
				.start();
		
	}
	
	/**
	 * This method is called when the action button is clicked
	 *
	 * @param view the view
	 */
	protected void onClickEdit(View view) {
		
		editContact();
	}
	
	/**
	 * This method is called when the action button is clicked
	 */
	protected abstract void editContact();
	
	/**
	 * Removes the view from the detail view
	 *
	 * @param view the view to remove
	 */
	protected void removeFromDetailView(View view) {
		
		mainContainer.removeView(view);
	}
	
	/**
	 * Adds the view to the detail view
	 *
	 * @param view the view to add
	 */
	protected void addToDetailView(@NotNull View view) {
		
		mainContainer.addView(view);
	}
	
}
