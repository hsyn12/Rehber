package com.tr.hsyn.colors;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.tr.hsyn.recolor.ReColor;
import com.tr.hsyn.textdrawable.util.ColorGenerator;

import java.util.function.Consumer;
import java.util.function.IntConsumer;


public class Colors {
	
	public static final  ColorGenerator COLOR_GENERATOR           = ColorGenerator.MATERIAL;
	private static final int            STATUS_BAR_COLOR_DURATION = 700;
	
	public static int getPrimaryColor() {
		
		return getColorHolder().getPrimaryColor();
	}
	
	public static ColorHolder getColorHolder() {
		
		return Rehber.Color.getColorHolder();
	}
	
	public static int getLastColor() {
		
		return getColorHolder().getLastColor();
	}
	
	public static int getRipple() {
		
		return getColorHolder().getRipple();
	}
	
	public static void changeStatusBarColor(@NonNull final Activity activity) {
		
		int         color       = getPrimaryColor();
		final float factor      = .8f;
		final int   darkenColor = darken(color, factor);
		
		new ReColor(activity)
				.setStatusBarColor(
						colorToString(0),
						colorToString(darkenColor),
						STATUS_BAR_COLOR_DURATION);
	}
	
	/**
	 * StatusBar rengini değiştir.
	 *
	 * @param activity Activity
	 */
	public static void changeStatusBarColor(@NonNull final Activity activity, int color) {
		
		
		final float factor      = .8f;
		final int   darkenColor = darken(color, factor);
		
		new ReColor(activity)
				.setStatusBarColor(
						colorToString(0),
						colorToString(darkenColor),
						STATUS_BAR_COLOR_DURATION);
	}
	
	public static void changeStatusBarColor(@NonNull final Activity activity, long duration) {
		
		int         color       = getPrimaryColor();
		final float factor      = .8f;
		final int   darkenColor = darken(color, factor);
		
		new ReColor(activity)
				.setStatusBarColor(
						colorToString(0),
						colorToString(darkenColor),
						(int) duration);
	}
	
	public static void changeStatusBarColor(@NonNull final Activity activity, int color, int duration) {
		
		final float factor      = .8f;
		final int   darkenColor = darken(color, factor);
		
		new ReColor(activity)
				.setStatusBarColor(
						colorToString(0),
						colorToString(darkenColor),
						duration);
	}
	
	public static void setStatusBarColor(@NonNull Window window) {
		
		int         color  = getPrimaryColor();
		final float factor = .8f;
		window.setStatusBarColor(darken(color, factor));
	}
	
	/**
	 * StatusBar rengini set eder.
	 *
	 * @param window Geçerli ekran
	 * @param color  Set edilecek renk. Ancak bu rengin hafif koyusu set edilir.
	 */
	public static void setStatusBarColor(@NonNull Window window, int color) {
		
		final float factor = .8f;
		window.setStatusBarColor(darken(color, factor));
	}
	
	/**
	 * Progressbar'ın rengini değiştirir.
	 *
	 * @param progressBar Progressbar
	 */
	@SuppressWarnings({"deprecation", "RedundantSuppression"})
	public static void setProgressColor(ProgressBar progressBar) {
		
		int color = getPrimaryColor();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			
			progressBar.getIndeterminateDrawable().setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_IN));
		}
		else {
			
			progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
		}
		
	}
	
	/**
	 * Verilen renge verilen alpha değerini uygular.
	 *
	 * @param color Değişecek olan renk
	 * @param alpha 0-255 arası bir değer. 0 - Görünmez, 255 - tam görünür
	 * @return Değişen renk
	 */
	public static int setAlpha(int color, int alpha) {
		
		return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
	}
	
	@SuppressWarnings({"deprecation", "RedundantSuppression"})
	public static void setTintDrawable(@NonNull Drawable drawable) {
		
		@ColorInt int color = getPrimaryColor();
		drawable.clearColorFilter();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			
			drawable.setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_IN));
		}
		else {
			
			drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
		}
		
		drawable.invalidateSelf();
		Drawable wrapDrawable = DrawableCompat.wrap(drawable).mutate();
		DrawableCompat.setTint(wrapDrawable, color);
	}
	
	public static void setTintDrawable(@NonNull Drawable drawable, @ColorInt int color) {
		
		drawable.clearColorFilter();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			
			drawable.setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_IN));
		}
		else {
			
			drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
		}
		
		drawable.invalidateSelf();
		Drawable wrapDrawable = DrawableCompat.wrap(drawable).mutate();
		DrawableCompat.setTint(wrapDrawable, color);
	}
	
	public static void setSwitchTint(@NonNull SwitchCompat view, int color) {
		
		int trackColor    = lighter(color, .8F);
		int thumbColor    = lighter(color, .5F);
		int disabledColor = lighter(Color.LTGRAY, .8f);
		
		ColorStateList trackStates = new ColorStateList(
				new int[][]{
						new int[]{-android.R.attr.state_enabled},
						new int[]{android.R.attr.state_checked},
						new int[]{}
				},
				new int[]{
						disabledColor,
						trackColor,
						trackColor,
				}
		);
		
		ColorStateList thumbStates = new ColorStateList(
				new int[][]{
						new int[]{-android.R.attr.state_enabled},
						new int[]{android.R.attr.state_checked},
						new int[]{}
				},
				new int[]{
						disabledColor,
						color,
						thumbColor,
				}
		);
		
		view.setThumbTintList(thumbStates);
		view.setTrackTintList(trackStates);
		view.setTrackTintMode(PorterDuff.Mode.MULTIPLY);
		view.setThumbTintMode(PorterDuff.Mode.MULTIPLY);
	}
	
	/**
	 * Rengi aç.
	 *
	 * @param color  Renk
	 * @param factor 0-1 arasında bir değer. 1'e yaklaştıkça açılma artar
	 * @return Açılmış renk
	 */
	public static int lighter(int color, float factor) {
		
		int red   = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
		int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
		int blue  = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
		return Color.argb(Color.alpha(color), red, green, blue);
	}
	
	/**
	 * Kaynak dosyasından verilen rengi döndürür.
	 *
	 * @param context    Context. Kaynak dosyaya erişmek için
	 * @param resourceId Kaynak id değeri
	 * @return Renk
	 */
	@ColorInt
	public static int getColor(@NonNull Context context, @ColorRes int resourceId) {
		
		return ContextCompat.getColor(context, resourceId);
	}
	
	/**
	 * Rengi koyulaştır.
	 *
	 * @param color  Renk
	 * @param factor 0-1 arasında bir değer. Sıfıra yaklaştıkça koyulaşır
	 * @return Koyu renk
	 */
	public static int darken(int color, double factor) {
		
		return (color & 0xFF000000) |
		       (crimp((int) (((color >> 16) & 0xFF) * factor)) << 16) |
		       (crimp((int) (((color >> 8) & 0xFF) * factor)) << 8) |
		       (crimp((int) (((color) & 0xFF) * factor)));
	}
	
	private static int crimp(int c) {
		
		return Math.min(Math.max(c, 0), 255);
	}
	
	/**
	 * Verilen rengi string olarak döndürür.
	 *
	 * @param color Renk
	 * @return Rengin string karşılığı
	 */
	public static String colorToString(@ColorInt int color) {
		
		return String.format("#%06X", 0xFFFFFF & color);
	}
	
	@SuppressWarnings({"deprecation", "RedundantSuppression"})
	public static void setProgressColor(ProgressBar progressBar, int color) {
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			
			progressBar.getIndeterminateDrawable().setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_IN));
		}
		else {
			
			progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
		}
		
	}
	
	/**
	 * Bir renkten diğerine geçiş yapan bir animasyon oluşturulur ve çalıştırılır.
	 *
	 * @param fromColor    Başlangıç rengi
	 * @param toColor      Nihai renk
	 * @param intConsumers Animasyon değerlerini alacak nesne
	 */
	public static void runColorAnimation(int fromColor, int toColor, IntConsumer intConsumers) {
		
		runColorAnimation(fromColor, toColor, 2500L, intConsumers);
	}
	
	public static void runColorAnimation(int fromColor, int toColor, long duration, IntConsumer intConsumers) {
		
		ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
		
		colorAnimation.setDuration(duration);
		
		colorAnimation.addUpdateListener(animator -> intConsumers.accept((int) animator.getAnimatedValue()));
		
		colorAnimation.start();
		
	}
	
	public static void runAnimation(float start, float end, long duration, Consumer<Float> consumer) {
		
		ValueAnimator colorAnimation = ValueAnimator.ofFloat(start, end);
		
		colorAnimation.setDuration(duration);
		
		colorAnimation.addUpdateListener(animator -> consumer.accept((Float) animator.getAnimatedValue()));
		
		colorAnimation.start();
	}
	
	public static void runAnimation(int start, int end, long duration, IntConsumer consumer) {
		
		ValueAnimator colorAnimation = ValueAnimator.ofInt(start, end);
		
		colorAnimation.setDuration(duration);
		
		colorAnimation.addUpdateListener(animator -> consumer.accept((int) animator.getAnimatedValue()));
		
		colorAnimation.start();
	}
	
	public static void runAnimation(int start, int end, long duration, IntConsumer consumer, Runnable onEnd) {
		
		ValueAnimator colorAnimation = ValueAnimator.ofInt(start, end);
		
		colorAnimation.setDuration(duration);
		
		colorAnimation.addListener(new AnimatorListenerAdapter() {
			
			@Override
			public void onAnimationEnd(Animator animation) {
				
				onEnd.run();
			}
		});
		
		colorAnimation.addUpdateListener(animator -> consumer.accept((int) animator.getAnimatedValue()));
		
		colorAnimation.start();
	}
	
	@ColorInt
	public static int getRandomColor() {
		
		return COLOR_GENERATOR.getRandomColor();
	}
	
	public static int getBackgroundColor(@NonNull View view) {
		
		Drawable d = view.getBackground();
		
		if (d instanceof ColorDrawable) {
			
			return ((ColorDrawable) d).getColor();
		}
		
		throw new IllegalArgumentException("No ColorDrawable");
	}
	
}
