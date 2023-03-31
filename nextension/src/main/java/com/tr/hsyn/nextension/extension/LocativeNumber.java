package com.tr.hsyn.nextension.extension;


import com.tr.hsyn.nextension.NumberExtention;

import org.jetbrains.annotations.NotNull;


/**
 * -de -da -te -ta (Bulunma Hali) (Lokatif - Nerede, Kimde, Nede)
 */
public class LocativeNumber implements NumberExtention {
	
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
						case '3':
						case '9': return "da";
						case '2':
						case '5':
						case '8': return "de";
						case '4':
						case '6': return "ta";
						case '7': return "te";
					}
					break;
				
				case 3:
				case 4:
				case 5:
				case 6: return "de";
				
				default: return "da";
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
			
			case '0':
			case '6':
			case '9': return "da";
			case '1':
			case '2':
			case '7':
			case '8': return "de";
			case '3':
			case '4':
			case '5': return "te";
			
			default: throw new IllegalArgumentException("This is not a number : " + number);
		}
	}
	
	public static void main(String[] args) {
		
		NumberExtention extention = new LocativeNumber();
		
		for (int i = 0; i < 101; i += 1) {
			
			System.out.printf("%d-%s\n", i, extention.getExt(i));
		}
		
		
	}
}
