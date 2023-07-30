package com.tr.hsyn.telefonrehberi.main;


import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.tr.hsyn.calldata.Type;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.textdrawable.TextDrawable;

import org.jetbrains.annotations.NotNull;


public interface Res {
	
	/**
	 * Creates a {@link Drawable} for items image.
	 *
	 * @param context the context
	 * @param name    the name to take the first letter to use in the drawable
	 * @return the drawable
	 */
	static Drawable textDrawable(@NotNull Context context, String name) {
		
		return TextDrawable.builder()
				.beginConfig()
				.useFont(ResourcesCompat.getFont(context, com.tr.hsyn.resfont.R.font.z))
				.endConfig()
				.buildRound(Stringx.getLetter(name), Colors.getRandomColor());
	}
	
	/**
	 * Creates a {@link Drawable} for items image.
	 *
	 * @param context the context
	 * @param name    the name to take the first letter to use in the drawable
	 * @param fontRes the font resource
	 * @return the drawable
	 */
	static Drawable textDrawable(@NotNull Context context, String name, int fontRes) {
		
		return TextDrawable.builder()
				.beginConfig()
				.useFont(ResourcesCompat.getFont(context, fontRes))
				.endConfig()
				.buildRound(Stringx.getLetter(name), Colors.getRandomColor());
	}
	
	interface Call {
		
		/**
		 * Returns the call type string for the given call type.
		 *
		 * @param context the context
		 * @param type    the call type
		 * @return the call type string
		 */
		@NonNull
		static String getCallType(@NotNull Context context, int type) {
			
			switch (type) {
				
				case Type.INCOMING:
				case Type.INCOMING_WIFI: return context.getString(R.string.call_type_incoming);
				case Type.OUTGOING:
				case Type.OUTGOING_WIFI: return context.getString(R.string.call_type_outgoing);
				case Type.MISSED: return context.getString(R.string.call_type_missed);
				case Type.REJECTED: return context.getString(R.string.call_type_rejected);
				case Type.BLOCKED: return context.getString(R.string.call_type_blocked);
				
				default: return "";
			}
		}
		
		/**
		 * Returns the name equivalent of the filtering options used in the call log.
		 *
		 * @param context context
		 * @param filter  the filter
		 * @return the name of the filter
		 */
		static String getCallFilterName(@NotNull Context context, int filter) {
			
			String[] filters = context.getResources().getStringArray(R.array.call_filter_items);
			
			return filters[filter];
		}
	}
}
