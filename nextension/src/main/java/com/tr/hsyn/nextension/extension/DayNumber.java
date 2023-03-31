package com.tr.hsyn.nextension.extension;


import com.tr.hsyn.nextension.NumberExtention;

import org.jetbrains.annotations.NotNull;


/**
 * Taihlerde gün sayıları için gereken eki bulur.
 */
public class DayNumber implements NumberExtention {
	
	@Override
	public @NotNull String getExt(long number) {
		
		String _number = String.valueOf(number);
		
		if (_number.length() == 1) return ones(_number);
		
		if (NumberExtention.isLastZero(_number)) {
			
			if (_number.length() != 2)
				throw new IllegalArgumentException("Day number length just can be 2 but this is : " + _number.length());
			
			char first = _number.charAt(0);
			
			switch (first) {
				
				case '1':
				case '3': return "u";
				case '2': return "si";
			}
		}
		else return ones(_number);
		
		return "";
	}
	
	private @NotNull String ones(@NotNull String number) {
		
		char c = number.charAt(number.length() - 1);
		
		switch (c) {
			
			case '0': return "ı";
			case '1':
			case '5':
			case '8': return "i";
			case '2':
			case '7': return "si";
			case '3':
			case '4': return "ü";
			case '6': return "sı";
			case '9': return "u";
			
			default: throw new IllegalArgumentException("This is not a number : " + number);
		}
	}
}
