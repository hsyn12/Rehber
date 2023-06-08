package com.tr.hsyn.colors;


import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.tr.hsyn.holder.Holder;


public interface Rehber {
	
	interface Color {
		
		//region App Colors
		int DEFAULT     = 0;
		int PURPLE      = 1;
		int BLUE        = 2;
		int ORANGE      = 3;
		int GREEN       = 4;
		int BROWN       = 5;
		int GREY        = 6;
		int TEAL        = 7;
		int PINK        = 8;
		int LIME        = 9;
		int AMBER       = 10;
		int LIGHT_GREEN = 11;
		int CHOCOLATE   = 12;
		int SKY         = 13;
		int NIGHT       = 14;
		int KESTANE     = 15;
		int ORKIDE      = 16;
		int YONCA       = 17;
		int ALEV        = 18;
		int HAVUC       = 19;
		//endregion
		
		Holder<ColorHolder> _colorHolder = Holder.newHolder();
		
		static ColorHolder getColorHolder() {
			
			if (_colorHolder.getValue() == null) throw new RuntimeException("init not called");
			
			return _colorHolder.getValue();
		}
		
		static void init(@NonNull final Context context) {
			
			int pColor = ColorSelection.getSelected(context);
			
			_colorHolder.setValue(ColorHolder.newHolder(
					pColor,
					0,
					getRipple(context, pColor)
			));
		}
		
		/**
		 * Seçilen renge göre ripple.
		 *
		 * @param context Context
		 * @return resource id
		 */
		@DrawableRes
		static int getRipple(@NonNull final Context context, int primaryColor) {
			
			int ripple = com.tr.hsyn.resripple.R.drawable.ripple;
			int index  = getColorIndex(context, primaryColor);
			
			switch (index) {
				//@off
				case DEFAULT: break;
				case PURPLE: ripple = com.tr.hsyn.resripple.R.drawable.ripple_purple;
					break;
				case BLUE: ripple = com.tr.hsyn.resripple.R.drawable.ripple_light_blue;
					break;
				case ORANGE: ripple = com.tr.hsyn.resripple.R.drawable.ripple_orange;
					break;
				case GREEN: ripple = com.tr.hsyn.resripple.R.drawable.ripple_cimen;
					break;
				case BROWN: ripple = com.tr.hsyn.resripple.R.drawable.ripple_toprak;
					break;
				case GREY: ripple = com.tr.hsyn.resripple.R.drawable.ripple_gri;
					break;
				case TEAL: ripple = com.tr.hsyn.resripple.R.drawable.ripple_teal;
					break;
				case PINK: ripple = com.tr.hsyn.resripple.R.drawable.ripple_pink;
					break;
				case LIME: ripple = com.tr.hsyn.resripple.R.drawable.ripple_lime;
					break;
				case AMBER: ripple = com.tr.hsyn.resripple.R.drawable.ripple_amber;
					break;
				case LIGHT_GREEN: ripple = com.tr.hsyn.resripple.R.drawable.ripple_light_green;
					break;
				case CHOCOLATE: ripple = com.tr.hsyn.resripple.R.drawable.ripple_cikolata;
					break;
				case SKY: ripple = com.tr.hsyn.resripple.R.drawable.ripple_dark_blue;
					break;
				case NIGHT: ripple = com.tr.hsyn.resripple.R.drawable.ripple_night;
					break;
				case KESTANE: ripple = com.tr.hsyn.resripple.R.drawable.ripple_kestane;
					break;
				case ORKIDE: ripple = com.tr.hsyn.resripple.R.drawable.ripple_orkide;
					break;
				case YONCA: ripple = com.tr.hsyn.resripple.R.drawable.ripple_yonca;
					break;
				case ALEV: ripple = com.tr.hsyn.resripple.R.drawable.ripple_alev;
					break;
				case HAVUC: ripple = com.tr.hsyn.resripple.R.drawable.ripple_havuc;
					break;
			}
			
			//@on
			return ripple;
		}
		
		/**
		 * Verilen rengin index değerini döndürür.
		 *
		 * @param context Context
		 * @param color   Aranan renk
		 * @return index. Yoksa {@code -1}
		 */
		static int getColorIndex(@NonNull final Context context, int color) {
			
			int[] colors = getColors(context);
			
			for (int i = 0; i < colors.length; i++)
				if (colors[i] == color) return i;
			
			return -1;
		}
		
		/**
		 * Uygulamamızın renk paleti.
		 *
		 * @param context Uygulamamızın context'i
		 * @return Renkler
		 */
		@NonNull
		static int[] getColors(@NonNull final Context context) {
			
			return context.getResources().getIntArray(com.tr.hsyn.rescolors.R.array.color_array);
		}
		
		/**
		 * Verilen index'deki rengi döndür.
		 *
		 * @param context Context
		 * @param index   index
		 * @return Renk
		 */
		static int getColor(@NonNull final Context context, @IntRange(from = 0, to = 19) final int index) {
			
			return getColors(context)[index];
		}
		
		/**
		 * Verilen rengin ismini döndürür.
		 *
		 * @param context Context
		 * @param color   Renk
		 * @return Rengin ismi. Yoksa boş string
		 */
		@NonNull
		static String getColorName(@NonNull final Context context, int color) {
			
			int index = getColorIndex(context, color);
			
			if (index != -1)
				return getColorNames(context)[index];
			
			return "";
		}
		
		@NonNull
		static String[] getColorNames(@NonNull final Context context) {
			
			return context.getResources().getStringArray(com.tr.hsyn.rescolors.R.array.color_names);
		}
		
	}
	
	interface ColorSelection {
		
		/**
		 * Renklerin kayıt edildiği ayar dosyası
		 */
		String PREF_COLORS       = "colors";
		/**
		 * Kaydedilen primary color değerinin kayıt anahtarı
		 */
		String KEY_PRIMARY_COLOR = "primaryColor";
		
		/**
		 * Seçili rengi döndürür.
		 * Eğer henüz renk seçimi yapılmamışsa varsayılan renk döner.
		 * Bu renk aynı zamanda uygulamanın ana rengidir.
		 * Doğrudan dosyadan okuma yapar.
		 *
		 * @param context Context
		 * @return Seçili renk
		 */
		static int getSelected(@NonNull final Context context) {
			
			return context
					.getSharedPreferences(PREF_COLORS, Context.MODE_PRIVATE)
					.getInt(KEY_PRIMARY_COLOR, ContextCompat.getColor(context, com.tr.hsyn.rescolors.R.color.colorPrimary));
		}
		
		/**
		 * Rengi kaydet.
		 *
		 * @param context Context
		 * @param color   Renk
		 */
		static void setSelected(@NonNull final Context context, int color) {
			
			context.getSharedPreferences(PREF_COLORS, Context.MODE_PRIVATE)
					.edit().putInt(KEY_PRIMARY_COLOR, color)
					.apply();
			
			Color._colorHolder.setValue(ColorHolder.newHolder(
					color,
					Color.getColorHolder().getPrimaryColor(),
					Color.getRipple(context, color)));
		}
	}
}
