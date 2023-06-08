package com.tr.hsyn.nextension.extension;


import com.tr.hsyn.nextension.NumberExtension;

import org.jetbrains.annotations.NotNull;


/**
 * -lık -lik -luk -lük extensions for numbers
 */
public class AbstractNumber implements NumberExtension {
	
	@Override
	public @NotNull String getExt(long number) {
		
		if (number == 0) return "";
		
		String _number = String.valueOf(number);
		
		if (_number.length() == 1) return ones(_number);
		
		if (NumberExtension.isLastZero(_number)) {
			
			_number = NumberExtension.getLastByZero(_number);
			
			if (NumberExtension.isLastZeroAll(_number)) {
				
				if (_number.length() > 3) {
					
					switch (_number.length()) {
						
						case 4:
						case 5:
						case 6: return resources.getString("abs_thin");
						case 7:
						case 8:
						case 9:
						case 13:
						case 14:
						case 15: return resources.getString("abs_thick_ou");
						case 10:
						case 11:
						case 12:
						case 16:
						case 17:
						case 18: return resources.getString("abs_thick");
					}
				}
			}
			
			if (NumberExtension.isLastZero(_number.substring(0, _number.length() - 1)))
				return resources.getString("abs_thin_ou");
			
			char last = _number.charAt(_number.length() - 2);
			
			switch (last) {
				
				case '1':
				case '3': return resources.getString("abs_thick_ou");
				case '2':
				case '5':
				case '7':
				case '8': return resources.getString("abs_thin");
				case '4':
				case '6':
				case '9': return resources.getString("abs_thick");
			}
		}
		else return ones(_number);
		
		return "";
	}
	
	private @NotNull String ones(@NotNull String number) {
		
		char c = number.charAt(number.length() - 1);
		
		switch (c) {
			
			case '0': return "";
			case '1':
			case '2':
			case '5':
			case '7':
			case '8': return resources.getString("abs_thin");
			
			case '3':
			case '4': return resources.getString("abs_thin_ou");
			case '6': return resources.getString("abs_thick");
			case '9': return resources.getString("abs_thick_ou");
			
			default: throw new IllegalArgumentException("This is not a number : " + number);
		}
	}
	
	public static void main(String[] args) {
		
		long[] numbers = {
				1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000, 10000000000L, 100000000000L, 1000000000000L
		};
		
		AbstractNumber ext = new AbstractNumber();
		
		for (long number : numbers) {
			
			System.out.printf("%d : %s\n", number, ext.getExt(number));
		}
	}
}
