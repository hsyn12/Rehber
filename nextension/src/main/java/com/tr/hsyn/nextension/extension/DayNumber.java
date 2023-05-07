package com.tr.hsyn.nextension.extension;


import com.tr.hsyn.nextension.NumberExtension;

import org.jetbrains.annotations.NotNull;


/**
 * Tarihlerde gün sayıları için gereken eki bulur.
 */
public class DayNumber implements NumberExtension {
	
	@Override
	public @NotNull String getExt(long number) {
		
		String _number = String.valueOf(number);
		
		if (_number.length() == 1) return ones(_number);
		
		if (NumberExtension.isLastZero(_number)) {
			
			if (_number.length() != 2)
				throw new IllegalArgumentException("Day number length just can be 2 but this is : " + _number.length());
			
			char first = _number.charAt(0);
			
			switch (first) {
				
				case '1':
				case '3': return resources.getString("into_soft_thick");
				case '2': return resources.getString("day_si");
			}
		}
		else return ones(_number);
		
		return "";
	}
	
	private @NotNull String ones(@NotNull String number) {
		
		char c = number.charAt(number.length() - 1);
		
		switch (c) {
			
			case '0': return resources.getString("into_hard_thick");
			case '1':
			case '5':
			case '8': return resources.getString("into_hard_thin");
			case '2':
			case '7': return resources.getString("day_si");
			case '3':
			case '4': return resources.getString("into_soft_thin_u");
			case '6': return resources.getString("day_s_thick");
			case '9': return resources.getString("into_soft_thick");
			
			default: throw new IllegalArgumentException("This is not a number : " + number);
		}
	}
}
