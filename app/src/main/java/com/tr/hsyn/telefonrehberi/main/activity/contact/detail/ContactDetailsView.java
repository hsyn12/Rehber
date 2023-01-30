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
 * Kişinin detayları için görsel öğeleri hazırlar.
 */
public abstract class ContactDetailsView extends ActivityView {
	
	/**
	 * Kişinin resmi
	 */
	protected ImageView               image;
	/**
	 * Kişinin çeşitli bilgilerini gösterecek olan görsel öğelerin kapsayıcısı.
	 * Kişi detayı için hazırlanan tüm görsel elemanlar bu kapsayıcının içine eklenir.
	 */
	protected ViewGroup               mainContainer;
	/**
	 * Kişinin telefon numaraları
	 */
	protected ViewGroup               numbersLayout;
	protected CollapsingToolbarLayout collapsingToolbarLayout;
	protected ProgressBar             progressBar;
	
	/**
	 * Kişi için düzenleme sayfasını aç
	 */
	protected abstract void editContact();
	
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
	
	protected void onClickEdit(View view) {
		
		editContact();
	}
	
	/**
	 * Kişi detaylarından görsel eleman çıkarır.
	 *
	 * @param view Görünümden çıkarılacak görsel eleman
	 */
	protected void removeDetailView(View view) {
		
		mainContainer.removeView(view);
	}
	
	/**
	 * Kişi detaylarına görsel eleman ekler.
	 *
	 * @param view Eklenecek görsel eleman
	 */
	protected void addDetailView(@NotNull View view) {
		
		mainContainer.addView(view);
	}
	
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
	
}
