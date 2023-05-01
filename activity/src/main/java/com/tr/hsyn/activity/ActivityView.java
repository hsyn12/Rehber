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
 * Uygulama genelinde kullanılan aynı tarz activity nesneleri için temel bir activity sınıfı.<br>
 * Soyut olan {@linkplain #getLayoutId()}} metodu ile verilecek görünüm dosyasını activity için set eder.<br>
 * Ayrıca, {@linkplain #hasToolbar()} metodu ile true dönülüyorsa, {@linkplain #getToolbarResourceId()} metodu
 * {@link Toolbar} görselinin kaynak id değerini dönmeli.
 * Bu şekilde toolbar set edilir ve rengi ana renk  olarak belirlenir
 * ve toolbar ikonunun click olayı için {@linkplain Activity#onBackPressed()} metodu verilir.
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
	 * @return Görünüm dosyasının id değeri
	 */
	@LayoutRes
	protected abstract int getLayoutId();
	
	/**
	 * Görünüm dosyası set edildikten (ve varsa toolbar ayarlandıktan) hemen sonra çağrılır.
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
