package com.tr.hsyn.nextension.extension;


import com.tr.hsyn.nextension.NumberExtention;

import org.jetbrains.annotations.NotNull;


/**
 * -in -ün -nin -nün -un -ın (İyelik Eki)
 */
public class PossessiveNumber implements NumberExtention {
	
	@Override
	public @NotNull String getExt(long number) {
		
		String _number = String.valueOf(number);
		
		if (_number.length() == 1) return ones(_number);
		
		if (NumberExtention.isLastZero(_number)) {
			
			_number = NumberExtention.getLastByZero(_number);
			int  len   = _number.length();
			char first = _number.charAt(0);
			
			switch (len) {
				
				case 2:
					
					switch (first) {
						
						case '1':
						case '3': return "un";
						case '2':
						case '5': return "nin";
						case '7':
						case '8': return "in";
						case '6':
						case '4':
						case '9': return "ın";
					}
					break;
				
				case 3: return "ün";
				case 4:
				case 5:
				case 6: return "in";
				
				default: return "un";
			}
		}
		else return ones(_number);
		
		return "";
	}
	
	/**
	 * Birler basamağı için uygun eki döndürür.<br>
	 * Sayı ya tek basamaklı olmalı ya da sonu sıfırla bitmeyen bir sayı olmalı.
	 *
	 * @param number Sayı
	 * @return Ek
	 */
	private @NotNull String ones(@NotNull String number) {
		
		char c = number.charAt(number.length() - 1);
		
		switch (c) {
			
			case '0': return "ın";
			case '1':
			case '5':
			case '8': return "in";
			case '2':
			case '7': return "nin";
			case '3':
			case '4': return "ün";
			case '6': return "nın";
			case '9': return "un";
			
			default: throw new IllegalArgumentException("This is not a number : " + number);
		}
	}
	
	public static void main(String[] args) {
		
		NumberExtention extention = new PossessiveNumber();
		
		for (int i = 100; i < 10001; i += 10) {
			
			System.out.printf("%d-%s\n", i, extention.getExt(i));
		}
		
		
	}
	
}
