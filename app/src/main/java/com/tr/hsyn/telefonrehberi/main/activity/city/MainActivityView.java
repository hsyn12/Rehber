package com.tr.hsyn.telefonrehberi.main.activity.city;


import android.content.res.ColorStateList;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.appcompat.widget.Toolbar;

import com.example.xtoolbar.Toolbarx;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tr.hsyn.activity.ActivityView;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.htext.base.HTextView;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.cast.HasTabBehavior;


public abstract class MainActivityView extends ActivityView implements HasTabBehavior {
	
	/**
	 * Ana activity'de olmasına rağmen Kişiler sayfasının yeni kişi eklemek için kullanılan aracı.
	 */
	protected FloatingActionButton actionButton;
	/**
	 * Toolbar
	 */
	protected Toolbar              toolbar;
	/**
	 * Appbar
	 */
	protected AppBarLayout         appBarLayout;
	protected HTextView            title;
	protected HTextView            subTitle;
	
	@Override
	protected int getLayoutId() {
		
		return R.layout.activity_main;
	}
	
	@CallSuper
	@Override
	protected void onCreate() {
		
		//- Activity'nin varsayılan başlığı
		setTitle("");
		
		toolbar = findViewById(R.id.main_toolbar);
		Toolbarx.setToolbar(this, toolbar, true);
		
		title        = findViewById(R.id.main_title);
		subTitle     = findViewById(R.id.main_sub_title);
		appBarLayout = findViewById(R.id.main_appbar);
		actionButton = findViewById(R.id.main_action_button);
		actionButton.setOnClickListener(this::onClickNewContact);
	}
	
	/**
	 * Kullanıcı rehbere yeni bir kişi eklemek istiyor.
	 *
	 * @param view view
	 */
	protected void onClickNewContact(View view) {
		
	}
	
	/**
	 * Gerekli ögelerin rengini ayarlar
	 *
	 * @param color Renk
	 */
	protected void setColors(int color) {
		
		final float factor = .9f;
		getWindow().setStatusBarColor(Colors.darken(color, factor));
		actionButton.setSupportBackgroundTintList(ColorStateList.valueOf(color));
		appBarLayout.setBackgroundColor(color);
		toolbar.setBackgroundColor(color);
	}
}
