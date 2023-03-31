package com.tr.hsyn.nextension.extension;


import com.tr.hsyn.nextension.NumberExtention;

import org.jetbrains.annotations.NotNull;


/**
 * -den -ten -dan -tan (Çıkma Hali) (Ablatif - Nereden, Kimden, Neden)
 */
public class AblativeNumber implements NumberExtention {
	
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
						case '9': return "dan";
						
						case '2':
						case '5':
						case '8': return "den";
						
						case '4':
						case '6': return "tan";
						
						case '7': return "ten";
					}
					break;
				
				case 3:
				case 4:
				case 5:
				case 6: return "den";
				
				default: return "dan";
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
	@NotNull
	private String ones(@NotNull String number) {
		
		char c = number.charAt(number.length() - 1);
		
		switch (c) {
			
			case '0':
			case '6':
			case '9': return "dan";
			
			case '1':
			case '2':
			case '7':
			case '8': return "den";
			
			case '3':
			case '4':
			case '5': return "ten";
			
			default: throw new IllegalArgumentException("This is not a number : " + number);
		}
	}
	
	public static void main(String[] args) {
		
		NumberExtention extention = new AblativeNumber();
		
		for (int i = 1000000; i < 10000001; i += 100000) {
			
			System.out.printf("%d-%s\n", i, extention.getExt(i));
		}
		
		
	}
	
}
