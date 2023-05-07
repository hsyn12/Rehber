package com.tr.hsyn.nextension.extension;


import com.tr.hsyn.nextension.NumberExtension;

import org.jetbrains.annotations.NotNull;


/**
 * -de -da -te -ta (Bulunma Hali) (Lokatif - Nerede, Kimde, Nede)
 */
public class LocativeNumber implements NumberExtension {
	
	public static void main(String[] args) {
		
		NumberExtension extention = new LocativeNumber();
		
		for (int i = 0; i < 101; i += 1) {
			
			System.out.printf("%d-%s\n", i, extention.getExt(i));
		}
		
		
	}
	
	@Override
	public @NotNull String getExt(long number) {
		
		String _number = String.valueOf(number);
		
		if (_number.length() == 1) return ones(_number);
		
		if (NumberExtension.isLastZero(_number)) {
			
			_number = NumberExtension.getLastByZero(_number);
			int  len   = _number.length();
			char first = _number.charAt(0);
			
			switch (len) {
				
				case 2:
					
					switch (first) {
						
						case '1':
						case '3':
						case '9': return resources.getString("at_soft_thick");
						case '2':
						case '5':
						case '8': return resources.getString("at_soft_thin");
						case '4':
						case '6': return resources.getString("at_hard_thick");
						case '7': return resources.getString("at_hard_thin");
					}
					break;
				
				case 3:
				case 4:
				case 5:
				case 6: return resources.getString("at_soft_thin");
				
				default: return resources.getString("at_soft_thick");
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
			case '9': return resources.getString("at_soft_thick");
			case '1':
			case '2':
			case '7':
			case '8': return resources.getString("at_soft_thin");
			case '3':
			case '4':
			case '5': return resources.getString("at_hard_thin");
			
			default: throw new IllegalArgumentException("This is not a number : " + number);
		}
	}
}
