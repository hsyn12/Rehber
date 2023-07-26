package com.tr.hsyn.telefonrehberi.main.activity.city;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.tr.hsyn.colors.ColorHolder;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.fastscroller.FastScrollListener;
import com.tr.hsyn.message.snack.SnackBarListener;
import com.tr.hsyn.shower.ShadowWomen;
import com.tr.hsyn.shower.ShowWomen;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.cast.MainHeader;
import com.tr.hsyn.telefonrehberi.main.call.fragment.PageCallLog;
import com.tr.hsyn.telefonrehberi.main.code.page.Header;
import com.tr.hsyn.telefonrehberi.main.code.page.PageAdapter;
import com.tr.hsyn.telefonrehberi.main.contact.fragment.FragmentContacts;
import com.tr.hsyn.xbox.Blue;


public abstract class MainActivity extends MainActivityView implements SnackBarListener, FastScrollListener {
	
	
	/**
	 * Kişiler sayfasının {@code viewPager} içindeki index değeri
	 */
	public static final int              PAGE_CONTACTS      = 0;
	/**
	 * Arama kayıtları sayfasının {@code viewPager} içindeki index değeri
	 */
	public static final int              PAGE_CALL_LOG      = 1;
	/**
	 * Uygun bir animasyon süresi
	 */
	private final       long             ANIMATION_DURATION = 600L;
	private final       ColorHolder      colorHolder        = Colors.getColorHolder();
	/**
	 * Arama Kayıtları Sayfası
	 */
	protected           PageCallLog      pageCallLog;
	/**
	 * Daima kullanıcının seçtiği sayfayı işaret eder.
	 *
	 * @see #PAGE_CONTACTS
	 * @see #PAGE_CALL_LOG
	 */
	protected           int              currentPage;
	protected           FragmentContacts pageContacts;
	/**
	 * Sayfaları kullanıcının seçimine göre aktif-pasif yapan nesne
	 */
	private             ShowWomen        showWomen;
	/**
	 * Aktif durumda bir Snack mesajı varsa {@code true} değerindedir, aksi halde {@code false}
	 */
	private             boolean          isSnakeActive;
	/**
	 * Hızlı kaydırma aktifse {@code true}
	 */
	private             boolean          fastScrollActive;
	/**
	 * Yeni kişi ekleme butonu görünmez durumda ise {@code true}
	 */
	private             boolean          actionButtonHidden;
	
	@Override
	protected void onCreate() {
		
		super.onCreate();
		
		//setContentView(R.layout.activity_main);
		
		
		//- MainActivity ana başlığı
		Header header = new MainHeader(title, subTitle);
		
		pageContacts = new FragmentContacts();
		pageContacts.setHeader(header);
		//pageContacts = new PageContacts();
		pageCallLog = new PageCallLog();
		pageCallLog.setHeader(header);
		
		TabLayout tabLayout = findViewById(R.id.main_tab_layout);
		ViewPager viewPager = findViewById(R.id.main_view_pager);
		viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
		tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
		
		
		setColors(colorHolder.getPrimaryColor());
		
		//pageCallLog.title.setObserver(ch -> setPageTitle(currentPage));
		//pageCallLog.subTitle.setObserver(ch -> changeSubTitle(PAGE_CALL_LOG, ch));
		
		final long duration = 300L;
		final int  alpha    = 150;
		
		Runny.run(() -> {
			
			int           colorUnselected = Colors.setAlpha(Color.WHITE, alpha);
			int           colorSelected   = Color.WHITE;
			TabLayout.Tab tabContacts     = tabLayout.getTabAt(0);
			TabLayout.Tab tabCalls        = tabLayout.getTabAt(1);
			
			Fragment[] fragments = {pageContacts, pageCallLog};
			
			PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), fragments);
			
			viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
				
				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
				
				@SuppressWarnings("ConstantConditions")
				@Override
				public void onPageSelected(int position) {
					
					currentPage = position;
					
					if (position == PAGE_CONTACTS) {
						
						tabContacts.getIcon().setTint(colorSelected);
						tabCalls.getIcon().setTint(colorUnselected);
					}
					else {
						
						tabContacts.getIcon().setTint(colorUnselected);
						tabCalls.getIcon().setTint(colorSelected);
					}
					
					onPageChange(currentPage);
				}
				
				@Override
				public void onPageScrollStateChanged(int state) {}
			});
			
			viewPager.setOffscreenPageLimit(2);
			viewPager.setAdapter(pageAdapter);
			
			showWomen = new ShadowWomen(pageContacts, pageCallLog);
		}, duration);
		
	}
	
	@Override
	protected void onDestroy() {
		
		Blue.clear();
		super.onDestroy();
	}
	
	@Override
	public int getCurrentPage() {
		
		return currentPage;
	}
	
	@Override
	public void changeTabBehavior(int flags) {
		
		AppBarLayout.LayoutParams p = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
		p.setScrollFlags(flags);
		toolbar.setLayoutParams(p);
	}
	
	@Override
	public void onSnackBarStarted(Object object) {
		
		isSnakeActive = true;
		setActionButtonVisibility(false);
	}
	
	@Override
	public void onSnackBarFinished(Object object, boolean actionPressed) {
		
		isSnakeActive = false;
		setActionButtonVisibility(true);
	}
	
	@Override
	public void onFastScrollStart() {
		
		fastScrollActive = true;
		setActionButtonVisibility(false);
	}
	
	@Override
	public void onFastScrollStop() {
		
		fastScrollActive = false;
		setActionButtonVisibility(true);
	}
	
	/**
	 * Sayfa değiştirildi.<br>
	 * Bu metot, ana ekranın sahip olduğu {@link ViewPager}'ın
	 * sayfa değişikliği olayının dinleyicisidir ve sayfa her değiştiğinde
	 * otomatik olarak çağrılır.
	 * Bu metodu kendi içinde tanımlayan alt sınıflar
	 * ilk önce {@code super} metodunu çağırmalı,
	 * aksi halde sayfa değişse bile değişen sayfalar değişiklikten habersiz durumda olacak.
	 * Çünkü sayfaların kendisi de aktif-pasif durumlarını bu çağrı ile almaktalar.
	 * Yine de bağımsız olarak {@link #currentPage} değişkeni daima o an seçili
	 * olan sayfayı anlık olarak işaret etmektedir (sayfanın kendisinin bundan haberi olmasa bile).
	 *
	 * @param page Yani sayfa
	 * @see #PAGE_CONTACTS
	 * @see #PAGE_CALL_LOG
	 */
	@CallSuper
	protected void onPageChange(int page) {
		
		if (showWomen != null)
			showWomen.show(page);
		
		setActionButtonVisibility(page == PAGE_CONTACTS);
	}
	
	private void setActionButtonVisibility(boolean visible) {
		
		if (visible) showActionButton();
		else hideActionButton();
	}
	
	private void showActionButton() {
		
		if (currentPage == PAGE_CONTACTS) {
			
			if (fastScrollActive) return;
			
			if (!isSnakeActive) {
				
				if (actionButtonHidden) {
					
					actionButton.setVisibility(View.VISIBLE);
					actionButtonHidden = false;
					
					ViewCompat.animate(actionButton)
							.alpha(1f)
							.rotation(0f)
							.translationY(0f)
							.setDuration(ANIMATION_DURATION)
							.start();
				}
				else {
					
					ViewCompat.animate(actionButton)
							.rotation(0f)
							.translationY(0f)
							.setDuration(ANIMATION_DURATION)
							.start();
				}
			}
			else {
				
				if (actionButtonHidden) {
					
					actionButton.setVisibility(View.VISIBLE);
					actionButtonHidden = false;
					
					//sadece görünür ol çünkü snakebar aktif
					ViewCompat.animate(actionButton)
							.alpha(1f)
							.setDuration(ANIMATION_DURATION)
							.start();
				}
			}
		}
	}
	
	private void hideActionButton() {
		
		final float rotation = 360f;
		final float transY   = -142f;
		
		//- Arama kayıtları sayfası
		if (currentPage != PAGE_CONTACTS) {
			
			if (isSnakeActive) {
				
				ViewCompat.animate(actionButton)
						.alpha(0f)
						.setDuration(ANIMATION_DURATION)
						.withEndAction(() -> actionButton.setVisibility(View.GONE))
						.start();
				
				actionButtonHidden = true;
			}
			else {
				
				if (!fastScrollActive) {
					
					ViewCompat.animate(actionButton)
							.alpha(0f)
							.rotation(rotation)
							.translationY(transY)
							.setDuration(ANIMATION_DURATION)
							.withEndAction(() -> actionButton.setVisibility(View.GONE))
							.start();
					
					actionButtonHidden = true;
				}
			}
		}
		else {//- Kişiler sayfası
			
			if (fastScrollActive) {
				
				ViewCompat.animate(actionButton)
						.alpha(0f)
						.rotation(rotation)
						.translationY(transY)
						.setDuration(ANIMATION_DURATION)
						.withEndAction(() -> actionButton.setVisibility(View.GONE))
						.start();
			}
			else {
				
				ViewCompat.animate(actionButton)
						.rotation(rotation)
						.translationY(transY)
						.setDuration(ANIMATION_DURATION)
						.start();
			}
			
			actionButtonHidden = true;
		}
		
	}
	
	/**
	 * Kullanıcı talebi sebebiyle rengi değiştir.
	 *
	 * @param color Yeni renk
	 */
	protected void onChangeColor(int color) {
		
		pageContacts.changeColor(color);
		pageCallLog.changeColor(color);
		
		Colors.runColorAnimation(Colors.getLastColor(), color, (int i) -> {
			
			appBarLayout.setBackgroundColor(i);
			toolbar.setBackgroundColor(i);
			actionButton.setSupportBackgroundTintList(ColorStateList.valueOf(i));
		});
		
		Colors.changeStatusBarColor(this, color);
	}
	
	
}